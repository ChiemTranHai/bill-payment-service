package org.example.enumeration;

public enum CmdCommand {
    CASH_IN, // CASH_IN 10000
    /**
     * CREATE_BILL 2000 22/09/2024 VNPT INTERNET
     * Please refer input order follow constructor {@link org.example.dto.InvoiceDTO}
     **/
    CREATE_BILL,
    DELETE_BILL,
    LIST_BILL,
    PAY,
    DUE_DATE,
    SCHEDULE,
    LIST_PAYMENT,
    SEARCH_BILL_BY_PROVIDER;

    public static boolean contains(String text) {
        for (CmdCommand cmd : CmdCommand.values()) {
            if (cmd.name().equalsIgnoreCase(text)) {
                return true;
            }
        }
        return false;
    }
}
