package org.example.service;

import org.example.dao.AccountDao;
import org.example.exception.BusinessException;

public class AccountService {

    private final AccountDao accountDao;

    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public Long getBalance() {
        return accountDao.findBalance();
    }

    private void validateAddBalance(Long balance) {
        if (balance <= 0) {
            throw new BusinessException("Please enter amount greater than zero!");
        }
    }

    private void validateSubtractBalance(Long balance) {
        validateAddBalance(balance);
        if (balance > accountDao.findBalance()) {
            throw new BusinessException("Please enter amount less than or equals available balance!");
        }
    }

    public Long addBalance(Long balance) {
        validateAddBalance(balance);
        return accountDao.editBalance(balance);
    }

    public boolean subtractBalance(Long balance) {
        validateSubtractBalance(balance);
        return accountDao.editBalance(-balance) >= 0;
    }
}
