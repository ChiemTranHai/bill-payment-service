package org.example.service;

import org.example.dao.AccountDao;
import org.example.exception.BusinessException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @Mock
    private AccountDao accountDao;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void testAddNegativeBalance() {
        Exception exception = Assert.assertThrows("Please enter amount greater than zero!",
                BusinessException.class,
                () -> accountService.addBalance(-1L)
        );
        Assert.assertEquals("Please enter amount greater than zero!", exception.getMessage());
    }

    @Test
    public void testAddZeroBalance() {
        Exception exception = Assert.assertThrows("Please enter amount greater than zero!",
                BusinessException.class,
                () -> accountService.addBalance(0L)
        );
        Assert.assertEquals("Please enter amount greater than zero!", exception.getMessage());
    }

    @Test
    public void testAddBalanceSuccess() {
        Mockito.when(accountDao.editBalance(1000L)).thenReturn(1000L);
        Assert.assertEquals(Optional.of(1000L).get(), accountService.addBalance(1000L));
    }

    @Test
    public void testSubtractBalanceGreaterThanAvailableBalance() {
        Mockito.when(accountDao.findBalance()).thenReturn(1000L);
        Exception exception = Assert.assertThrows("Please enter amount less than or equals available balance!",
                BusinessException.class,
                () -> accountService.subtractBalance(2000L)
        );
        Assert.assertEquals("Please enter amount less than or equals available balance!", exception.getMessage());

    }

    @Test
    public void testSubtractBalanceSuccess() {
        Mockito.when(accountDao.findBalance()).thenReturn(3000L);
        Mockito.when(accountDao.editBalance(-1000L)).thenReturn(2000L);
        Assert.assertTrue(accountService.subtractBalance(1000L));
    }
}
