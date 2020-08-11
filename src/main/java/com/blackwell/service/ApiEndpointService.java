package com.blackwell.service;

import com.blackwell.response.FullPictureResponse;
import com.blackwell.response.PicturesResponse;
import com.blackwell.response.TokenResponse;
import com.blackwell.exception.InvalidTokenResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class ApiEndpointService implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiEndpointService.class);

    private static final String HOST = "http://interview.agileengine.com/";
    private static final String GET_PICTURES_URL = HOST + "images";
    private static final String POST_TOKEN_URL = HOST + "auth";

    private RestTemplate restTemplate;
    private HttpEntity<String> tokenEntity;

    @Value("${apiKey}")
    private String apiKey;

    public FullPictureResponse getPicture(String id) {
        String uri = GET_PICTURES_URL + "/" + id;
        try {
            ResponseEntity<FullPictureResponse> res =
                    this.restTemplate.exchange(uri, HttpMethod.GET, tokenEntity, FullPictureResponse.class);
            return res.getBody();
        } catch (HttpClientErrorException exception) {
            this.renewToken();
            throw exception;
        }
    }

    public PicturesResponse getPictures(int page) {
        String uri = UriComponentsBuilder.fromHttpUrl(GET_PICTURES_URL)
                .queryParam("page", page).toUriString();
        try {
            ResponseEntity<PicturesResponse> res =
                    this.restTemplate.exchange(uri, HttpMethod.GET, tokenEntity, PicturesResponse.class);
            return res.getBody();
        } catch (HttpClientErrorException exception) {
            this.renewToken();
            throw exception;
        }
    }

    private void renewToken() {
        try {
            this.updateToken();
        } catch (InvalidTokenResponseException exception){
            LOGGER.error("Exception was thrown while renewing token.", exception);
        }
    }

    private void updateToken() throws InvalidTokenResponseException {
        Map<String, String> map = new HashMap<>();
        map.put("apiKey", apiKey);

        TokenResponse response = this.restTemplate.postForEntity(POST_TOKEN_URL, map, TokenResponse.class).getBody();
        if (response == null || !response.isAuth()) {
            throw new InvalidTokenResponseException("Token Response is invalid. Maybe api key is incorrect?");
        }
        LOGGER.info("Successfully obtain token with api key!");

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(response.getToken());

        this.tokenEntity = new HttpEntity<>("body", headers);
    }

    @Override
    public void afterPropertiesSet() throws InvalidTokenResponseException {
        this.restTemplate = new RestTemplate();
        this.updateToken();
    }
}
