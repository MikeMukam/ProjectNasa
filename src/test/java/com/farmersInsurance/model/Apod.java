package com.farmersInsurance.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class Apod {

    private final String date;
    private final String thumbnailUrl;
    private final String copyright;
    private final String explanation;
    private final String media_type;
    private final String service_version;
    private final String title;
    private final String url;
    private final String hdUrl;
    private final String errorMessage;

    @JsonCreator
    public Apod(@JsonProperty(value = "date", required = true) String date,
                @JsonProperty(value = "thumbnail_url") String thumbnailUrl,
                @JsonProperty(value = "explanation", required = true) String explanation,
                @JsonProperty(value = "hdurl") String hdUrl,
                @JsonProperty(value = "media_type", required = true) String mediaType,
                @JsonProperty(value = "service_version", required = true) String serviceVersion,
                @JsonProperty(value = "title", required = true) String title,
                @JsonProperty(value = "url", required = true) String url,
                @JsonProperty(value = "msg") String errorMessage,
                @JsonProperty(value = "copyright") String copyright) {

        this.date = date;
        this.thumbnailUrl = thumbnailUrl;
        this.copyright = copyright;
        this.hdUrl = hdUrl;
        this.media_type = mediaType;
        this.explanation = explanation;
        this.service_version = serviceVersion;
        this.title = title;
        this.errorMessage = errorMessage;
        this.url = url;
    }
}
