package org.example.dao;

import org.example.dto.AccountDTO;

public class AccountDao {
    private static final AccountDTO accountDTO = new AccountDTO(1, 0L);

    public Long findBalance() {
        return accountDTO.getBalance();
    }

    public Long editBalance(Long balance) {
        Long totalBalance = accountDTO.getBalance() + balance;
        accountDTO.setBalance(totalBalance);
        return totalBalance;
    }
}
