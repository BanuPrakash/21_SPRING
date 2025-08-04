package com.adobe.rentalapp.async;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NotificationServiceHandler {
    @EventListener
    @Async
    public void sendNotification(PatientDischargeEvent event) {
        System.out.println(Thread.currentThread() + " : " + " notify patient " + event.getPatientId());
    }
}
