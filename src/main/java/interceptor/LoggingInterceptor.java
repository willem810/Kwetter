package interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.Arrays;

@Logging
@Interceptor
public class LoggingInterceptor {
    @AroundInvoke
    public Object log(InvocationContext context) throws Exception {
        String name = context.getMethod().getName();
        String params = Arrays.toString(context.getParameters());
        System.out.println("LOGGING: name = " +name + " params = "+ params);

        Object resp = context.proceed();

        System.out.println("LOGGING: Response = " + resp);

        return resp;
    }
}
