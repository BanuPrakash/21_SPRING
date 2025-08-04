package com.adobe.rentalapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
//    @NotBlank(message = "Email is required")
@NotBlank(message = "{email.required}")
    @Email(message = "{email.incorrect}")
    private String email;

    //UP41AE900
    @Pattern(regexp = "^[A-Z]{2}[0-9]{2}[A-Z]{0,2}[0-9]{4}$", message = "Registration Number ${validatedValue} is not valid!!!")
    private String registrationNumber;

    @FutureOrPresent(message = "Rented Date should be present or future date!!!")
    private Date rentedDate;
}
