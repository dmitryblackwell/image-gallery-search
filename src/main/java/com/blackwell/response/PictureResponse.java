package com.blackwell.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PictureResponse implements Serializable {

    private static final long serialVersionUID = -9185371569665268570L;

    private String id;
    @JsonProperty("cropped_picture")
    private String croppedPicture;
}
