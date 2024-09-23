package org.example.dto;

import org.example.enumeration.State;
import org.example.enumeration.TypeBill;
import org.example.utils.DateUtils;

import java.util.Date;

public class InvoiceDTO {

    private Integer invoiceId;
    private Long amount;
    private Date dueDate;
    private State state;
    private String provider;
    private TypeBill type;
    private Date createdDate;
    private Date lastUpdatedDate;
    private boolean isDeleted;

    public InvoiceDTO() {
    }

    public InvoiceDTO(Long amount, Date dueDate, String provider, TypeBill type) {
        this.amount = amount;
        this.dueDate = dueDate;
        this.provider = provider;
        this.type = type;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public TypeBill getType() {
        return type;
    }

    public void setType(TypeBill type) {
        this.type = type;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void print() {
        System.out.println(String.valueOf(invoiceId) +
                '\t' +
                type +
                '\t' +
                amount +
                '\t' +
                DateUtils.format(dueDate) +
                '\t' +
                state +
                '\t' +
                provider);
    }
}
