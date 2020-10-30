package com.digipay.paymentservice.paymentservice.gateway;

import com.digipay.paymentservice.paymentservice.model.PaymentDetails;
import com.digipay.paymentservice.paymentservice.model.PaymentProcessorResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.math.BigDecimal;


@Service
public class BankServiceProvider2 implements BankServiceProvider {

    private static final String PAYMENT_URL = "http://localhost:9090/cards/pay";
    private RestTemplate restTemplate;

    @Override
    public PaymentProcessorResponse transfer(
            final PaymentDetails paymentDetails) {

        return restTemplate.postForObject(PAYMENT_URL,
                                          new PaymentDetailsBankProvider2(paymentDetails),
                                          PaymentProcessorResponse.class);
    }

    public RestTemplate getRestTemplate() {

        return restTemplate;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {

        this.restTemplate = restTemplate;
    }
}

/**
 * added Just for 2 property Name (expire, pin2) that differ.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class PaymentDetailsBankProvider2 implements Serializable {

    String source;
    String dest;
    Integer ccv2;
    String expire;
    Integer pin2;
    BigDecimal amount;

    PaymentDetailsBankProvider2(PaymentDetails paymentDetails) {

        this.source = paymentDetails.getSource();
        this.dest   = paymentDetails.getDest();
        this.ccv2   = paymentDetails.getCcv2();
        this.expire = paymentDetails.getExpDate();
        this.pin2   = paymentDetails.getPin();
        this.amount = paymentDetails.getAmount();
    }
}
