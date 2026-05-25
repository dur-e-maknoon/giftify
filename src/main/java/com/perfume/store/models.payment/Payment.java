package com.perfume.store.models.payment;

import com.perfume.store.models.Order;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String paymentMethod; // "CASH", "CARD", "ONLINE"

    @Column(nullable = false)
    private String status; // "PENDING", "COMPLETED", "FAILED", "REFUNDED"

    private String transactionId;

    /** EasyPaisa, JazzCash, etc. — used when paymentMethod is ONLINE */
    private String onlineProvider;

    /** Card or mobile-wallet account number */
    private String accountNumber;

    @CreationTimestamp
    private LocalDateTime paidAt;

    // --- Constructors ---

    public Payment() {}

    public Payment(Order order, BigDecimal amount, String paymentMethod) {
        this.order = order;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = "PENDING";
    }

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getOnlineProvider() { return onlineProvider; }
    public void setOnlineProvider(String onlineProvider) { this.onlineProvider = onlineProvider; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public LocalDateTime getPaidAt() { return paidAt; }

    public String getDisplayLabel() {
        if ("CASH".equals(paymentMethod)) {
            return "Cash on Delivery";
        }
        if ("CARD".equals(paymentMethod)) {
            return "Credit / Debit Card";
        }
        if ("ONLINE".equals(paymentMethod) && onlineProvider != null) {
            return formatProvider(onlineProvider);
        }
        return paymentMethod;
    }

    private static String formatProvider(String provider) {
        return switch (provider) {
            case "EASYPAISA" -> "EasyPaisa";
            case "JAZZCASH" -> "JazzCash";
            case "SADAPAY" -> "SadaPay";
            case "NAYAPAY" -> "NayaPay";
            default -> provider;
        };
    }

    public String getMaskedAccountNumber() {
        if (accountNumber == null || accountNumber.length() < 4) {
            return accountNumber;
        }
        int len = accountNumber.length();
        return "****" + accountNumber.substring(len - 4);
    }
}
