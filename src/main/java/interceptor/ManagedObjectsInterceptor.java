package interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Managed
@Interceptor
public class ManagedObjectsInterceptor {

    @PersistenceContext(unitName = "KwetterPU")
    EntityManager em = null;

    @AroundInvoke
    public Object authenticate(InvocationContext context) throws Exception {
        // First check if there's managed required on method level.
        Managed managed = context.getMethod().getAnnotation(Managed.class);

        // If there's no managed required on method level, check class level.
        if (managed == null) {
            managed = context.getTarget().getClass().getAnnotation(Managed.class);
        }

        // Else, there's no managed required, thus we chan continue;
        if (managed == null || (managed.value().length >0 && managed.value()[0].equals(void.class))) {
            System.out.println("MANAGED: Method: " + context.getMethod().getName() + ", no managed entities required");
            context.proceed();
        }

        if (em == null) {
            System.out.println("Could not find EntityManager! Proceeding...");
            return context.proceed();
        }

        boolean manageAll = managed.value().length <= 0;

        Object[] objects = context.getParameters();

        for (int i = 0; i < objects.length; i++) {
            Object obj = objects[i];
            boolean isManaged = manageAll;

            if (!manageAll) {
                for (Class c : managed.value()) {
                    if (c.equals(obj.getClass())) {
                        isManaged = true;
                        break;
                    }
                }
            }

            if (isManaged) objects[i] = manage(objects[i]);
        }

        context.setParameters(objects);

        return context.proceed();
    }

    private <T> T manage(T obj) {
        if (!em.contains(obj)) {
            obj = em.merge(obj);
        }

        return obj;
    }
}
