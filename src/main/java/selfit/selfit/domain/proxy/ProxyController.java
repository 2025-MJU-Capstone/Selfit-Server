package selfit.selfit.domain.proxy;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ProxyController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/proxy/model")
    public ResponseEntity<Resource> proxyModel(@RequestParam String url) {
        // 메쉬 API로부터 모델 다운로드
        ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(response.getBody().length);

        return new ResponseEntity<>(new ByteArrayResource(response.getBody()), headers, HttpStatus.OK);
    }
}
