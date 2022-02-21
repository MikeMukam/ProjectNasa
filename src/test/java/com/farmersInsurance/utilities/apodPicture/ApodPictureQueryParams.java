package com.farmersInsurance.utilities.apodPicture;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ApodPictureQueryParams {
    API_KEY("api_key"),
    COUNT("count"),
    DATE("date"),
    START_DATE("start_date"),
    THUMBS("thumbs"),
    END_DATE("end_date");

    private String key;
}
