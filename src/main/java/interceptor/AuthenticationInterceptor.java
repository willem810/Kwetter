package interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.Arrays;

@Interceptor
@Authentication
public class AuthenticationInterceptor {

    @AroundInvoke
    public Object authenticate(InvocationContext context) throws Exception {
        // First check if there's a permissions required on method level.
        Authentication auth = context.getMethod().getAnnotation(Authentication.class);

        // If there's no authentication required on method level, check class level.
        if (auth == null) {
            auth = context.getTarget().getClass().getAnnotation(Authentication.class);
        }

        // Else, there's no permission required, thus we chan continue;
        if (auth == null) {
            System.out.println("AUTHENTICATION: Method: " + context.getMethod().getName() + ", no permission required");
            context.proceed();
        }

        // Since we don't have any session details, we can't retrieve the user's permissions
        // for now we just print the permissions, to show in works.
        String perms = Arrays.toString(auth.value());
        System.out.println("AUTHENTICATION: Method: " + context.getMethod().getName() + ", Permissions: " + perms);

        //
        //TODO: check users permissions and return 401 if user is not authenticated.
        //

        return context.proceed();
    }
}
