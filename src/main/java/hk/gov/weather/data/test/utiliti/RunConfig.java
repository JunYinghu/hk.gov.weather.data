package hk.gov.weather.data.test.utiliti;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class RunConfig {

    private static String lang;
    private static String requestPathParameter;

    public static String getLanguage() {
        return lang;
    }

    public static void setLanguage(String language) {
        lang = language;
    }

    public static String getRequestPathParameter() {
        return requestPathParameter;
    }

    public static void setRequestPathParameter(String pathParameter) {
        requestPathParameter = pathParameter + ".php";
    }

    public static String getCurrentRequestDate(String DateFormatPattern, boolean timeZone) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormatPattern);
        if (timeZone)
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(date);
    }


}
