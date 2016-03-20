package fr.imlovinit.myrevieweditor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class MarkdownReviewParserTest {

	@Test
	public void split_shouldSplitReview() throws Exception {
		// Given
		String reviews = read("sample.txt");
		
		// When
		List<Review> actual = new MarkdownReviewParser().parse(reviews);
		
		// Then
		List<Review> expected = Arrays.asList(
				new Review.Builder().
					amazonReviewId("R1X1BTRM9QM5VI").
					author("Amazon Customer").
					book("Python Pocket Reference (Pocket Reference (O'Reilly))").
					date("February 7, 2014").
					note("5").
					rank(1).
					total(58).
					votes(24).
					title("Worth keeping close at hand").
					comment("As someone who teaches beginning Pythonistas to learn to program, this updated edition of the Python Pocket Reference is worth keeping close at hand.\n\nThe reference covers both Python 3.4 and 2.7, and it highlights changes between the two major releases in a succinct, clear way. Yes, one can find the reference information in the freely available Python docs, but this guide earns its place in my backpack by pulling together practical tips, hints, and common gotchas when using a method or function.\n\nI appreciate the author including a section on the tkinter GUI module and tools. It’s helpful to have the basics in a concise format for the times that I am troubleshooting a user interface for a laboratory program or helping someone create a simple status display for an application.\n\nThough it might not be the most exciting read, the Python Pocket Reference does what it does well – a straightforward, up-to-date resource for Python 3.4 and 2.7. Its hints and common idioms may also save a programmer valuable time debugging common mistakes.").
					build(),
				new Review.Builder().
					amazonReviewId("R3AGNQQ10MTL52").
					author("Casper Chauncey").
					book("Python Pocket Reference (Pocket Reference (O'Reilly))").
					date("August 2, 2014").
					note("4").
					rank(2).
					total(58).
					votes(20).
					title("Pretty good, sometimes index lacks").
					comment("I refer to this frequently as I am learning Python and it usually gives me the answer but sometimes it takes quite a bit of searching and flipping back and forth. So I think the index and/or organization could be a little better. Some examples:\n\n* If you are trying to look up a concept but you don't know Python's word for it, alternate words likely won't get you there.\n\n* Suppose you want to copy an object, or assign the contents of one object to another, and you're trying to find the Python way to do that. The index does not have entries for \"copy\" or \"contents\". It does have an entry for \"assignment statement\" but that will just assign an object's reference to another variable so now you have two variables pointing to the same object. What you're looking for is under \"slicing\" but how would you find that unless you already knew it?\n\nA few more details / examples wouldn't hurt either although I understand they are trying to keep a \"pocket\" reference small.").
					build(),
				new Review.Builder().
					amazonReviewId("RQC9W4DKHC4HU").
					author("I. Blais").
					book("Python Pocket Reference (Pocket Reference (O'Reilly))").
					date("April 2, 2014").
					note("5").
					rank(3).
					total(58).
					votes(13).
					title("Great book for Python scripters").
					comment("This book has become a permanent resident in my backpack. I often find myself writing quick python scripts on the go for work. This book has proved immensely valuable when I'm working on a script and don't have network access (if I have internet it's generally faster to google it :). 5/5 would recommend.").
					build());

		assertThat(actual).usingFieldByFieldElementComparator().containsExactlyElementsOf(expected);
	}

	@SuppressWarnings("resource")
	private String read(String filename) {
		return new Scanner(MarkdownReviewParserTest.class.getClassLoader().getResourceAsStream(filename), "UTF-8").useDelimiter("\\A").next();
	}
	
}
