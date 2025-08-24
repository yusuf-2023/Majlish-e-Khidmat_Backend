package com.majlishekhidmat.dtoDonation;

// dto/DonationRequest.java

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DonationRequest {
  @NotBlank private String donorName;
  @NotNull @Positive private Double amount;
  @NotNull private Long accountId;        // which admin account
  private String method;                  // "UPI","CARD","NETBANKING","QR","UPI_INTENT"
}

