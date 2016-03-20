package fr.imlovinit.myrevieweditor;

public class Review {

	private String author;
	private String date;
	private String book;
	private String amazonReviewId;
	private String title;
	private String note;
	private int rank;
	private int total;
	private int votes;
	private String comment = "";

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public String getBook() {
		return book;
	}

	public void setBook(String book) {
		this.book = book;
	}

	public String getAmazonReviewId() {
		return amazonReviewId;
	}

	public void setAmazonReviewId(String amazonReviewId) {
		this.amazonReviewId = amazonReviewId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public static class Builder {
		
		private Review result = new Review();
		
		public Builder author(String author) {
			result.setAuthor(author);
			return this;
		}

		public Builder date(String date) {
			result.setDate(date);
			return this;
		}
		
		public Builder book(String book) {
			result.setBook(book);
			return this;
		}

		public Builder amazonReviewId(String amazonReviewId) {
			result.setAmazonReviewId(amazonReviewId);
			return this;
		}

		public Builder title(String title) {
			result.setTitle(title);
			return this;
		}

		public Builder note(String note) {
			result.setNote(note);
			return this;
		}

		public Builder rank(int rank) {
			result.setRank(rank);
			return this;
		}

		public Builder total(int total) {
			result.setTotal(total);
			return this;
		}

		public Builder votes(int votes) {
			result.setVotes(votes);
			return this;
		}

		public Builder comment(String comment) {
			result.setComment(comment);
			return this;
		}

		public Review build() {
			return result;
		}
		
	}
	
	public String generateFilename(String extension) {
		return String.format("%s-%s.%s", escape(getBook()), getAmazonReviewId(), extension);
	}
	
	private static String escape(String name) {
		int indexParenthesis = name.indexOf('(');
		if (indexParenthesis != -1) {
			name = name.substring(0, indexParenthesis).trim();
		}
		return name.replace(' ', '_').replace('.', '_').replaceAll("\\W", "");
	}
}
