package org.euaggelion.theauthenticapp.repositories;

import org.euaggelion.theauthenticapp.models.Manufacturer;
import org.euaggelion.theauthenticapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {

    // This is the new method to find a Manufacturer by User
    Optional<Manufacturer> findByUser(User user);
}
