package org.example.service;

import org.example.constant.StringPool;
import org.example.dao.InvoiceDao;
import org.example.dto.InvoiceDTO;
import org.example.dto.TransactionHistoryDTO;
import org.example.enumeration.State;
import org.example.enumeration.TransactionStatus;
import org.example.exception.BusinessException;
import org.example.filter.InvoiceFilter;

import java.util.Date;
import java.util.List;

public class InvoiceService {

    private final AccountService accountService;

    private final InvoiceDao invoiceDao;

    private final TransactionHistoryService transactionHistoryService;

    public InvoiceService(AccountService accountService, InvoiceDao invoiceDao, TransactionHistoryService transactionHistoryService) {
        this.accountService = accountService;
        this.invoiceDao = invoiceDao;
        this.transactionHistoryService = transactionHistoryService;
    }

    public void printAll() {
        System.out.println(StringPool.HEADER_INVOICE);
        invoiceDao.findAll().forEach(InvoiceDTO::print);
    }

    public void printListBy(InvoiceFilter filter) {
        System.out.println(StringPool.HEADER_INVOICE);
        invoiceDao.findBy(filter).forEach(InvoiceDTO::print);
    }

    public void updateStates(List<Integer> invoiceIds) {
        List<InvoiceDTO> invoices = invoiceDao.findByIds(invoiceIds);
        Long totalAmount = invoices.stream().map(InvoiceDTO::getAmount).reduce(0L, Long::sum);
        Long availableBalance = accountService.getBalance();
        if (totalAmount > availableBalance) {
            throw new BusinessException("Sorry! Not enough fund to proceed with payment!");
        }
        boolean isSuccess = invoiceDao.updateStates(invoiceIds) && accountService.subtractBalance(totalAmount);
        if (isSuccess) {
            transactionHistoryService.tracking(buildTransactionHistoryDTOs(invoices), TransactionStatus.PROCESSED);
            System.out.println("Payment has been completed for Bill with id " + invoiceIds.toString().replace("[", "").replace("]", ""));
            System.out.println("You current balance is: " + (availableBalance - totalAmount));
        } else {
            throw new BusinessException("Sorry! Payment failed!");
        }
    }

    public Integer editInvoice(InvoiceDTO invoiceDTO) {
        if (State.PAID.equals(invoiceDTO.getState())) {
            throw new BusinessException("The bill was processed. You can't update!");
        }
        Integer invoiceId = invoiceDao.editInvoice(invoiceDTO);
        if (invoiceId == 0) {
            throw new BusinessException("Sorry! Not found a bill with such id " + invoiceDTO.getInvoiceId());
        }
        invoiceDTO.setInvoiceId(invoiceId);
        transactionHistoryService.tracking(buildTransactionHistoryDTO(invoiceDTO), TransactionStatus.PENDING);
        return invoiceId;
    }

    public void deleteInvoice(Integer invoiceId) {
        InvoiceDTO invoiceDTO = invoiceDao.findById(invoiceId);
        if (invoiceDTO == null) {
            throw new BusinessException("Sorry! Not found a bill with such id " + invoiceId);
        }
        if (State.PAID.equals(invoiceDTO.getState())) {
            throw new BusinessException("The bill was processed. You can't delete!");
        }
        invoiceDao.deleteInvoice(invoiceDTO);
        System.out.println("Delete success for Bill with id " + invoiceId);
        transactionHistoryService.tracking(buildTransactionHistoryDTO(invoiceDTO), TransactionStatus.CANCELLED);
    }

    private List<TransactionHistoryDTO> buildTransactionHistoryDTOs(List<InvoiceDTO> invoices) {
        return invoices.stream().map(this::buildTransactionHistoryDTO).toList();
    }

    private TransactionHistoryDTO buildTransactionHistoryDTO(InvoiceDTO invoiceDTO) {
        TransactionHistoryDTO transactionHistoryDTO = new TransactionHistoryDTO();
        transactionHistoryDTO.setAmount(invoiceDTO.getAmount());
        transactionHistoryDTO.setInvoiceId(invoiceDTO.getInvoiceId());
        transactionHistoryDTO.setPaymentDate(new Date());
        return transactionHistoryDTO;
    }
}
