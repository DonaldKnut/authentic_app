package org.euaggelion.theauthenticapp.dtos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.euaggelion.theauthenticapp.models.User;

@Entity
@Getter
@Setter
public class ManufacturerInfo {

    // Getters and Setters
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    private String registrationNumber;
    private String address;
    private String certificationDocuments;
    private boolean isVerified;

    @OneToOne
    private User user;

}

