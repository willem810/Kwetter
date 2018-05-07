package interceptor;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@InterceptorBinding
@Target({TYPE, METHOD, CONSTRUCTOR})
@Retention(RUNTIME)
public @interface Logging {
}
