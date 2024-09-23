package org.example.filter;

import org.example.enumeration.State;
import org.example.enumeration.TypeBill;

public class InvoiceFilter {
    private String provider;
    private TypeBill type;
    private String dueDate;
    private State state;

    public InvoiceFilter() {
    }

    public InvoiceFilter(String provider, TypeBill type, String dueDate) {
        this.provider = provider;
        this.type = type;
        this.dueDate = dueDate;
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

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
