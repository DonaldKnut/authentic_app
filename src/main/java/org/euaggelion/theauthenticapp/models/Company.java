package org.euaggelion.theauthenticapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String companyName;  // Company name

    @Column(nullable = false, unique = true)
    private String companyEmail;

    @OneToMany(mappedBy = "company")
    @ToString.Exclude  // A company can produce many products
    private Set<Product> products;

    @OneToMany(mappedBy = "company")
    @ToString.Exclude
    private Set<Manufacturer> manufacturers;  // A company can have multiple manufacturers
}
