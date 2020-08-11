package com.blackwell.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullPictureResponse implements Serializable {

    private static final long serialVersionUID = -5238630847663950987L;

    private String id;
    private String author;
    private String camera;
    private String tags;

    @JsonProperty("cropped_picture")
    private String croppedPicture;
    @JsonProperty("full_picture")
    private String fullPicture;

    public boolean containsSearchValue(String value) {
        return  StringUtils.contains(author, value) ||
                StringUtils.contains(camera, value) ||
                StringUtils.contains(tags, value);
    }
}
