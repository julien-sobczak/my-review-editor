package fr.imlovinit.myrevieweditor;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.imlovinit.myrevieweditor.SearchResult.Hit;

/**
 * Based on the code: https://github.com/apache/lucene-solr/blob/master/lucene/demo/src/java/org/apache/lucene/demo/SearchFiles.java
 */
@RestController
@RequestMapping("/api")
public class SearchController {

	@RequestMapping("/search")
	public SearchResult search(@RequestParam("q") String queryString) throws IOException, ParseException {

		String index = "index";
		String field = "commentText";
		int hitsPerPage = 25;

		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer();

		QueryParser parser = new QueryParser(field, analyzer);
		Query query = parser.parse(queryString);

		long startTime = System.currentTimeMillis();
		searcher.search(query, 100);

		TopDocs results = searcher.search(query, hitsPerPage);
		ScoreDoc[] hits = results.scoreDocs;

		int numTotalHits = results.totalHits;

		int start = 0;
		int end = Math.min(numTotalHits, hitsPerPage);

		if (end > hits.length) {
			System.out.println("Only results 1 - " + hits.length + " of " + numTotalHits
					+ " total matching documents collected.");
			hits = searcher.search(query, numTotalHits).scoreDocs;
		}

		end = Math.min(hits.length, start + hitsPerPage);

		List<String> tokens = tokenizeString(analyzer, queryString);
		
		SearchResult result = new SearchResult();
		result.setTook(System.currentTimeMillis() - startTime);
		result.setTotal(numTotalHits);
		result.setQuery(queryString);
		result.setAnalyzedQuery(query.toString(field));
		result.setHits(new ArrayList<>());
		
		for (int i = start; i < end; i++) {
			Document doc = searcher.doc(hits[i].doc);

			String comment = newlineToTag(highlightTokens(doc.get("comment"), tokens));
			String title = newlineToTag(highlightTokens(doc.get("title"), tokens));
			
			Hit hit = new Hit();
			hit.setBook(doc.get("book"));
			hit.setReviewer(doc.get("author"));
			hit.setDate(doc.get("date"));
			hit.setNote(Integer.valueOf(doc.get("note")));
			hit.setRank(Integer.valueOf(doc.get("rank")));
			hit.setTotal(Integer.valueOf(doc.get("total")));
			hit.setVotes(Integer.valueOf(doc.get("votes")));
			hit.setTitle(doc.get("title"));
			hit.setComment(doc.get("comment"));
			// +
			hit.setTitleHighlight(title);
			hit.setCommentHighlight(comment);
			hit.setExtractHighlight(extract(doc.get("title") + ". " + doc.get("comment"), tokens));
			result.getHits().add(hit);
		}

		reader.close();
		
		return result;
	}
	
	private String newlineToTag(String text) {
		StringBuilder result = new StringBuilder();

		for (String paragraph : text.split("\\r?\\n")) {
			result.append("<p>").append(paragraph.trim()).append("</p>").append("\n\n");
		}

		return result.toString();
	}

	static String extract(String text, List<String> tokens) {
		String highlightedText = highlightTokens(text, tokens);
		
		StringBuilder result = new StringBuilder();
		
		boolean keepPreviousPhrase = true;
		boolean empty = true;
		
		for (String phrase : highlightedText.split("(\\r?\\n)|[.?!;]")) {
			if (phrase.contains("<em>")) {
				if (!keepPreviousPhrase) {
					result.append("...");
				} else if (!empty){
					result.append(". ");
				}
				result.append(phrase.trim());
				keepPreviousPhrase = true;
				empty = false;
			} else {
				keepPreviousPhrase = false;
			}
		}
		
		if (keepPreviousPhrase) {
			result.append(".");
		} else {
			result.append("...");
		}
		
		return result.toString();
	}
	
	static String highlightTokens(String text, List<String> tokens) {
		String result = text;
		for (String eachToken : tokens) {
			result = result.replaceAll("(?i)" + eachToken, "<em>" + eachToken + "</em>");
		}
		// Highlight consecutive terms together
		result = result.replaceAll("</em>([\\s,;:.]*)<em>", "$1");
		return result;
	}

	public static List<String> tokenizeString(Analyzer analyzer, String string) {
	    List<String> result = new ArrayList<String>();
	    try {
	      TokenStream stream  = analyzer.tokenStream(null, new StringReader(string));
	      stream.reset();
	      while (stream.incrementToken()) {
	        result.add(stream.getAttribute(CharTermAttribute.class).toString());
	      }
	    } catch (IOException e) {
	      // not thrown b/c we're using a string reader...
	      throw new RuntimeException(e);
	    }
	    return result;
	  }
	
	public static void main(String[] args) {
		try {
			SearchResult result = new SearchController().search("A very well-written book");
			System.out.println("Search took " + result.getTook() + "ms");
			for (Hit eachHit : result.getHits()) {
				System.out.println(eachHit.getTitleHighlight());
				System.out.println(eachHit.getCommentHighlight());
				System.out.println(eachHit.getExtractHighlight());
				System.out.println("\n =================== \n");
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
}