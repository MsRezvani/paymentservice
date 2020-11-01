package com.digipay.paymentservice.paymentservice.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public interface INotificationService {

    public void notify(String dest, BigDecimal amount, Date date);

    public void notifyFallback(String dest, BigDecimal amount, Date date);
}
