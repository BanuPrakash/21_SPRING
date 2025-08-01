package com.adobe.rentalapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RentalDTO {
    private String email;
    private String registrationNumber;
    private Date rentedDate;
}
