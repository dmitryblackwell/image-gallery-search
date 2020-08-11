package com.blackwell.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PicturesResponse implements Serializable {

    private static final long serialVersionUID = 6196746965222514673L;

    private List<PictureResponse> pictures = null;
    private Integer page;
    private Integer pageCount;
    private Boolean hasMore;
}
