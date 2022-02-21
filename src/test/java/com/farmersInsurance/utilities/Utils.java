package com.farmersInsurance.utilities;

import com.farmersInsurance.utilities.apodPicture.ApodPictureQueryParams;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {

    public static String getApiKeyValue() {
        return ConfigurationReader.getProperty("api_key");
    }

    public static String getApiKey() {
        return ApodPictureQueryParams.API_KEY.getKey();
    }

    public static String apodDataFormat(final LocalDate date) {
        final String apodDateFormatPattern = "MMM dd, yyyy";
        return date.format(DateTimeFormatter.ofPattern(apodDateFormatPattern));
    }

    public static String apodDataFormat(final String date) {
        final String apodDateFormatPattern = "MMM dd, yyyy";
        return LocalDate.parse(date).format(DateTimeFormatter.ofPattern(apodDateFormatPattern));
    }
}
