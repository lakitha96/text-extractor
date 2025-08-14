package com.example.file_extractor.service;

import com.example.file_extractor.model.AppProperties;
import com.example.file_extractor.model.StatementEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class OutputWriter {

    @Autowired
    private AppProperties props;

    /**
     * Writes ONLY the requested text format:
     *
     * Transaction Reference Number: <TRN>
     *
     * Account Identification: <Account>
     *
     * Reference1 : <Ref>
     * Amount: <Amount>
     *
     * Reference1 : <Ref>
     * Amount: <Amount>
     *
     * (…repeat for each match; "Reference1 :" is not incremented)
     */
    public void write(String trn, String account, List<StatementEntry> matches) throws IOException {
        Path outDir = Path.of(props.getOutputDir());
        Files.createDirectories(outDir);

        String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        Path txt = outDir.resolve("matches-" + safe(trn) + "-" + safe(account) + "-" + stamp + ".txt");

        StringBuilder sb = new StringBuilder();
        sb.append("Transaction Reference Number: ").append(trn).append("\n\n");
        sb.append("Account Identification: ").append(account).append("\n\n");

        for (StatementEntry m : matches) {
            sb.append("Reference1 : ").append(m.getReference()).append("\n");
            sb.append("Amount: ").append(m.getAmount().toPlainString()).append("\n\n");
        }

        Files.writeString(txt, sb.toString());
    }

    private static String safe(String s) {
        return s == null ? "NULL" : s.replaceAll("[^A-Za-z0-9_\\-]", "_");
    }
}
