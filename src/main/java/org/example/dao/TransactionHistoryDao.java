package org.example.dao;

import org.example.dto.TransactionHistoryDTO;
import org.example.enumeration.TransactionStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.Date;
import java.util.Comparator;
import java.util.stream.Collectors;

public class TransactionHistoryDao {
    private static final List<TransactionHistoryDTO> transactionHistoryDTOS = new ArrayList<>();

    public Integer getMaxId() {
        if (transactionHistoryDTOS.isEmpty()) {
            return 0;
        }
        return transactionHistoryDTOS.get(transactionHistoryDTOS.size() - 1).getInvoiceId();
    }

    public List<TransactionHistoryDTO> findAllGroupByInvoiceId() {
        Map<Integer, Optional<TransactionHistoryDTO>> mapTransaction = transactionHistoryDTOS.stream()
                .collect(Collectors
                        .groupingBy(TransactionHistoryDTO::getInvoiceId,
                                Collectors.maxBy(Comparator.comparing(TransactionHistoryDTO::getPaymentDate))
                        )
                );
        return mapTransaction.values().stream().flatMap(Optional::stream).toList();
    }

    public void insertTransactionHistories(List<TransactionHistoryDTO> transactionHistoryDTOs, TransactionStatus status) {
        for (TransactionHistoryDTO transactionHistoryDTO : transactionHistoryDTOs) {
            transactionHistoryDTO.setTransactionHistoryId(getMaxId() + 1);
            transactionHistoryDTO.setTransactionStatus(status);
            transactionHistoryDTO.setCreatedDate(new Date());
        }
        transactionHistoryDTOS.addAll(transactionHistoryDTOs);
    }
}
