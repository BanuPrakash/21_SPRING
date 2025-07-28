package client;

import com.example.api.LogService;

import java.util.ServiceLoader;

public class Main {
    public static void main(String[] args) {
        ServiceLoader<LogService> loader = ServiceLoader.load(LogService.class);
        for(LogService service : loader) {
            service.log("Hello World!!!");
        }
    }
}
