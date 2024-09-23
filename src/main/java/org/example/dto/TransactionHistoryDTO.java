package org.example.dto;

import org.example.enumeration.TransactionStatus;
import org.example.utils.DateUtils;

import java.util.Date;

public class TransactionHistoryDTO {
    private Integer transactionHistoryId;
    private Long amount;
    private Date paymentDate;
    private Integer invoiceId;
    private TransactionStatus transactionStatus;
    private Date createdDate;

    public Integer getTransactionHistoryId() {
        return transactionHistoryId;
    }

    public void setTransactionHistoryId(Integer transactionHistoryId) {
        this.transactionHistoryId = transactionHistoryId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void print() {
        System.out.println(String.valueOf(transactionHistoryId) +
                '\t' +
                amount +
                '\t' +
                DateUtils.format(paymentDate) +
                '\t' +
                transactionStatus +
                '\t' +
                invoiceId);
    }
}
