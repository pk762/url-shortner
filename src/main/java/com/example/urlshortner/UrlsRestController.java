package com.example.urlshortner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class UrlsRestController {

    private final UrlsRepository urlsRepository;

    @PostMapping("/urls")
    public ResponseEntity addShortUrl(@RequestBody UrlsPojo urlsPojo) {

        LocalDateTime time = LocalDateTime.now();

        UrlsEntity urlsEntity = urlsRepository.save(
                UrlsEntity
                        .builder()
                        .shortUrl(String.valueOf(time.toEpochSecond(ZoneOffset.UTC)))
                        .calls(0)
                        .url(urlsPojo.getUrl()).build());

        return ResponseEntity.ok().body(urlsEntity);
    }

    @GetMapping("/")
    public ResponseEntity getAll() {

        return ResponseEntity.ok().body(urlsRepository.findAll());
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity redirectTo(@PathVariable String shortUrl) {

        List<UrlsEntity> uris = urlsRepository.findByShortUrl(shortUrl);

        try {
            UrlsEntity urlsEntity = uris.get(0);
            URI rUri = new URI("http://" + urlsEntity.getUrl());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(rUri);

            urlsEntity.setCalls(urlsEntity.getCalls() + 1);
            urlsRepository.save(urlsEntity);

            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        } catch (URISyntaxException e) {
            throw new RuntimeException(
                    String.format("Could not redirect to '%s' due to syntax problem.", uris.get(0).getUrl()), e);
        }
    }

    @GetMapping("/{shortUrl}/stats")
    public ResponseEntity showStats(@PathVariable String shortUrl) {

        List<UrlsEntity> uris = urlsRepository.findByShortUrl(shortUrl);
        UrlsEntity urlsEntity = uris.get(0);

        return ResponseEntity.ok().body(urlsEntity.getCalls());
    }
}

@NoArgsConstructor
@AllArgsConstructor
@Getter
class UrlsPojo {

    private String url;
}