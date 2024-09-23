package org.example;

import org.example.constant.StringPool;
import org.example.dao.AccountDao;
import org.example.dao.InvoiceDao;
import org.example.dao.TransactionHistoryDao;
import org.example.dto.InvoiceDTO;
import org.example.enumeration.CmdCommand;
import org.example.enumeration.State;
import org.example.enumeration.TypeBill;
import org.example.exception.BusinessException;
import org.example.filter.InvoiceFilter;
import org.example.service.AccountService;
import org.example.service.InvoiceService;
import org.example.service.TransactionHistoryService;
import org.example.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static List<String> getParams(String userInput) {
        List<String> list = new ArrayList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(userInput);
        while (m.find()) {
            list.add(m.group(1).replace("\"", ""));
        }
        return list;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String userInput;
        AccountDao accountDao = new AccountDao();
        InvoiceDao invoiceDao = new InvoiceDao();
        TransactionHistoryDao transactionHistoryDao = new TransactionHistoryDao();
        AccountService accountService = new AccountService(accountDao);
        TransactionHistoryService transactionHistoryService = new TransactionHistoryService(transactionHistoryDao);
        InvoiceService invoiceService = new InvoiceService(accountService, invoiceDao, transactionHistoryService);
        InvoiceFilter filter;
        System.out.print(System.getProperty("user.dir") + StringPool.SPACE);
        while (!"exit".equalsIgnoreCase(userInput = scanner.nextLine())) {
            List<String> params = getParams(userInput);
            CmdCommand command;
            try {
                command = CmdCommand.valueOf(params.get(0));
                switch (command) {
                    case CASH_IN:
                        System.out.println("Your available balance: " + accountService.addBalance(Long.valueOf(params.get(1))));
                        break;
                    case CREATE_BILL:
                        InvoiceDTO invoiceDTO = new InvoiceDTO(
                                Long.valueOf(params.get(1)),
                                DateUtils.parse(params.get(2)),
                                params.get(3),
                                TypeBill.valueOf(params.get(4))
                        );
                        invoiceDTO.setInvoiceId(0);
                        System.out.println("Create bill success with bill id = " + invoiceService.editInvoice(invoiceDTO));
                        break;
                    case DELETE_BILL:
                        invoiceService.deleteInvoice(Integer.valueOf(params.get(1)));
                        break;
                    case LIST_BILL:
                        invoiceService.printAll();
                        break;
                    case PAY:
                        List<Integer> invoiceIds = params.subList(1, params.size()).stream().map(Integer::valueOf).toList();
                        invoiceService.updateStates(invoiceIds);
                        break;
                    case DUE_DATE:
                        filter = new InvoiceFilter();
                        filter.setState(State.NOT_PAID);
                        invoiceService.printListBy(filter);
                        break;
                    case LIST_PAYMENT:
                        transactionHistoryService.printAll();
                        break;
                    case SCHEDULE:
                        System.out.println("Coming soon!");
                        break;
                    case SEARCH_BILL_BY_PROVIDER:
                        filter = new InvoiceFilter();
                        filter.setProvider(params.get(1));
                        invoiceService.printListBy(filter);
                        break;
                    default:
                        break;
                }
            } catch (BusinessException exception) {
                System.out.println(exception.getMessage());
            } catch (IllegalArgumentException | IndexOutOfBoundsException exception) {
                System.out.println("Wrong format!");
            } catch (Exception exception) {
                System.out.println("Service error!");
            }
            System.out.print(System.getProperty("user.dir") + StringPool.SPACE);
        }

        System.out.println("Good bye!");
        scanner.close();
        System.exit(0);
    }
}