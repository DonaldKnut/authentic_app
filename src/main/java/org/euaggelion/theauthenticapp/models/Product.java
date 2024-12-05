package org.euaggelion.theauthenticapp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;  // Product name

    // The company that manufactures the product
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    // Optional: User who bought or registered the product
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    // Optional: ISBN for products with ISBN
    @Column(unique = true, nullable = true)
    private String isbn;

    // QR Code associated with the product
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "qr_code_id", referencedColumnName = "id")
    private QRCode qrCode;

    // Whether the product is authentic
    @Column(nullable = false)
    private boolean isAuthentic = false;  // Default to false until verified

    // Whether the product has been verified
    @Column(nullable = false, updatable = false)
    private boolean isVerified = false; // Default to false

    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

}
