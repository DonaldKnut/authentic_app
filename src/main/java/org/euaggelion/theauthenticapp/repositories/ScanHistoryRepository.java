package org.euaggelion.theauthenticapp.repositories;


import org.euaggelion.theauthenticapp.models.ScanHistory;
import org.euaggelion.theauthenticapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScanHistoryRepository extends JpaRepository<ScanHistory, Long> {
    List<ScanHistory> findByUser(User user);
}