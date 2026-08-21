package id.putra.core_erp.purchaserequisition.service;

public interface MailService {
    void sendSimpleMessage(String to, String subject, String text);
}

