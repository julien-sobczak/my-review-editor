package fr.imlovinit.myrevieweditor;

import java.util.List;

public class SearchResult {
	
	private String query;
	private String analyzedQuery;
	private long took;
	private List<Hit> hits;
	private long total;

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getAnalyzedQuery() {
		return analyzedQuery;
	}

	public void setAnalyzedQuery(String analyzedQuery) {
		this.analyzedQuery = analyzedQuery;
	}

	public long getTook() {
		return took;
	}

	public void setTook(long took) {
		this.took = took;
	}
	
	public long getTotal() {
		return total;
	}
	
	public void setTotal(long total) {
		this.total = total;
	}

	public List<Hit> getHits() {
		return hits;
	}

	public void setHits(List<Hit> hits) {
		this.hits = hits;
	}

	public static class Hit {

		private String book;
		private String reviewer;
		private String date;
		private int note;
		private int rank;
		private int total;
		private int votes;
		private String title;
		private String comment;
		private String titleHighlight;
		private String commentHighlight;
		private String extractHighlight;

		public String getBook() {
			return book;
		}

		public void setBook(String book) {
			this.book = book;
		}

		public String getReviewer() {
			return reviewer;
		}

		public void setReviewer(String reviewer) {
			this.reviewer = reviewer;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public int getNote() {
			return note;
		}

		public void setNote(int note) {
			this.note = note;
		}

		public int getRank() {
			return rank;
		}

		public void setRank(int rank) {
			this.rank = rank;
		}

		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
			this.total = total;
		}

		public int getVotes() {
			return votes;
		}

		public void setVotes(int votes) {
			this.votes = votes;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}
		
		public String getTitleHighlight() {
			return titleHighlight;
		}

		public void setTitleHighlight(String titleHighlight) {
			this.titleHighlight = titleHighlight;
		}

		public String getCommentHighlight() {
			return commentHighlight;
		}

		public void setCommentHighlight(String commentHighlight) {
			this.commentHighlight = commentHighlight;
		}
		
		public String getExtractHighlight() {
			return extractHighlight;
		}
		
		public void setExtractHighlight(String extractHighlight) {
			this.extractHighlight = extractHighlight;
		}

	}
}
