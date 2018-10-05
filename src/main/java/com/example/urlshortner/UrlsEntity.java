package com.example.urlshortner;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;


@Entity
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UrlsEntity {

    @Id
    @GeneratedValue
    private UUID id;
    private String url;
    private String shortUrl;
    private long calls;
}
