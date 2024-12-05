package org.euaggelion.theauthenticapp.repositories;


import org.euaggelion.theauthenticapp.dtos.ManufacturerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManufacturerInfoRepository extends JpaRepository<ManufacturerInfo, Long> {
}
