package org.euaggelion.theauthenticapp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
@Table(name = "qr_codes")
public class QRCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @OneToOne(mappedBy = "qrCode")
    private Product product;

    @Column(nullable = false)
    private boolean isAuthentic;

    // Getters and Setters
}
