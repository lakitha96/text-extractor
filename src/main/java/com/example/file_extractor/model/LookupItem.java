package com.example.file_extractor.model;

import java.math.BigDecimal;

/**
 * @author lakithaprabudh
 */
public class LookupItem {
    private String reference;     // validated by regex
    private BigDecimal amount;    // validated (scale=2, >0)

    public LookupItem() {}
    public LookupItem(String reference, BigDecimal amount) {
        this.reference = reference;
        this.amount = amount;
    }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
