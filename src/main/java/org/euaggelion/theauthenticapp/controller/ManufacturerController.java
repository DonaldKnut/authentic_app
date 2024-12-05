package org.euaggelion.theauthenticapp.controller;


import org.euaggelion.theauthenticapp.dtos.CompanyRegistrationDTO;
import org.euaggelion.theauthenticapp.dtos.ManufacturerProfileDTO;
import org.euaggelion.theauthenticapp.dtos.ProductRegistrationDTO;
import org.euaggelion.theauthenticapp.models.User;
import org.euaggelion.theauthenticapp.services.ManufacturerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manufacturer")
public class ManufacturerController {

    private final ManufacturerService manufacturerService;

    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @PreAuthorize("hasRole('MANUFACTURER')")
    @PostMapping("/register-company")
    public ResponseEntity<String> registerCompany(@RequestBody CompanyRegistrationDTO companyRegistrationDTO) {
        String result = manufacturerService.registerCompany(companyRegistrationDTO);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('MANUFACTURER')")
    @PostMapping("/register-product")
    public ResponseEntity<String> registerProduct(@RequestBody ProductRegistrationDTO productRegistrationDTO) {
        String result = manufacturerService.registerProduct(productRegistrationDTO.getProductName(),
                productRegistrationDTO.getIsbn(), productRegistrationDTO.getQrCodeData());
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('MANUFACTURER')")
    @PostMapping("/complete-profile")
    public ResponseEntity<String> completeManufacturerProfile(@RequestBody ManufacturerProfileDTO manufacturerProfileDTO,
                                                              @AuthenticationPrincipal User currentUser) {
        String result = manufacturerService.completeManufacturerProfile(currentUser, manufacturerProfileDTO);
        return ResponseEntity.ok(result);
    }
}
