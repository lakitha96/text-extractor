package com.example.file_extractor.service;

import com.example.file_extractor.model.StatementEntry;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TextExtractor {

    private static final Pattern P20 = Pattern.compile("^\\s*20:\\s*(?:Transaction Reference Number)?\\s*$");
    private static final Pattern P25 = Pattern.compile("^\\s*25:\\s*(?:Account Identification)?\\s*$");
    private static final Pattern P61 = Pattern.compile("^\\s*61:\\s*(?:Statement Line)?\\s*$");
    private static final Pattern MSG_TEXT = Pattern.compile("^-+\\s*Message Text\\s*-+$");

    private static final Pattern ENTRY_LINE = Pattern.compile(
            "^\\s*(\\d{6})\\s+\\d{4}\\s+([DC])\\s+\\S+\\s+([A-Z0-9/\\-]+)\\s+#([0-9]+(?:\\.[0-9]{2})?)#\\s+([A-Z]{1,2})\\s*$"
    );

    public List<StatementEntry> extractAll(Path file) throws IOException {
        List<StatementEntry> out = new ArrayList<>();

        String currentTRN = null;
        String currentAccount = null;
        boolean in61Block = false;

        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {

                // New message section
                if (MSG_TEXT.matcher(line.trim()).find()) {
                    in61Block = false;
                    currentTRN = null;  // force re-read
                    currentAccount = null;
                    continue;
                }

                if (P20.matcher(line).find()) {
                    currentTRN = readNextNonEmpty(br);
                    continue;
                }

                if (P25.matcher(line).find()) {
                    currentAccount = readNextNonEmpty(br);
                    continue;
                }

                if (P61.matcher(line).find()) {
                    in61Block = true;
                    continue;
                }

                if (in61Block) {
                    // Stop block on next header
                    if (looksLikeHeader(line)) {
                        in61Block = false;
                        continue;
                    }

                    Matcher m = ENTRY_LINE.matcher(line);
                    if (m.matches() && currentTRN != null && currentAccount != null) {
                        StatementEntry se = new StatementEntry();
                        se.setTransactionReferenceNumber(currentTRN.trim());
                        se.setAccountIdentification(currentAccount.trim());
                        se.setReference(m.group(3));
                        se.setAmount(new BigDecimal(m.group(4)).setScale(2));
                        se.setDcMark(m.group(5));
                        out.add(se);
                    }
                }
            }
        }
        return out;
    }

    private static boolean looksLikeHeader(String line) {
        String s = line.trim();
        return P20.matcher(s).find()
                || P25.matcher(s).find()
                || P61.matcher(s).find()
                || MSG_TEXT.matcher(s).find();
    }

    private static String readNextNonEmpty(BufferedReader br) throws IOException {
        String s;
        while ((s = br.readLine()) != null) {
            s = s.trim();
            if (!s.isEmpty()) return s;
        }
        return null;
    }
}
