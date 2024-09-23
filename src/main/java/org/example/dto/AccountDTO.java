package org.example.dto;

public class AccountDTO {

    private Integer accountId;
    private Long balance;

    public AccountDTO() {
    }

    public AccountDTO(Long balance) {
        this.balance = balance;
    }

    public AccountDTO(Integer accountId, Long balance) {
        this.accountId = accountId;
        this.balance = balance;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }
}
