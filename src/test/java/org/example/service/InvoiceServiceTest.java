package org.example.service;

import org.example.dao.InvoiceDao;
import org.example.dto.InvoiceDTO;
import org.example.enumeration.State;
import org.example.enumeration.TypeBill;
import org.example.exception.BusinessException;
import org.example.utils.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class InvoiceServiceTest {

    @Mock
    private AccountService accountService;

    @Mock
    private TransactionHistoryService transactionHistoryService;

    @Mock
    private InvoiceDao invoiceDao;

    @InjectMocks
    private InvoiceService invoiceService;

    private List<InvoiceDTO> invoiceDTOS;

    @Before
    public void prepareDateBeforeTest() {
        this.invoiceDTOS = buildInvoices();
    }

    @Test
    public void testUpdateStatesSuccess() {
        Mockito.when(invoiceDao.findByIds(Mockito.anyList())).thenReturn(this.invoiceDTOS);
        Mockito.when(accountService.getBalance()).thenReturn(600_000L);
        Mockito.when(invoiceDao.updateStates(Mockito.anyList())).thenReturn(true);
        Mockito.when(accountService.subtractBalance(Mockito.anyLong())).thenReturn(true);
        Mockito.doNothing().when(transactionHistoryService).tracking(Mockito.anyList(), Mockito.any());
        invoiceService.updateStates(List.of(2, 3));
    }

    @Test
    public void testUpdateStatesTotalPayGreaterThanAvailableBalance() {
        Mockito.when(invoiceDao.findByIds(Mockito.anyList())).thenReturn(this.invoiceDTOS);
        Mockito.when(accountService.getBalance()).thenReturn(500_000L);
        Exception exception = Assert.assertThrows("Sorry! Not enough fund to proceed with payment!",
                BusinessException.class,
                () -> invoiceService.updateStates(List.of(2, 3))
        );
        Assert.assertEquals("Sorry! Not enough fund to proceed with payment!", exception.getMessage());
    }

    @Test
    public void testUpdateStatesWithSubtractBalanceFailure() {
        Mockito.when(invoiceDao.findByIds(Mockito.anyList())).thenReturn(this.invoiceDTOS);
        Mockito.when(accountService.getBalance()).thenReturn(600_000L);
        Mockito.when(invoiceDao.updateStates(Mockito.anyList())).thenReturn(true);
        Mockito.when(accountService.subtractBalance(Mockito.anyLong())).thenReturn(false);
        Exception exception = Assert.assertThrows("Sorry! Payment failed!",
                BusinessException.class,
                () -> invoiceService.updateStates(List.of(2, 3))
        );
        Assert.assertEquals("Sorry! Payment failed!", exception.getMessage());
    }

    @Test
    public void testUpdateStatesFailure() {
        Mockito.when(invoiceDao.findByIds(Mockito.anyList())).thenReturn(this.invoiceDTOS);
        Mockito.when(accountService.getBalance()).thenReturn(600_000L);
        Mockito.when(invoiceDao.updateStates(Mockito.anyList())).thenReturn(false);
        Exception exception = Assert.assertThrows("Sorry! Payment failed!",
                BusinessException.class,
                () -> invoiceService.updateStates(List.of(2, 3))
        );
        Assert.assertEquals("Sorry! Payment failed!", exception.getMessage());
    }

    @Test
    public void testEditInvoiceWasPaid() {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setState(State.PAID);

        Exception exception = Assert.assertThrows("The bill was processed. You can't update!",
                BusinessException.class,
                () -> invoiceService.editInvoice(invoiceDTO)
        );
        Assert.assertEquals("The bill was processed. You can't update!", exception.getMessage());
    }

    @Test
    public void testEditInvoiceFailed() {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setInvoiceId(1);
        Mockito.when(invoiceDao.editInvoice(Mockito.any())).thenReturn(0);
        Exception exception = Assert.assertThrows("Sorry! Not found a bill with such id " + invoiceDTO.getInvoiceId(),
                BusinessException.class,
                () -> invoiceService.editInvoice(invoiceDTO)
        );
        Assert.assertEquals("Sorry! Not found a bill with such id " + invoiceDTO.getInvoiceId(), exception.getMessage());
    }

    @Test
    public void testEditInvoiceSuccess() {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setState(State.NOT_PAID);
        Mockito.when(invoiceDao.editInvoice(Mockito.any())).thenReturn(Mockito.anyInt());
        Mockito.lenient().doNothing().when(transactionHistoryService).tracking(Mockito.anyList(), Mockito.any());
        invoiceService.editInvoice(invoiceDTO);
    }

    @Test
    public void testDeleteInvoiceSuccess() {
        Mockito.when(invoiceDao.deleteInvoice(Mockito.any())).thenReturn(true);
        Mockito.when(invoiceDao.findById(1)).thenReturn(new InvoiceDTO());
        Mockito.lenient().doNothing().when(transactionHistoryService).tracking(Mockito.anyList(), Mockito.any());
        invoiceService.deleteInvoice(1);
    }

    @Test
    public void testDeleteInvoiceFailed() {
        Integer invoiceId = 1;
        Mockito.when(invoiceDao.findById(invoiceId)).thenReturn(null);
        Exception exception = Assert.assertThrows("Sorry! Not found a bill with such id " + invoiceId,
                BusinessException.class,
                () -> invoiceService.deleteInvoice(invoiceId)
        );
        Assert.assertEquals("Sorry! Not found a bill with such id 1", exception.getMessage());
    }

    @Test
    public void testDeleteInvoiceWasPaid() {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setState(State.PAID);
        Mockito.when(invoiceDao.findById(1)).thenReturn(invoiceDTO);
    }

    private List<InvoiceDTO> buildInvoices() {
        InvoiceDTO invoiceDTO1 = new InvoiceDTO(200_000L, DateUtils.parse("25/10/2020"), "EVN HCMC", TypeBill.ELECTRIC);
        InvoiceDTO invoiceDTO2 = new InvoiceDTO(175_000L, DateUtils.parse("30/10/2020 "), "SAVACO HCMC", TypeBill.WATER);
        InvoiceDTO invoiceDTO3 = new InvoiceDTO(200_000L, DateUtils.parse("30/11/2020"), "VNPT", TypeBill.INTERNET);
        return List.of(invoiceDTO1, invoiceDTO2, invoiceDTO3);
    }
}
