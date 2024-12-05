package org.euaggelion.theauthenticapp.controller;


import org.euaggelion.theauthenticapp.models.Product;
import org.euaggelion.theauthenticapp.models.ScanHistory;
import org.euaggelion.theauthenticapp.models.User;
import org.euaggelion.theauthenticapp.repositories.ProductRepository;
import org.euaggelion.theauthenticapp.repositories.UserRepository;
import org.euaggelion.theauthenticapp.services.ScanHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scan-history")
public class ScanHistoryController {

    @Autowired
    private ScanHistoryService scanHistoryService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    // Endpoint to get all scan history for a user
    @GetMapping("/{userId}")
    public ResponseEntity<List<ScanHistory>> getScanHistory(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID")); // Ensure user exists

        List<ScanHistory> history = scanHistoryService.getUserScanHistory(user);
        return ResponseEntity.ok(history);
    }

    // Endpoint to log a new scanned item for a user
    @PostMapping("/save")
    public ResponseEntity<Void> saveScanHistory(@RequestParam Long userId, @RequestParam Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

        scanHistoryService.logScan(user, product);
        return ResponseEntity.ok().build();
    }

    // Optionally, you can add endpoints for deleting or updating scan history
}
