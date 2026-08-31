package id.putra.core_erp.purchase_requisition.service;

public interface MailService {
    void sendSimpleMessage(String to, String subject, String text);
}
