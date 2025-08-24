package com.majlishekhidmat.serviceDonation;



import org.springframework.stereotype.Service;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class UpiQrService {
  public String buildUpiUri(String pa, String pn, double amount, String tn) {
    String base = "upi://pay";
    String q = "pa=" + enc(pa) +
               "&pn=" + enc(pn) +
               "&am=" + enc(String.format("%.2f", amount)) +
               "&cu=INR" +
               (tn != null ? "&tn=" + enc(tn) : "");
    return base + "?" + q;
  }
  private String enc(String s){ return URLEncoder.encode(s, StandardCharsets.UTF_8); }
}

