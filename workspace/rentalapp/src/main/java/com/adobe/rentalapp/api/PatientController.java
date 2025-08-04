package com.adobe.rentalapp.api;

import com.adobe.rentalapp.async.PatientDischargeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/patient")
@RequiredArgsConstructor
public class PatientController {
    private final ApplicationEventPublisher publisher;
    // GET http://localhost:8080/api/patient/discharge?id=P123S
    @GetMapping("/discharge")
    public String dichargePatient(@RequestParam("id") String id) {
        publisher.publishEvent(new PatientDischargeEvent(this, id));
        return "Patient Discharged!!!";
    }
}
