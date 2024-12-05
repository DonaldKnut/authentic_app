package org.euaggelion.theauthenticapp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "manufacturers")
public class Manufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;  // Link to the User

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;  // A manufacturer works for a company
}
