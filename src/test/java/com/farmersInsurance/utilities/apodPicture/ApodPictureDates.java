package com.farmersInsurance.utilities.apodPicture;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public enum ApodPictureDates {
    RANGE_START(LocalDate.parse("1995-06-16")),
    DEFAULT(LocalDate.now()),
    VALID(LocalDate.now().minusMonths(1)),
    START(LocalDate.now().minusYears(1)),
    RANGE_END(LocalDate.now());

    private LocalDate date;

    public String getDateAsString() {
        return date.toString();
    }
}
