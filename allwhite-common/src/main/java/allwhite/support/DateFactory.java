package allwhite.support;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.TimeZone;

@Component
public class DateFactory {

    public static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone("UTC");

    public Date now() {
        return new Date();
    }

    public TimeZone timeZone() {
        return DEFAULT_TIME_ZONE;
    }
}
