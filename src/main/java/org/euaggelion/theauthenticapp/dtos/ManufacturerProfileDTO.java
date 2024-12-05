package org.euaggelion.theauthenticapp.dtos;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class ManufacturerProfileDTO {
    // Getters and Setters
    private String companyName;
    private String registrationNumber;
    private String address;
    private String certificationDocuments;  // Could be a URL to uploaded documents or a file

}
