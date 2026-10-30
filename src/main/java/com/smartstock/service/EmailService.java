
package com.smartstock.service;

import com.smartstock.model.LowStockAlertResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.List;

@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Value("${brevo.api.url}")
    private String brevoApiUrl;

    private final RestClient restClient;
    @Value("${brevo.sender.email}")
    private String senderEmail;

    public EmailService() {
        this.restClient = RestClient.create();
    }

    public void sendLowStockAlert(LowStockAlertResult alert) {

        String emailBody =
                "SMARTSTOCK AI - LOW STOCK ALERT\n\n" +
                        "Product: " + alert.getProductName() + "\n" +
                        "Product ID: " + alert.getProductId() + "\n\n" +
                        "Current Stock: " + alert.getCurrentStock() + "\n" +
                        "Reorder Point: " + alert.getReorderPoint() + "\n" +
                        "Required Stock: " + alert.getRequiredStock() + "\n" +
                        "Lead Time: " + alert.getLeadTimeDays() + " days\n\n" +
                        "Distributor: " + alert.getDistributorName() + "\n" +
                        "Distributor Email: " + alert.getDistributorEmail() + "\n\n" +
                        "Status: " + alert.getStatus() + "\n\n" +
                        "Please arrange replenishment for this product.\n\n" +
                        "Generated automatically by SMARTSTOCK AI.";

        Map<String, Object> requestBody = Map.of(
                "sender", Map.of(
                        "name", "SMARTSTOCK AI",
                        "email", senderEmail
                ),
                "to", List.of(
                        Map.of(
                                "email", "devadigaprithesh@gmail.com"
                        )
                ),
                "subject", "SMARTSTOCK AI - Low Stock Alert: "
                        + alert.getProductName(),
                "textContent", emailBody
        );

        restClient.post()
                .uri(brevoApiUrl)
                .header("api-key", brevoApiKey)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .toBodilessEntity();
    }
}
