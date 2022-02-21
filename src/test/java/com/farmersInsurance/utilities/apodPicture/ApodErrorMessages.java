package com.farmersInsurance.utilities.apodPicture;

import com.farmersInsurance.utilities.Utils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
@Getter
public enum ApodErrorMessages {

    DATE_NOT_WITHIN_CORRECT_RANGE(String.format("Date must be between Jun 16, 1995 and %s.", Utils.apodDataFormat(ApodPictureDates.RANGE_END.getDate()))),
    NO_DATA_AVAILABLE("No data available for date: %s"),
    MAX_COUNT_EXCEEDED("Count must be positive and cannot exceed 100"),
    WRONG_COMBINATION("Bad Request: invalid field combination passed. Allowed request fields for apod method are 'concept_tags', 'date', 'hd', 'count', 'start_date', 'end_date', 'thumbs'"),
    DATE_FORMAT_MISMATCH("time data '%s' does not match format");

    private String text;

    public String getText(final String date) {
        return String.format(getText(), date);
    }
}
