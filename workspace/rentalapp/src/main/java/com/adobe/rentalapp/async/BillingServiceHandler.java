package com.adobe.rentalapp.async;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class BillingServiceHandler {

    @EventListener
    @Async
    public void processBill(PatientDischargeEvent event) {
        System.out.println(Thread.currentThread() + " : " + " bill process " + event.getPatientId());
    }
}
