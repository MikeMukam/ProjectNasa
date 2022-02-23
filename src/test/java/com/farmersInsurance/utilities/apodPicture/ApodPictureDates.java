package com.farmersInsurance.utilities.apodPicture;

import com.farmersInsurance.utilities.Utils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public enum ApodPictureDates {
    RANGE_START(LocalDate.parse("1995-06-16")),
    DEFAULT(Utils.getET_zoneNow()),
    VALID(Utils.getET_zoneNow().minusMonths(1)),
    START(Utils.getET_zoneNow().minusYears(1)),
    RANGE_END(Utils.getET_zoneNow());

    private LocalDate date;

    public String getDateAsString() {
        return date.toString();
    }
}
