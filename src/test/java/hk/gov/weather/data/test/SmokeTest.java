package hk.gov.weather.data.test;

import hk.gov.weather.data.test.utiliti.HKOConstants;
import hk.gov.weather.data.test.utiliti.RunConfig;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class SmokeTest extends BaseAPICall {
    @Rule
    public ExpectedException exception = ExpectedException.none();
    private String verifiedChars;
    private RequestSpecification reqSpec;
    private ResponseSpecification resSpec;

    @BeforeClass
    public void setup() {
        reqSpec = RestUtilities.getRequestSpecification();
        reqSpec.queryParam("dataType", "rhrread");
        reqSpec.queryParam("lang", RunConfig.getLanguage());
        reqSpec.basePath(RunConfig.getRequestPathParameter());
        resSpec = RestUtilities.getResponseSpecification();
        resSpec.contentType("application/json; charset=utf-8");

        if (RunConfig.getLanguage().equalsIgnoreCase("en"))
            verifiedChars = "Central &amp; Western District";
        else if (RunConfig.getLanguage().equalsIgnoreCase("tc"))
            verifiedChars = "中西區";
        else
            verifiedChars = "中西区";
    }

    @Test(description = "to verify current weather report responseCode,ResponseTime,statusLine,contentType")
    @Severity(SeverityLevel.CRITICAL)
    public void TC1_validates_get_current_weather_report_response_basic() {
        given()
                .spec(reqSpec)
                .log().uri()
                .when()
                .get()
                .then()
                //.log().all() // print out response payload on console
                .spec(resSpec);
    }

    @Test(description = "to verify current weather report response headers")
    @Severity(SeverityLevel.CRITICAL)
    @Issue("inconsistent result for validation on Content-Length - Need to figure out")
    public void TC2_validates_get_current_weather_report_response_headers() {
        exception.expect(AssertionError.class);
        exception.expectMessage("response header validation is wrong");

        // including standard response headers validation only
        ResponseSpecBuilder responseHeaderBuilder = new ResponseSpecBuilder();
        responseHeaderBuilder.expectHeader("Content-Type", "application/json; charset=utf-8");
        responseHeaderBuilder.expectHeader("Server", "Apache");
        responseHeaderBuilder.expectHeader("Referrer-Policy", "no-referrer-when-downgrade");
        responseHeaderBuilder.expectHeader("Access-Control-Allow-Origin", "*");
        responseHeaderBuilder.expectHeader("X-Frame-Options", "SAMEORIGIN");
        responseHeaderBuilder.expectHeader("Keep-Alive", "timeout=10, max=100");
        responseHeaderBuilder.expectHeader("Connection", "Keep-Alive");
        ResponseSpecification responseHeaderSpec;
        responseHeaderSpec = responseHeaderBuilder.build();
        String dateFormatPattern = "EEE, dd MMM yyyy HH";
        String requestDateStamp = RunConfig.getCurrentRequestDate(dateFormatPattern, true);
        System.out.println(requestDateStamp);
        given()
                .spec(reqSpec)
                // .log().method()
                .log().uri()
                .when()
                .get()
                .then()
                .spec(responseHeaderSpec)
                // .assertThat().header("Content-Length", Integer::parseInt, lessThan(3000))
                .and().assertThat().header("Date", String::toString, containsString(requestDateStamp));
    }

    @Test(description = "to verify current weather report based given json schema")
    @Description("this case expect mandatory parameters showing in response")
    @Severity(SeverityLevel.CRITICAL)
    public void TC3_validates_get_current_weather_report_schema() {
        exception.expect(AssertionError.class);
        exception.expectMessage("schema validation is wrong");
        given()
                .spec(reqSpec)
                .log().uri() // print out current request URI
                .expect().contentType(ContentType.JSON)
                .and().statusCode(200)
                .when()
                .get()
                .then()
                .assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("current-weather-report-schema-drf7.json"));
    }

    @Test(description = "to verify current weather report language")
    @Description("This case expect language as per given")
    @Severity(SeverityLevel.CRITICAL)
    public void TC4_validates_get_current_weather_report_language() {
        given()
                .spec(reqSpec)
                .log().uri()
                .when()
                .get()
                .then()
                .assertThat()
                //.log().body()
                .spec(resSpec)
                .body("rainfall.data[0].place", equalTo(verifiedChars)
                );
    }

    @Test(description = "to verify response when language not given")
    @Description("this case expect current weather report with english language returned")
    @Severity(SeverityLevel.NORMAL)
    public void TC5_validates_get_current_weather_language_default() {
        verifiedChars = "Hong Kong Observatory";
        baseURI = HKOConstants.BASE_URI;
        basePath = RunConfig.getRequestPathParameter();
        given()
                .param("dataType", "rhrread")
                .param("lang", "")
                .log().uri()
                .when()
                .get()
                .then()
                //.log().all() // print out response payload on console
                .body("humidity.data[0].place", equalTo(verifiedChars))
                .spec(resSpec);
    }
}
