package hk.gov.weather.data.test;

import hk.gov.weather.data.test.utiliti.RunConfig;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseAPICall {

    @BeforeClass
    @Parameters({"language", "pathParameter"})
    public void setLanguageBeforeTesting(@Optional("tc") String language, @Optional("weather") String pathParameter) {
        RunConfig.setLanguage(language);
        RunConfig.setRequestPathParameter(pathParameter);
    }
}
