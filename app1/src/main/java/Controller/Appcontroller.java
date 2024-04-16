package Controller;



import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Appcontroller {
	 
	  @PostMapping("/analysis/process")
	    public ResponseEntity<String> analyzeText(@RequestBody String text) {
	        String result = performAnalysis(text); // Implement analysis logic
	        return ResponseEntity.ok(result);
	    }

	    public String performAnalysis(String text) {
	    	String prompt = text;
	        RestTemplate restTemplate = new RestTemplate();
	        String url = "http://20.163.181.186:5001/chat/?prompt="+ prompt;
	        String authorizationHeader = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjbGllbnRfaWQiOiJqb2huQGpvaG4uY29tIn0.C7ZM47jRHCbh22EG2CMaU6k2DncAUmG-2KnGCeX8HYI";
	        
	        // Set the Authorization header
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Authorization", authorizationHeader);

	        // Create a new HttpEntity with the headers
	        HttpEntity<String> entity = new HttpEntity<>(headers);

	        // Make the HTTP GET request
	        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
	        
	        // Return the response body
	        return response.getBody();
	    }
}
