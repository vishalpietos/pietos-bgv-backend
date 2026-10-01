package com.pietos.bgv.service;

public interface WhatsAppService {

    String sendTextMessage(String phoneNumber, String message);

    String sendTemplateMessage(String phoneNumber,
                               String customerName,
                               String orderId,
                               String orderDate);
}