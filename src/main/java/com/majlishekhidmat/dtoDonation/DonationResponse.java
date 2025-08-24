package com.majlishekhidmat.dtoDonation;

// dto/DonationResponse.java

import lombok.*;
@Data @AllArgsConstructor
public class DonationResponse {
  private Long donationId;
  private String status;
  private String message;
}

