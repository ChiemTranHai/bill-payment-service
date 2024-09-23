package org.example.service;

import org.example.constant.StringPool;
import org.example.dao.TransactionHistoryDao;
import org.example.dto.TransactionHistoryDTO;
import org.example.enumeration.TransactionStatus;

import java.util.List;

public class TransactionHistoryService {
    private final TransactionHistoryDao transactionHistoryDao;

    public TransactionHistoryService(TransactionHistoryDao transactionHistoryDao) {
        this.transactionHistoryDao = transactionHistoryDao;
    }

    public void tracking(TransactionHistoryDTO transactionHistoryDTO, TransactionStatus status) {
        transactionHistoryDao.insertTransactionHistories(List.of(transactionHistoryDTO), status);
    }

    public void tracking(List<TransactionHistoryDTO> transactionHistoryDTO, TransactionStatus status) {
        transactionHistoryDao.insertTransactionHistories(transactionHistoryDTO, status);
    }

    public void printAll() {
        System.out.println(StringPool.HEADER_TRANSACTION_HISTORY);
        transactionHistoryDao.findAllGroupByInvoiceId().forEach(TransactionHistoryDTO::print);
    }
}
