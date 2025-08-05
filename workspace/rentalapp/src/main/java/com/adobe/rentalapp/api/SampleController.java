package com.adobe.rentalapp.api;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class SampleController {
    private final Counter counter;
    private final Timer timer;
    public SampleController(MeterRegistry meterRegistry) {
        counter = Counter.builder("sample.counter")
                .description("Hits to SampleController")
                .register(meterRegistry);

        timer = Timer.builder("hello.timer")
                .description("Times Say Hello World!!").register(meterRegistry);
    }

    @GetMapping()
    public String sayHello() throws Exception {
        counter.increment();
       return timer.recordCallable(() -> serviceMethod());
    }

    public String serviceMethod() throws Exception {
        Thread.sleep((long)(Math.random() * 2000));
        return "Hello World!!!";
    }
}
