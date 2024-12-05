package org.euaggelion.theauthenticapp.repositories;


import org.euaggelion.theauthenticapp.models.Company;
import org.euaggelion.theauthenticapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByCompanyEmail(String companyEmail);

    // Find a company by the manufacturer (User)
    Optional<Company> findByManufacturers_User(User user);
    // Use Optional for null-safety

}
