package org.example.dao;

import org.example.dto.InvoiceDTO;
import org.example.enumeration.State;
import org.example.filter.InvoiceFilter;
import org.example.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

public class InvoiceDao {

    private static final List<InvoiceDTO> invoiceDTOS = new ArrayList<>();

    public Integer getMaxId() {
        if (invoiceDTOS.isEmpty()) {
            return 0;
        }
        return invoiceDTOS.get(invoiceDTOS.size() - 1).getInvoiceId();
    }

    public List<InvoiceDTO> findAll() {
        return invoiceDTOS.stream()
                .filter(predicateNotDelete())
                .toList();
    }

    public InvoiceDTO findById(Integer id) {
        for (InvoiceDTO invoiceDTO : invoiceDTOS) {
            if (invoiceDTO.getInvoiceId().equals(id) && !invoiceDTO.isDeleted()) {
                return invoiceDTO;
            }
        }
        return null;
    }

    public boolean existsById(Integer id) {
        Predicate<InvoiceDTO> predicate = invoiceDTO -> invoiceDTO.getInvoiceId().equals(id);
        predicate = predicate.and(predicateNotDelete());
        return invoiceDTOS.stream().anyMatch(predicate);
    }

    public List<InvoiceDTO> findByIds(List<Integer> ids) {
        return invoiceDTOS.stream()
                .filter(invoiceDTO -> ids.contains(invoiceDTO.getInvoiceId()))
                .filter(predicateNotDelete())
                .toList();
    }

    public List<InvoiceDTO> findBy(InvoiceFilter invoiceFilter) {
        Predicate<InvoiceDTO> predicate = predicateNotDelete();
        if (invoiceFilter.getProvider() != null) {
            predicate = predicate.and(invoiceDTO -> invoiceFilter.getProvider().equalsIgnoreCase(invoiceDTO.getProvider()));
        }
        if (invoiceFilter.getType() != null) {
            predicate = predicate.and(invoiceDTO -> invoiceFilter.getType().equals(invoiceDTO.getType()));
        }
        if (invoiceFilter.getDueDate() != null) {
            predicate = predicate.and(invoiceDTO -> invoiceDTO.getDueDate().equals(DateUtils.parse(invoiceFilter.getDueDate())));
        }
        if (invoiceFilter.getState() != null) {
            predicate = predicate.and(invoiceDTO -> invoiceDTO.getState().equals(invoiceFilter.getState()));
        }
        return invoiceDTOS.stream().filter(predicate).toList();
    }

    public Integer editInvoice(InvoiceDTO invoiceDTO) {
        if (invoiceDTO.getInvoiceId() == 0) {
            invoiceDTO.setInvoiceId(getMaxId() + 1);
            invoiceDTO.setCreatedDate(new Date());
            invoiceDTO.setLastUpdatedDate(new Date());
            invoiceDTO.setState(State.NOT_PAID);
            invoiceDTOS.add(invoiceDTO);
        } else {
            InvoiceDTO oldInvoice = findById(invoiceDTO.getInvoiceId());
            if (oldInvoice != null) {
                oldInvoice.setLastUpdatedDate(new Date());
                invoiceDTOS.set(invoiceDTOS.indexOf(oldInvoice), invoiceDTO);
            } else {
                return 0;
            }
        }
        return invoiceDTO.getInvoiceId();
    }

    public boolean deleteInvoice(InvoiceDTO invoiceDTO) {
        invoiceDTO.setDeleted(true);
        invoiceDTOS.set(invoiceDTOS.indexOf(invoiceDTO), invoiceDTO);
        return true;
    }

    public boolean updateStates(List<Integer> invoiceIds) {
        for (InvoiceDTO invoiceDTO : invoiceDTOS) {
            if (invoiceIds.contains(invoiceDTO.getInvoiceId())) {
                invoiceDTO.setState(State.PAID);
            }
        }
        return true;
    }

    private Predicate<InvoiceDTO> predicateNotDelete() {
        return invoiceDTO -> !invoiceDTO.isDeleted();
    }
}
