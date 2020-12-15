package hk.gov.weather.data.test;

import hk.gov.weather.data.test.utiliti.RunConfig;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;

import static hk.gov.weather.data.test.RestUtilities.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DestructiveTest extends BaseAPICall {

    private final HashMap<String, String> validQueryParams = new HashMap<>();

    private RequestSpecification reqSpec;
    private ResponseSpecification resSpec;
    private String reqBasePath;
    private String reqLang;
    private String verifiedMessageInvalidAPIRequest;

    @BeforeMethod
    public void setup() {
        reqSpec = RestUtilities.getRequestSpecification();
        reqSpec.basePath("");
        validQueryParams.put("dataType", "rhrread");
        validQueryParams.put("lang", reqLang);
        reqBasePath = RunConfig.getRequestPathParameter();
        reqLang = RunConfig.getLanguage();
        resSpec = RestUtilities.getResponseSpecification();

        resSpec.contentType("text/html; charset=utf-8");
        verifiedMessageInvalidAPIRequest = "Please include valid parameters in API request. For details, please refer to";
    }


    @Test(description = "to verify return error message as per type parameter dataType")
    @Description("this case expect error message saying need include valid parameter in API request")
    @Severity(SeverityLevel.NORMAL)
    public void TC1_validates_response_query_parameter_dataType_typo() {
        HashMap<String, String> invalidQueryParams = new HashMap<>();
        invalidQueryParams.put("datatype", "rhrread");
        invalidQueryParams.put("lang", reqLang);
        String exceptionMessage = "Expectation failed. expected message (request using datatype rather dataType): \n"
                + verifiedMessageInvalidAPIRequest + "\n";
        Response response =
                given()
                        .spec(RestUtilities.createQueryParams(reqSpec, invalidQueryParams))
                        .log().uri()
                        .when()
                        .get(reqBasePath)
                        .then()
                        //.log().all()
                        .spec(resSpec)
                        .extract().response();
        Assert.assertTrue(response.asString().startsWith(verifiedMessageInvalidAPIRequest), exceptionMessage);
    }

    @Test(description = "to verify post method not allowed")
    @Description("this case expect post method not allowed, but it is allowed")
    @Issue("Post Method should not be allowed")
    @Severity(SeverityLevel.NORMAL)
    public void TC2_validates_not_allowed_request_method() {
        setEndPoint(reqBasePath);
        RestUtilities.createQueryParams(reqSpec, validQueryParams);
        String exceptionMessage = "Expectation failed: give method should not allowed. expected status code 405.";
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(getResponse(reqSpec, "put").statusCode() == 405, "put: " + exceptionMessage);
        softAssert.assertTrue(getResponse(reqSpec, "delete").statusCode() == 405, "delete: " + exceptionMessage);
        softAssert.assertTrue(getResponse(reqSpec, "post").statusCode() == 405, "post: " + exceptionMessage);
        softAssert.assertAll();
    }

    @Test(description = "to verify return error message as per invalid path parameter")
    @Description("this case expect error message saying page not found")
    @Severity(SeverityLevel.NORMAL)
    public void TC3_validates_message_page_not_found_invalid_path_parameter() {
        String exceptionMessage = "Expectation failed. expected message (request using invalid path weather): \n" +
                "對不起，我們找不到你要的網頁" + "\n";
        Response response =
                given()
                        .spec(RestUtilities.createQueryParams(reqSpec, validQueryParams))
                        .log().uri()
                        .when()
                        .get("weather")
                        .then()
                        //.log().body()
                        .extract().response();
        Assert.assertTrue(response.asString().contains("對不起，我們找不到你要的網頁"), exceptionMessage);

    }

    @Test(description = "to verify return error message as per incorrect query parameter(dataType) value")
    @Description("this case expect error message saying invalid parameter in API request")
    @Severity(SeverityLevel.NORMAL)
    public void TC4_validates_message_incorrect_query_parameter_dataType_value() {
        String exceptionMessage = "Expectation failed. expected message (request with incorrect dataType value rhread): \n"
                + verifiedMessageInvalidAPIRequest + "\n";
        HashMap<String, String> invalidQueryParams = new HashMap<>();
        invalidQueryParams.put("dataType", "rhread");
        invalidQueryParams.put("lang", reqLang);

        Response response =
                given()
                        .spec(createQueryParams(reqSpec, invalidQueryParams))
                        .log().uri()
                        .when()
                        .get(reqBasePath)
                        .then()
                        //.log().all()
                        .extract().response();
        Assert.assertTrue(response.asString().contains(verifiedMessageInvalidAPIRequest), exceptionMessage);
    }

    @Test(description = "to verify return error message as per missing query parameter")
    @Description("this case expect error message saying invalid parameters in API request")
    @Severity(SeverityLevel.NORMAL)
    public void TC5_validates_message_without_query_parameter() {
        String exceptionMessage = "Expectation failed. expected message (request without query parameters): \n"
                + verifiedMessageInvalidAPIRequest + "\n";
        Response response =
                given()
                        .spec(reqSpec)
                        .log().uri()
                        .when()
                        .get(reqBasePath)
                        .then()
                        .extract().response();
        Assert.assertTrue(response.asString().contains(verifiedMessageInvalidAPIRequest), exceptionMessage);

    }

    @Test(description = "to verify return error message as per invalid query parameter lang")
    @Description("this case return correct response in english")
    @Severity(SeverityLevel.TRIVIAL)
    @Issue("Expected Error for Invalid Query Parameter (lang), Actual:Using default language (en),Equivalent to not pass lang parameter")
    public void TC6_validates_response_query_parameter_lang_typo() {
        HashMap<String, String> invalidQueryParams = new HashMap<>();
        invalidQueryParams.put("dataType", "rhrread");
        invalidQueryParams.put("Lang", reqLang);
        reqSpec.basePath(reqBasePath);
        resSpec.contentType("application/json; charset=utf-8");
        String verifiedChars = "Hong Kong Observatory";
        resSpec.expect().body("humidity.data[0].place", equalTo(verifiedChars));
        given()
                .spec(RestUtilities.createQueryParams(reqSpec, invalidQueryParams))
                .log().uri()
                .when()
                .get()
                .then()
                //.log().all()
                .spec(resSpec);
    }

    @Test(description = "to verify return error message as per unsupported language")
    @Description("this case return message saying include valid parameters in API request.")
    @Severity(SeverityLevel.TRIVIAL)
    public void TC7_validates_message_unsupported_language() {
        String exceptionMessage = "Expectation failed. expected message (request with unsupported language): \n"
                + verifiedMessageInvalidAPIRequest + "\n";
        HashMap<String, String> invalidQueryParams = new HashMap<>();
        invalidQueryParams.put("dataType", "rhrread");
        invalidQueryParams.put("lang", "jp");
        reqSpec.basePath(reqBasePath);
        Response response =
                given()
                        .spec(RestUtilities.createQueryParams(reqSpec, invalidQueryParams))
                        .log().uri()
                        .when()
                        .get()
                        .then()
                        .extract().response();
        Assert.assertTrue(response.asString().contains(verifiedMessageInvalidAPIRequest), exceptionMessage);
    }

    @Test(description = "to verify return error message as per path parameter upperCase")
    @Description("this case return message saying file not found.")
    @Severity(SeverityLevel.TRIVIAL)
    @Issue("Expect file not found error - Actual: It returns either file not found or page not found")
    public void TC8_validates_message_file_not_found_path_parameter_case_sensitive() {
        String exceptionMessage = "Expectation failed. expected message (request with incorrect path): \n"
                + "File not found." + "\n";
        Response response =
                given()
                        .spec(RestUtilities.createQueryParams(reqSpec, validQueryParams))
                        .header("Accept", "*/*")
                        .log().uri()
                        .when()
                        .get("Weather.php")
                        .then()
                        .extract().response();
        Assert.assertTrue(response.asString().contains("File not found."), exceptionMessage);
    }

}