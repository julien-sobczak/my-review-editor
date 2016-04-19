package fr.imlovinit.myrevieweditor;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class SearchControllerTest {
	
	@Test
	public void highlightTokens_wrapWithEmTag() throws Exception {
		// When
		String actual = SearchController.highlightTokens("Ceci est un example simple", Arrays.asList("example"));
		
		// Then
		String expected = "Ceci est un <em>example</em> simple";
		assertEquals(expected, actual);
	}
	
	@Test
	public void highlightTokens_mergeTwoConsecutiveEmTag() throws Exception {
		// When
		String actual = SearchController.highlightTokens("Ceci est un example simple", Arrays.asList("example", "simple"));
		
		// Then
		String expected = "Ceci est un <em>example simple</em>";
		assertEquals(expected, actual);
	}
	
	@Test
	public void highlightTokens_multipleSeparateWords() throws Exception {
		// When
		String actual = SearchController.highlightTokens("Ceci est un autre example tout aussi simple", 
				Arrays.asList("autre", "tout", "simple"));
		
		// Then
		String expected = "Ceci est un <em>autre</em> example <em>tout</em> aussi <em>simple</em>";
		assertEquals(expected, actual);
	}
	
	@Test
	public void extract_keepOnlyPhraseContainingAToken() throws Exception {
		// When
		String actual = SearchController.extract("Ceci est une phrase. Ceci est une deuxième phrase.", 
				Arrays.asList("deuxième"));
		
		// Then
		String expected = "...Ceci est une <em>deuxième</em> phrase.";
		assertEquals(expected, actual);
	}
	
	@Test
	public void extract_useDotsToSeparatePhrases() throws Exception {
		// When
		String actual = SearchController.extract("Ceci est une phrase. Ceci en est une deuxième. C'est la dernière phrase.", 
				Arrays.asList("phrase"));
		
		// Then
		String expected = "Ceci est une <em>phrase</em>...C'est la dernière <em>phrase</em>.";
		assertEquals(expected, actual);
	}
	
	@Test
	public void extract_useSingleDotIfSucessivePhrases() throws Exception {
		// When
		String actual = SearchController.extract("Ceci est une phrase. Ceci est une autre phrase. Ceci est la dernière.", 
				Arrays.asList("phrase"));
		
		// Then
		String expected = "Ceci est une <em>phrase</em>. Ceci est une autre <em>phrase</em>...";
		assertEquals(expected, actual);
	}
	
}