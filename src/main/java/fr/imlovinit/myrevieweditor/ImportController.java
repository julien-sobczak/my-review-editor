package fr.imlovinit.myrevieweditor;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
public class ImportController {

	private MarkdownReviewParser parser = new MarkdownReviewParser();

	private ObjectMapper objectMapper = new ObjectMapper();
	
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public ResponseEntity<String> importReview(@RequestBody String reviewsRaw) {
    	
    	try {
	    	List<Review> reviews = parser.parse(reviewsRaw);
	    	
	    	for (Review eachReview : reviews) {
	    		String reviewJson;
					reviewJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(eachReview);
				String filename = eachReview.generateFilename("txt");
				System.out.println(filename);
				System.out.println(reviewJson);
	    		
	    		Files.write(Paths.get("C:/Workspaces/workshop/my-review-editor/data/" + filename), 
	    				reviewJson.getBytes(Charset.forName("UTF-8")));
	    	}

	    	return ResponseEntity.ok("Successfully imported");
    	
    	} catch (Exception e) {
    		e.printStackTrace();
    		return ResponseEntity.badRequest().body(e.getMessage());
    	}
    }

}