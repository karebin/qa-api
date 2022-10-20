package data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Params  {
    @Value("${base.url}")
    protected String baseUrl;
    public ObjectMapper mapper = new ObjectMapper();
    public RestTemplate restTemplate = new RestTemplate();
    public ResponseEntity<String> response;
}