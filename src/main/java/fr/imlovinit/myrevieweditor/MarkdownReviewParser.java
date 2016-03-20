package fr.imlovinit.myrevieweditor;

import java.util.ArrayList;
import java.util.List;

public class MarkdownReviewParser {

	
	public List<Review> parse(String reviews) {
		List<Review> result = new ArrayList<>();
		Review currentReview = null;
		boolean inComment = false;
		for (String line : reviews.split("\n")) {
			if (line.startsWith("# ")) { // New review
				if (currentReview != null) {
					currentReview.setComment(currentReview.getComment().trim()); // HACK
					result.add(currentReview);
				}
				currentReview = new Review();
				currentReview.setTitle(line.substring(2));
			} else if (line.startsWith("<!--")){
				inComment = true;
			} else if (line.startsWith("  -->")) {
				inComment = false;
			} else {
				
				if (inComment) {
					int indexDot = line.indexOf(": ");
					String property = line.substring(5, indexDot);
					String value = line.substring(indexDot + 2).trim();
					switch (property) {
					case "id":
						currentReview.setAmazonReviewId(value);
						break;
					case "date":
						currentReview.setDate(value.substring(3));
						break;
					case "author":
						currentReview.setAuthor(value);
						break;
					case "book":
						currentReview.setBook(value);
						break;
					case "votes":
						currentReview.setVotes(Integer.parseInt(value));
						break;
					case "position":
						currentReview.setRank(Integer.parseInt(value));
						break;
					case "total":
						currentReview.setTotal(Integer.parseInt(value));
						break;
					case "note":
						currentReview.setNote(value);
						break;
					default:
						throw new IllegalArgumentException("Unknown property: " + property);
					}
				} else {
					currentReview.setComment(currentReview.getComment() + line + "\n");
				}
			}
		}
		if (currentReview != null) {
			currentReview.setComment(currentReview.getComment().trim()); // HACK
			result.add(currentReview);
		}
		return result;
	}
	
}
