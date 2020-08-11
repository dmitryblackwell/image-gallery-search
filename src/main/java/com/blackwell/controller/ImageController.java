package com.blackwell.controller;

import com.blackwell.response.FullPictureResponse;
import com.blackwell.response.PicturesResponse;
import com.blackwell.service.ImageService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@EnableScheduling
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/images")
    public PicturesResponse getPictures(@RequestParam(value = "page", defaultValue = "1") Integer page) {
        return imageService.getPictures(page);
    }

    @GetMapping("/images/{imageId}")
    public FullPictureResponse getPictures(@PathVariable String imageId) {
        return imageService.getPicture(imageId);
    }

    @Scheduled(fixedRateString = "${cache.reloadTime}")
    @GetMapping("/update")
    public void updateFullDataset() {
        imageService.updateFullDatasetWithCacheReload();
    }

    @GetMapping("/search/{value}")
    public List<FullPictureResponse> search(@PathVariable String value) {
        return this.imageService.searchPictures(value);
    }
}
