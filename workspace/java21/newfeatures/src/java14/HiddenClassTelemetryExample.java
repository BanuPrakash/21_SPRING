package java14;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class HiddenClassTelemetryExample {

    public HiddenClassTelemetryExample() throws IOException, IllegalAccessException {
    }

    // Simulated target class to monitor
    public static class BusinessService {
        public void processTransaction() {
            System.out.println("Processing transaction...");
            try {
                Thread.sleep(300); // Simulate delay
            } catch (InterruptedException ignored) {}
        }
    }

    public static void main(String[] args) throws Throwable {
    // Step 1: Your business class
        BusinessService service = new BusinessService();

        // Step 2: Dynamically create telemetry interceptor class bytecode
        Path path = Paths.get("/Users/banuprakash/IdeaProjects/Adobe/21_SPRING/workspace/java21/newfeatures/out/production/newfeatures/java14/TelemetryInterceptor.class");
        byte[] classFileBytes = Files.readAllBytes(path);

        String BASE_64 = Base64.getEncoder().encodeToString(classFileBytes);
        byte[] telemetryClassBytes = Base64.getDecoder().decode(BASE_64);

        // Step 3: Define hidden class
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        var hidden = lookup.defineHiddenClass(telemetryClassBytes, true,
             MethodHandles.Lookup.ClassOption.NESTMATE);

        Class<?> interceptorClass = hidden.lookupClass();
//        Class.forName("a");
        // Step 4: Instantiate and call
        Object interceptor = interceptorClass.getConstructor().newInstance();
        Method method = interceptorClass.getMethod("intercept", Object.class, Method.class);

        // Step 5: Intercept a method call
        Method targetMethod = BusinessService.class.getMethod("processTransaction");

        method.invoke(interceptor, service, targetMethod);
    }

}

