package hk.gov.weather.data.test;

import hk.gov.weather.data.test.utiliti.HKOConstants;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

public class RestUtilities {

    private static String ENDPOINT;
    private static ResponseSpecBuilder RESPONSE_BUILDER;
    private static RequestSpecBuilder REQUEST_BUILDER;
    private static RequestSpecification REQUEST_SPEC;
    private static ResponseSpecification RESPONSE_SPEC;

    public static void setEndPoint(String endPoint) {
        ENDPOINT = endPoint;
    }

    public static ResponseSpecification getResponseSpecification() {
        RESPONSE_BUILDER = new ResponseSpecBuilder();

        RESPONSE_BUILDER.expectResponseTime(lessThan(3L), TimeUnit.SECONDS);
        RESPONSE_BUILDER.expectStatusLine("HTTP/1.1 200 OK");
        RESPONSE_BUILDER.expectStatusCode(200);
        RESPONSE_SPEC = RESPONSE_BUILDER.build();
        return RESPONSE_SPEC;
    }

    public static RequestSpecification getRequestSpecification() {
        REQUEST_BUILDER = new RequestSpecBuilder();
        REQUEST_BUILDER.setBaseUri(HKOConstants.BASE_URI);
        REQUEST_SPEC = REQUEST_BUILDER.build();

        return REQUEST_SPEC;
    }

    public static RequestSpecification createQueryParams(RequestSpecification rspc, Map<String, String> queryMap) {
        return rspc.queryParams(queryMap);
    }


    public static RequestSpecification createQueryParam(RequestSpecification rspc, String param, String value) {
        return rspc.queryParam(param, value);
    }

    public static RequestSpecification createPathParam(RequestSpecification rspc, String param, String value) {
        return rspc.pathParam(param, value);
    }

    public static RequestSpecification createPathParams(RequestSpecification rspc, Map<String, String> pathMap) {
        return rspc.pathParams(pathMap);
    }

    public static Response getResponse() {
        return given().get(ENDPOINT);
    }

    public static Response getResponse(RequestSpecification reqSpec, String type) {
        REQUEST_SPEC.spec(reqSpec);
        Response response = null;
        if (type.equalsIgnoreCase("get"))
            return response = given().spec(REQUEST_SPEC).log().uri().log().method().get(ENDPOINT);
        else if (type.equalsIgnoreCase("post"))
            return response = given().spec(REQUEST_SPEC).log().uri().log().method().post(ENDPOINT);
        else if (type.equalsIgnoreCase("delete"))
            return response = given().spec(REQUEST_SPEC).log().uri().log().method().delete(ENDPOINT);
        else if (type.equalsIgnoreCase("put"))
            return response = given().spec(REQUEST_SPEC).log().uri().log().method().put(ENDPOINT);
        else
            return response;
    }
}
