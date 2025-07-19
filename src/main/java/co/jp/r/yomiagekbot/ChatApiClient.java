package co.jp.r.yomiagekbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ChatApiClient {

    private String chatapiUrl;
    private RestTemplate restTemplate;

    public ChatApiClient(
            @Value("${chatapi.base-url}") final String chatapiUrl,
            final RestTemplate restTemplate) {
        this.chatapiUrl = chatapiUrl;
        this.restTemplate = restTemplate;
    }

    public String chat(String sentence) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ResponseEntity<ChatApiResponse>> entity = new HttpEntity<>(restTemplate.exchange(chatapiUrl, HttpMethod.POST, new HttpEntity<>(new ChatApiRequest(sentence), headers), ChatApiResponse.class), headers);
        String response = entity.getBody().getBody().response.toString();
        System.out.println(response);
        return response;
    }

    record ChatApiRequest(String message){}
    record ChatApiResponse(String response){}
}
