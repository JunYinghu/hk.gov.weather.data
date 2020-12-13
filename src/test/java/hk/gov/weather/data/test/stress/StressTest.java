package hk.gov.weather.data.test.stress;

import hk.gov.weather.data.test.BaseAPICall;
import hk.gov.weather.data.test.utiliti.HKOConstants;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class StressTest extends BaseAPICall {
    static final int wait = 50 * 1000;

    static String url = null;
    int iterations = 30;

    @BeforeClass
    public void setUp() {
        url = HKOConstants.BASE_URI + "weather.php?dataType=rhrread&lang=tc";
    }

    @Test
    public void stressWithRestAssuredGet() {
        for (int i = 0, n = iterations; i < n; i++) {
            given().
                    log().uri().
                    expect().statusCode(200).
                    when().get(url);
        }
    }
}