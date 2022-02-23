package com.farmersInsurance.utilities.apodPicture;

import com.farmersInsurance.model.Apod;
import com.farmersInsurance.utilities.Utils;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.awaitility.Awaitility;
import org.testng.Assert;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;

public class ApodHelper {

    public Response getAPodResponse(final Map<String, ?> params) {
        return getAPodResponse(params, true);
    }

    public Response getAPodResponse(final Map<String, ?> params, final boolean withApiKey) {
        final String apiKey = withApiKey ? Utils.getApiKey() : StringUtils.EMPTY;
        final String apiKeyValue = withApiKey ? Utils.getApiKeyValue() : StringUtils.EMPTY;

        return given()
                .contentType(ContentType.JSON)
                .with().queryParam(apiKey, apiKeyValue)
                .with().queryParams(params)
                .when()
                .log().all()
                .get();
    }

    public Response getAPodResponse() {
        return getAPodResponse(false);
    }

    public Response getAPodResponse(final boolean withApiKey) {
        return withApiKey ? given().contentType(ContentType.JSON).with().queryParam(Utils.getApiKey(), Utils.getApiKeyValue()).when().log().all().get() :
                given().when().log().all().get();

    }

    public String getMsg(final Response response) {
        return response.then().extract().path("msg");
    }

    public String getErrorCodeMessage(final Response response) {
        return response.then().extract().path("error.code");
    }

    public String getDate(final Response response) {
        return response.then().extract().path("date");
    }

    public void verifyIfApiKeyIsMissing() {
        final Response response = getAPodResponse();
        Assert.assertEquals(getErrorCodeMessage(response), "API_KEY_MISSING");
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_FORBIDDEN);
    }

    public void verifyIfApiKeyIsInvalid(final Response response) {
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_FORBIDDEN);
        Assert.assertEquals(getErrorCodeMessage(response), "API_KEY_INVALID");
    }

    public void verifyStartDateInValidDateRange(final Response response){
        Awaitility.await().atMost(30, TimeUnit.SECONDS).pollInterval(5, TimeUnit.SECONDS).until(() -> response.getStatusCode() == HttpStatus.SC_OK);

        final List<Apod> apods = response.then().extract().as(new TypeRef<>() {});

        apods.forEach(apod -> {
            final LocalDate actualDate = LocalDate.parse(apod.getDate());
            final LocalDate expectedDate = ApodPictureDates.START.getDate().minusDays(1);
            final LocalDate today = ApodPictureDates.DEFAULT.getDate();
            Assert.assertTrue(actualDate.isAfter(expectedDate) &&
                            (actualDate.isBefore(ApodPictureDates.RANGE_END.getDate()) || actualDate.equals(today)),
                    String.format("%s date is before expected date %s", actualDate, expectedDate));
        });
    }

    public void verifyDateFormat() {
        final String invalidFormattedDate = "01-01-2020";
        final Response response = getAPodResponse(Map.of(ApodPictureQueryParams.DATE.getKey(), invalidFormattedDate));
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_BAD_REQUEST);

        final String errorMessage = getMsg(response);
        Assert.assertTrue(errorMessage.startsWith(ApodErrorMessages.DATE_FORMAT_MISMATCH.getText(invalidFormattedDate)));
    }
}
