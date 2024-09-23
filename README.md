# bill-payment-service
Build a software solution that provides customers with bill payment service.
Input parameter when running app.
CASH_IN 1000000
CREATE_BILL 200000 25/10/2020 "EVN HCMC" ELECTRIC -- CREATE_BILL {amount} {due_date} {provider} {type}
CREATE_BILL 175000 30/10/2020 "SAVACO HCMC" WATER
CREATE_BILL 800000 30/11/2020 "VNPT" INTERNET
LIST_BILL
LIST_PAYMENT
PAY 2
SEARCH_BILL_BY_PROVIDER "EVN HCMC"
DELETE_BILL 1
LIST_PAYMENT
