package org.euaggelion.theauthenticapp.services;

import org.euaggelion.theauthenticapp.dtos.CompanyRegistrationDTO;
import org.euaggelion.theauthenticapp.dtos.ManufacturerInfo;
import org.euaggelion.theauthenticapp.dtos.ManufacturerProfileDTO;
import org.euaggelion.theauthenticapp.models.*;
import org.euaggelion.theauthenticapp.repositories.*;
import org.euaggelion.theauthenticapp.role.Role;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.euaggelion.theauthenticapp.repositories.ManufacturerInfoRepository;

import java.util.Optional;

@Service
public class ManufacturerService {

    private final CompanyRepository companyRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private final ManufacturerInfoRepository manufacturerInfoRepository;

    public ManufacturerService(CompanyRepository companyRepository, ManufacturerRepository manufacturerRepository,
                               ProductRepository productRepository, UserRepository userRepository, ManufacturerInfoRepository manufacturerInfoRepository) {
        this.companyRepository = companyRepository;
        this.manufacturerRepository = manufacturerRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.manufacturerInfoRepository = manufacturerInfoRepository;
    }

    public String registerCompany(CompanyRegistrationDTO companyRegistrationDTO) {
        // Get the authenticated user from the security context
        String username = getAuthenticatedUsername();

        // Fetch the user from the database based on the username
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return "User not found.";
        }

        User user = userOptional.get();

        // Check if the user is a manufacturer
        if (user.getRole() != Role.MANUFACTURER) {
            return "Only manufacturers can register a company.";
        }

        // Extract company name and email from the DTO
        String companyName = companyRegistrationDTO.getCompanyName();
        String companyEmail = companyRegistrationDTO.getCompanyEmail();

        // Validate company email
        if (companyEmail.endsWith("gmail.com")) {
            return "Gmail is not allowed for registration. Please use a company-specific email.";
        }

        // Check if the company email is already in use
        if (companyRepository.existsByCompanyEmail(companyEmail)) {
            return "Company email already in use.";
        }

        // Create and link the company to the manufacturer (user)
        Company company = new Company();
        company.setCompanyName(companyName);
        company.setCompanyEmail(companyEmail);

        Manufacturer manufacturer = manufacturerRepository.findByUser(user)
                .orElseGet(() -> {
                    Manufacturer newManufacturer = new Manufacturer();
                    newManufacturer.setUser(user);
                    return manufacturerRepository.save(newManufacturer);
                });

        manufacturer.setCompany(company);  // Associate the manufacturer with the company
        companyRepository.save(company);

        return "Company registered successfully.";
    }

    public String registerProduct(String productName, String isbn, String qrCodeData) {
        String username = getAuthenticatedUsername();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return "User not found.";
        }

        User user = userOptional.get();
        if (user.getRole() != Role.MANUFACTURER) {
            return "Only manufacturers can register products.";
        }

        Optional<Manufacturer> manufacturerOptional = manufacturerRepository.findByUser(user);
        if (manufacturerOptional.isEmpty()) {
            return "User is not a registered manufacturer.";
        }

        Manufacturer manufacturer = manufacturerOptional.get();
        Optional<Company> companyOptional = companyRepository.findById(manufacturer.getCompany().getId());
        if (companyOptional.isEmpty()) {
            return "Manufacturer has no registered company.";
        }


        Product product = new Product();
        product.setName(productName);
        product.setIsbn(isbn);
        product.setManufacturer(manufacturer);

        QRCode qrCode = new QRCode();
        qrCode.setCode(qrCodeData);
        product.setQrCode(qrCode);

        productRepository.save(product);
        return "Product registered successfully.";
    }


    private String getAuthenticatedUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public String completeManufacturerProfile(User currentUser, ManufacturerProfileDTO manufacturerProfileDTO) {
        // Create a new ManufacturerInfo entity to store the details
        ManufacturerInfo manufacturerInfo = new ManufacturerInfo();
        manufacturerInfo.setCompanyName(manufacturerProfileDTO.getCompanyName());
        manufacturerInfo.setRegistrationNumber(manufacturerProfileDTO.getRegistrationNumber());
        manufacturerInfo.setAddress(manufacturerProfileDTO.getAddress());
        manufacturerInfo.setCertificationDocuments(manufacturerProfileDTO.getCertificationDocuments());
        manufacturerInfo.setVerified(false);  // Not verified by default

        // Associate with the current user
        manufacturerInfo.setUser(currentUser);
        manufacturerInfoRepository.save(manufacturerInfo);

        // Perform verification (could be manual or automatic)
        if (isValidRegistration(manufacturerProfileDTO)) {
            // Update the current user's status to "Trusted Manufacturer"
            currentUser.setStatus("Trusted Manufacturer");
            userRepository.save(currentUser);
            manufacturerInfo.setVerified(true);
            manufacturerInfoRepository.save(manufacturerInfo);
            return "Profile verified and user certified as Trusted Manufacturer";
        } else {
            return "Profile submitted but verification failed. Please check your details.";
        }
    }

    // Example verification logic (simple for now)
    private boolean isValidRegistration(ManufacturerProfileDTO manufacturerProfileDTO) {
        // Example check: registration number must not be empty
        return manufacturerProfileDTO.getRegistrationNumber() != null && !manufacturerProfileDTO.getRegistrationNumber().isEmpty();
    }
    public boolean verifyUser(String token) {
        // Find user by verification token
        Optional<User> userOptional = userRepository.findByVerificationToken(token);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEmailVerified(true);  // Mark user as verified
            user.setVerificationToken(null);  // Clear the token after successful verification
            userRepository.save(user);
            return true;
        }

        return false;  // Token not found or invalid
    }

}
