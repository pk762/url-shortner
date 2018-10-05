package com.example.urlshortner;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Transactional
@Component
public interface UrlsRepository extends CrudRepository<UrlsEntity, UUID> {

    List<UrlsEntity> findByShortUrl(String shortUrl);
}
