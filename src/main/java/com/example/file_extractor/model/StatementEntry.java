package com.example.file_extractor.model;

import java.math.BigDecimal;

/**
 * @author lakithaprabudh
 */
public class StatementEntry {
    private String transactionReferenceNumber;
    private String accountIdentification;
    private String reference;
    private BigDecimal amount;
    private String dcMark;

    public String getTransactionReferenceNumber() { return transactionReferenceNumber; }
    public void setTransactionReferenceNumber(String transactionReferenceNumber) { this.transactionReferenceNumber = transactionReferenceNumber; }
    public String getAccountIdentification() { return accountIdentification; }
    public void setAccountIdentification(String accountIdentification) { this.accountIdentification = accountIdentification; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getDcMark() { return dcMark; }
    public void setDcMark(String dcMark) { this.dcMark = dcMark; }
}
