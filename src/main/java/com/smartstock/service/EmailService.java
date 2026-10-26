package com.smartstock.service;

import com.smartstock.model.LowStockAlertResult;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendLowStockAlert(LowStockAlertResult alert) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo("devadigaprithesh@gmail.com");

        message.setSubject(
                "SMARTSTOCK AI - Low Stock Alert: "
                        + alert.getProductName()
        );

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

        message.setText(emailBody);

        mailSender.send(message);
    }
}