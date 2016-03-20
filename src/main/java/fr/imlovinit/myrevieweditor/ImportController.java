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
public class ImportController {

	private MarkdownReviewParser parser = new MarkdownReviewParser();
	@SuppressWarnings("serial")
	private ObjectMapper objectMapper = new ObjectMapper();
	
    @RequestMapping("/")
    public String index() {
        return "Welcome to My Review Editor Application!";
    }
    
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<String> importReview(@RequestBody String reviewsRaw) {
    	
    	try {
	    	List<Review> reviews = parser.parse(reviewsRaw);
	    	
	    	for (Review eachReview : reviews) {
	    		String reviewJson;
					reviewJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(eachReview);
				System.out.println(reviewJson);
				String filename = eachReview.generateFilename("txt");
	    		
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