package com.farmersInsurance.utilities.apodPicture;

import com.farmersInsurance.utilities.Utils;
import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApodHelper {

    public Response getAPodResponse(final Map<String, ?> params) {
        return getAPodResponse(params, true);
    }

    public Response getAPodResponse(final Map<String, ?> params, final boolean withApiKey) {
        final String apiKey = withApiKey ? Utils.getApiKey() :StringUtils.EMPTY;
        final String apiKeyValue = withApiKey ? Utils.getApiKeyValue() :StringUtils.EMPTY;

       return given()
                .with().queryParam(apiKey, apiKeyValue)
                .with().queryParams(params)
                .when()
                .log().all()
                .get();
    }

    public Response getAPodResponse() {
      return getAPodResponse(false);
    }

    public Response getAPodResponse(final boolean withApiKey){
        return withApiKey ? given().with().queryParam(Utils.getApiKey(), Utils.getApiKeyValue()).when().log().all().get() :
                            given().when().log().all().get();

    }

    public String extractFromApodResponse (final Response response, final String path){
        return response.then().extract().path(path).toString();
    }

}
