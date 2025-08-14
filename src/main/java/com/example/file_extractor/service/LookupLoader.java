package com.example.file_extractor.service;

import com.example.file_extractor.model.AppProperties;
import com.example.file_extractor.model.LookupItem;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * @author lakithaprabudh
 */
@Service
public class LookupLoader {
    private final AppProperties props;
    private final ValidationService validator;

    public record Criteria(String transactionReferenceNumber,
                           String accountIdentification,
                           List<Map<String, String>> lines) {}

    public static class LoadedCriteria {
        public String trn;
        public String account;
        public List<LookupItem> items;
    }

    public LookupLoader(AppProperties props, ValidationService validator) {
        this.props = props;
        this.validator = validator;
    }

    public LoadedCriteria load() {
        LoadedCriteria out = new LoadedCriteria();
        out.trn = "XY2112211379-01";
        out.account = "21245870000";
        out.items = List.of(
                new LookupItem("ABC-SW-20683/21", new BigDecimal("50.00")),
                new LookupItem("ABC/TT/21/50235", new BigDecimal("25.00"))
        );
        return out;
    }
}
