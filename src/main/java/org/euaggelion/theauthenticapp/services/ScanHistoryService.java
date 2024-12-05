package org.euaggelion.theauthenticapp.services;

import org.euaggelion.theauthenticapp.models.Product;
import org.euaggelion.theauthenticapp.models.ScanHistory;
import org.euaggelion.theauthenticapp.models.User;
import org.euaggelion.theauthenticapp.repositories.ScanHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScanHistoryService {

    private final ScanHistoryRepository scanHistoryRepository;

    public ScanHistoryService(ScanHistoryRepository scanHistoryRepository) {
        this.scanHistoryRepository = scanHistoryRepository;
    }

    public void logScan(User user, Product product) {
        ScanHistory scanHistory = new ScanHistory();
        scanHistory.setUser(user);
        scanHistory.setProduct(product);
        scanHistory.setScanTime(LocalDateTime.now());

        scanHistoryRepository.save(scanHistory);
    }

    public List<ScanHistory> getUserScanHistory(User user) {
        return scanHistoryRepository.findByUser(user);
    }
}
