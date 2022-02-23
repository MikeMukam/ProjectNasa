package com.farmersInsurance.utilities;

import com.farmersInsurance.utilities.apodPicture.ApodPictureQueryParams;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Utils {

    public static String getApiKeyValue() {
        return ConfigurationReader.getProperty("api_key");
    }

    public static String getApiKey() {
        return ApodPictureQueryParams.API_KEY.getKey();
    }

    public static String apodDataFormat(final LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }

    public static LocalDate getET_zoneNow(){
        return LocalDate.now(ZoneId.of( "America/New_York" ));
    }
}
