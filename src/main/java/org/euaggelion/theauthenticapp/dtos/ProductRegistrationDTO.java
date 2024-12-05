package org.euaggelion.theauthenticapp.dtos;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRegistrationDTO {
    private String productName;
    private String isbn;
    private String qrCodeData;
}
