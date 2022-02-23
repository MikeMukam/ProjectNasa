package com.farmersInsurance.apiTests;

import com.farmersInsurance.model.Apod;
import com.farmersInsurance.utilities.Utils;
import com.farmersInsurance.utilities.apodPicture.ApodErrorMessages;
import com.farmersInsurance.utilities.apodPicture.ApodHelper;
import com.farmersInsurance.utilities.apodPicture.ApodPictureDates;
import com.farmersInsurance.utilities.ConfigurationReader;
import com.farmersInsurance.utilities.apodPicture.ApodPictureQueryParams;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.awaitility.Awaitility;
import org.testng.Assert;
import org.testng.annotations.*;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @author : Mukam(Mike)
 * Created on : 02/18/2022
 */
public class NasaApiTests {

    private final ApodHelper apodHelper = new ApodHelper();

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = ConfigurationReader.getProperty("apod_url");
    }

    @Test(description = "Verify valid date and valid date format")
    public void dateVerifyValidity() {

        final Response response = apodHelper.getAPodResponse(Map.of(ApodPictureQueryParams.DATE.getKey(), ApodPictureDates.VALID.getDateAsString()));
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);

        final Apod apod = response.then().extract().as(Apod.class);

        final LocalDate actualDate = LocalDate.parse(apod.getDate());
        final LocalDate earliestDate = ApodPictureDates.RANGE_START.getDate();
        final LocalDate today = ApodPictureDates.DEFAULT.getDate();
        Assert.assertTrue(actualDate.isAfter(earliestDate) && actualDate.isBefore(today),
                String.format("%s date is not within the range of  %s - %s", actualDate, earliestDate, today));
    }

    @Test(description = "Verify default date to be Today (now)")
    public void dateVerifyDefaultValue() {
        final Response response = apodHelper.getAPodResponse(true);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);
        Assert.assertEquals(ApodPictureDates.DEFAULT.getDate(), LocalDate.parse(apodHelper.getDate(response)));
    }

    @Test(description = "Verify date query param is no earlier than 1995-06-16")
    public void dateVerifyErrorMessageForStartRange() {
        final LocalDate startDate = ApodPictureDates.RANGE_START.getDate();
        final Response response = apodHelper.getAPodResponse(Map.of(ApodPictureQueryParams.DATE.getKey(), startDate.minusDays(1).toString()));
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_BAD_REQUEST);
        Assert.assertEquals(apodHelper.getMsg(response), ApodErrorMessages.DATE_NOT_WITHIN_CORRECT_RANGE.getText());
    }

    @Test(description = "Verify date query param is no later than today local time")
    public void dateVerifyErrorMessageForEndRange() {
        final String date = ApodPictureDates.RANGE_END.getDate().plusDays(2).toString();
        final Response response = apodHelper.getAPodResponse(Map.of(ApodPictureQueryParams.DATE.getKey(), date));
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_BAD_REQUEST);
        Assert.assertEquals(apodHelper.getMsg(response), ApodErrorMessages.DATE_NOT_WITHIN_CORRECT_RANGE.getText());
    }

    /**
     * TODO: This is a potential bug
     */
    @Test(description = "Verify error Message for in case data is not avaliable")
    public void dateVerifyErrorMessageForNoDataAvailable() {
        final String date = ApodPictureDates.RANGE_START.getDate().plusDays(1).toString();
        final Response response = apodHelper.getAPodResponse(Map.of(ApodPictureQueryParams.DATE.getKey(), date));
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_NOT_FOUND);
        Assert.assertEquals(apodHelper.getMsg(response), ApodErrorMessages.NO_DATA_AVAILABLE.getText(date));
    }

    @Test(description = "Verify Invalid Date Format")
    public void dateVerifyInvalidity() {
       apodHelper.verifyDateFormat();
    }

    @Test(description = "Verify date of APOD is after provided start date")
    public void startDateVerifyStartDate() {
        final Response response = apodHelper.getAPodResponse(Map.of(ApodPictureQueryParams.START_DATE.getKey(), ApodPictureDates.START.getDateAsString()));
        apodHelper.verifyStartDateInValidDateRange(response);
    }

    @Test(description = "Verify invalid start date response with error message")
    public void startDateVerifyInvalidDate() {
        final LocalDate date = ApodPictureDates.RANGE_START.getDate().minusDays(1);
        final Response response = apodHelper.getAPodResponse(Map.of(ApodPictureQueryParams.DATE.getKey(), date.toString()));

        Assert.assertEquals(response.statusCode(), HttpStatus.SC_BAD_REQUEST);

        final String errorMessage = apodHelper.getMsg(response);
        Assert.assertEquals(errorMessage, ApodErrorMessages.DATE_NOT_WITHIN_CORRECT_RANGE.getText());
    }

    @Test(description = "Verify that Query params, date and start_date should return status code 200")
    public void startDateVerifyDateAndStartDateQueryParamsShouldReturnBadRequest() {
        final Response response = apodHelper.getAPodResponse(Map.of(ApodPictureQueryParams.DATE.getKey(), ApodPictureDates.VALID.getDateAsString(),
                                                                    ApodPictureQueryParams.START_DATE.getKey(), ApodPictureDates.VALID.getDateAsString()));
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_BAD_REQUEST);

        final String errorMessage = apodHelper.getMsg(response);
        Assert.assertEquals(errorMessage, ApodErrorMessages.WRONG_COMBINATION.getText());
    }

    @Test(description = "Verify that start_date and end_date are within correct range")
    public void endDateVerifyRangeBetweenStartDateAndEndDate() {
        final Response response = apodHelper.getAPodResponse(Map.of(ApodPictureQueryParams.START_DATE.getKey(), ApodPictureDates.START.getDateAsString(),
                                                                    ApodPictureQueryParams.END_DATE.getKey(), ApodPictureDates.VALID.getDateAsString()));
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);

        final List<Apod> apods = response.then().extract().as(new TypeRef<>() {
        });

        apods.forEach(apod -> {
            final LocalDate actualDate = LocalDate.parse(apod.getDate());
            final LocalDate rangeStartDate = ApodPictureDates.START.getDate().minusDays(1);
            final LocalDate rangeEndDate = ApodPictureDates.VALID.getDate().plusDays(1);
            Assert.assertTrue(actualDate.isAfter(rangeStartDate) &&
                            actualDate.isBefore(rangeEndDate),
                    String.format("%s date is after expected date %s", actualDate, rangeEndDate));
        });
    }

    @Test(description = "Verify that Query params, date and end_date should return status code 200")
    public void endDateVerifyDateAndStartDateQueryParamsShouldReturnBadRequest() {
        final Response response = apodHelper.getAPodResponse(Map.of(ApodPictureQueryParams.END_DATE.getKey(), ApodPictureDates.VALID.getDateAsString(),
                                                                    ApodPictureQueryParams.DATE.getKey(), ApodPictureDates.VALID.getDateAsString()));
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_BAD_REQUEST);

        final String errorMessage = apodHelper.getMsg(response);
        Assert.assertEquals(errorMessage, ApodErrorMessages.WRONG_COMBINATION.getText());
    }

    @Test(description = "Verify end date query parameter returns 400 status code when used without start date")
    public void endDateVerifyDate() {
        final Response response = apodHelper.getAPodResponse(Map.of(ApodPictureQueryParams.END_DATE.getKey(), ApodPictureDates.VALID.getDateAsString()));
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_BAD_REQUEST);
        Assert.assertEquals(apodHelper.getMsg(response), ApodErrorMessages.WRONG_COMBINATION.getText());
    }

    @Test(description = "Verify count query params should return status code 400 when used with start date")
    public void verifyCountShouldNotWorkWithEarliestDate() {
        final Response response = apodHelper.getAPodResponse(Map.of(ApodPictureQueryParams.DATE.getKey(), ApodPictureDates.START.getDateAsString(),
                                                                    ApodPictureQueryParams.COUNT.getKey(), 2));
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Verify max count is 100")
    public void countVerifyMaxCount() {
        final int maxCount = 100;
        final Response badResponse = apodHelper.getAPodResponse(Map.of(ApodPictureQueryParams.COUNT.getKey(), maxCount + 1));
        Assert.assertEquals(badResponse.statusCode(), HttpStatus.SC_BAD_REQUEST);

        final String countErrorMessage = apodHelper.getMsg(badResponse);
        Assert.assertEquals(countErrorMessage, ApodErrorMessages.MAX_COUNT_EXCEEDED.getText());

        final Response goodResponse = apodHelper.getAPodResponse(Map.of(ApodPictureQueryParams.COUNT.getKey(), maxCount));
        Assert.assertEquals(goodResponse.statusCode(), HttpStatus.SC_OK);
    }

    @Test(description = "Verify when thumb query param is set to false, it should not be present in response body")
    public void thumbVerifyThumbShouldNotBePresent() {
        final Response response = apodHelper.getAPodResponse(Map.of(ApodPictureQueryParams.THUMBS.getKey(), "false",
                                                                    ApodPictureQueryParams.START_DATE.getKey(), ApodPictureDates.START.getDateAsString(),
                                                                    ApodPictureQueryParams.END_DATE.getKey(), ApodPictureDates.VALID.getDateAsString()));

        final List<Apod> apods = response.then().extract().as(new TypeRef<>() {
        });

        apods.forEach(apod -> {
            final LocalDate actualDate = LocalDate.parse(apod.getDate());
            final LocalDate rangeStartDate = ApodPictureDates.START.getDate().minusDays(1);
            final LocalDate rangeEndDate = ApodPictureDates.VALID.getDate().plusDays(1);

            Assert.assertNull(apod.getThumbnailUrl());
            Assert.assertTrue(actualDate.isAfter(rangeStartDate) &&
                            actualDate.isBefore(rangeEndDate),
                    String.format("%s date is after expected date %s", actualDate, rangeEndDate));
        });
    }

    @Test(description = "Verify when thumb query param is set to true, it should  be present in response body")
    public void thumbVerifyThumbShouldBePresent() {
        final Response response = apodHelper.getAPodResponse(Map.of(ApodPictureQueryParams.THUMBS.getKey(), true,
                                                                    ApodPictureQueryParams.START_DATE.getKey(), ApodPictureDates.START.getDateAsString(),
                                                                    ApodPictureQueryParams.END_DATE.getKey(), ApodPictureDates.VALID.getDateAsString()));

        final List<Apod> apods = response.then().extract().as(new TypeRef<>() {});
        apods.stream().filter(apod -> apod.getThumbnailUrl() != null).findAny().orElseThrow(() -> new AssertionError("No thumb_nail field is found"));
    }

    @Test(description = "Verify API key with valid values")
    public void apiKeyVerifyWithValidValues() {
        Assert.assertEquals(apodHelper.getAPodResponse(true).statusCode(), HttpStatus.SC_OK);
    }

    @Test(description = "Verify API key without api key query parameter")
    public void apiKeyVerifyNullity() {
        apodHelper.verifyIfApiKeyIsMissing();
    }

    @Test(description = "Verify invalid api Key")
    public void apiKeyVerifyInvalidValue() {
        final Response response = apodHelper.getAPodResponse(Map.of(Utils.getApiKey(), "1234"), false);
        apodHelper.verifyIfApiKeyIsInvalid(response);
    }

}
