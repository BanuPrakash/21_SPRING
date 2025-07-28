package java14;

import java.lang.reflect.Method;

public class TelemetryInterceptor {
    public void intercept(Object target, Method method) throws Exception {
        long start = System.nanoTime();
        method.invoke(target); // reflection API
        long end = System.nanoTime();
        System.out.println("Execution time: " + (end - start)/1_000_000 + " ms");
    }
}
