package com.blackwell.service;

import com.blackwell.response.FullPictureResponse;
import com.blackwell.response.PictureResponse;
import com.blackwell.response.PicturesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ImageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);
    public static final String PICTURES = "PICTURES";
    public static final String PICTURE = "PICTURE";

    private final ApiEndpointService apiEndpointService;
    private final HashOperations<String, Integer, PicturesResponse> picturesHashOperations;
    private final HashOperations<String, String, FullPictureResponse> pictureHashOperations;

    public ImageService(RedisTemplate<String, PicturesResponse> redisPicturesTemplate, RedisTemplate<String, FullPictureResponse> redisPictureTemplate, ApiEndpointService apiEndpointService) {
        this.apiEndpointService = apiEndpointService;
        this.picturesHashOperations = redisPicturesTemplate.opsForHash();
        this.pictureHashOperations = redisPictureTemplate.opsForHash();
    }

    private FullPictureResponse getPicture(String id, boolean callApi) {
        FullPictureResponse res = this.pictureHashOperations.get(PICTURE, id);
        if (res == null || callApi) {
            LOGGER.info("Calling API for picture with id " + id);
            res = apiEndpointService.getPicture(id);
            this.pictureHashOperations.put(PICTURE, id, res);
        } else {
            LOGGER.info("Get Picture with id " + id + " from cache.");
        }
        return res;
    }

    public FullPictureResponse getPicture(String id) {
        return this.getPicture(id, false);
    }
    public FullPictureResponse getPictureWithCacheReload(String id) {
        return this.getPicture(id, true);
    }

    private PicturesResponse getPictures(int page, boolean callApi) {
        PicturesResponse res = this.picturesHashOperations.get(PICTURES, page);
        if (res == null || callApi) {
            LOGGER.info("Calling API for page " + page);
            res = apiEndpointService.getPictures(page);
            this.picturesHashOperations.put(PICTURES, page, res);
        } else {
            LOGGER.info("Get Pictures for page " + page + " from cache.");
        }
        return res;
    }

    public PicturesResponse getPictures(int page) {
        return this.getPictures(page, false);
    }
    public PicturesResponse getPicturesWithCacheReload(int page) {
        return this.getPictures(page, true);
    }

    public void updateFullDatasetWithCacheReload() {
        PicturesResponse picturesResponse = this.getPicturesWithCacheReload(1);
        do {
            for(PictureResponse pictureResponse : picturesResponse.getPictures()) {
                this.getPictureWithCacheReload(pictureResponse.getId());
            }
            picturesResponse = this.getPicturesWithCacheReload(picturesResponse.getPage() + 1);
        } while (picturesResponse.getHasMore());
    }

    public List<FullPictureResponse> searchPictures(String value) {
        List<FullPictureResponse> fullPictureResponseList = new LinkedList<>();
        FullPictureResponse picture;
        PicturesResponse picturesResponse = this.getPictures(1);
        do {
            for(PictureResponse pictureResponse : picturesResponse.getPictures()) {
                picture = this.getPicture(pictureResponse.getId());
                if (picture.containsSearchValue(value)) {
                    fullPictureResponseList.add(picture);
                }
            }
            picturesResponse = this.getPictures(picturesResponse.getPage() + 1);
        } while (picturesResponse.getHasMore());
        return fullPictureResponseList;
    }
}
