package com.example.file_extractor.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author lakithaprabudh
 *
 * transactionReferenceNumber: "XY2112211379-01"
 * accountIdentification: "21245870000"
 */

@Service
public class ValidationService {
    // Examples:
    // TRN: "XY2112211379-01" → 2 letters + 11 digits + "-" + 2 digits
    private static final Pattern TRN = Pattern.compile("^[A-Z]{2}\\d{11}-\\d{2}$");

    // Account: you gave 11 digits → tighten to exactly 11; relax to {8,16} if needed
    private static final Pattern ACCOUNT = Pattern.compile("^\\d{11}$");

    // Reference: uppercase letters, digits, / and - (your samples)
    private static final Pattern REFERENCE = Pattern.compile("^[A-Z0-9][A-Z0-9/\\-]*$");

    // Amount: 50 or 50.00 etc, exactly 2 decimals if present
    private static final Pattern AMOUNT = Pattern.compile("^\\d+(\\.\\d{2})?$");

    public void validateTrn(String trn) {
        if (trn == null || !TRN.matcher(trn).matches()) {
            throw new IllegalArgumentException("Invalid Transaction Reference Number format");
        }
    }

    public void validateAccount(String account) {
        if (account == null || !ACCOUNT.matcher(account).matches()) {
            throw new IllegalArgumentException("Invalid Account Identification format");
        }
    }

    public void validateReference(String ref) {
        if (ref == null || !REFERENCE.matcher(ref).matches()) {
            throw new IllegalArgumentException("Invalid Reference format: " + ref);
        }
    }

    public void validateAmount(String amtStr) {
        if (amtStr == null || !AMOUNT.matcher(amtStr).matches()) {
            throw new IllegalArgumentException("Invalid Amount format: " + amtStr);
        }
    }

    public void validateAmount(BigDecimal amt) {
        if (amt == null || amt.scale() > 2 || amt.signum() <= 0) {
            throw new IllegalArgumentException("Invalid Amount value: " + amt);
        }
    }
}
