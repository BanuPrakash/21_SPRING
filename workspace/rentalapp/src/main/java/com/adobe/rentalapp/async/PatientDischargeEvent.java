package com.adobe.rentalapp.async;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class PatientDischargeEvent extends ApplicationEvent  {
    private String patientId;
    public PatientDischargeEvent(Object source, String id) {
        super(source);
        this.patientId = id;
    }
}
