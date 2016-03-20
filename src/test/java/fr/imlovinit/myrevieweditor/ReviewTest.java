package fr.imlovinit.myrevieweditor;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ReviewTest {

	@Test
	public void generateFilename() throws Exception {
		Review review = new Review.Builder().
				book("Python Pocket Reference (Pocket Reference (O'Reilly))").
				amazonReviewId("R1X1BTRM9QM5VI").
				build();
		
		String filename = review.generateFilename("txt");
		
		assertThat(filename).isEqualTo("Python_Pocket_Reference-R1X1BTRM9QM5VI.txt");
	}
}
