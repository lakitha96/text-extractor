package com.example.file_extractor;

import com.example.file_extractor.model.AppProperties;
import com.example.file_extractor.model.LookupItem;
import com.example.file_extractor.model.StatementEntry;
import com.example.file_extractor.service.LookupLoader;
import com.example.file_extractor.service.OutputWriter;
import com.example.file_extractor.service.TextExtractor;
import com.example.file_extractor.service.ValidationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@SpringBootApplication
public class FileExtractorApplication implements CommandLineRunner {

	@Autowired
	AppProperties props;
	@Autowired
	ValidationService validator;
	@Autowired
	LookupLoader loader;
	@Autowired
	TextExtractor extractor;
	@Autowired
	OutputWriter output;
	@Autowired
	ObjectMapper om;

	public static void main(String[] args) {
		SpringApplication.run(FileExtractorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Ensure required folders exist
		Files.createDirectories(Path.of(props.getLookupDir()));
		Files.createDirectories(Path.of(props.getOutputDir()));

		// Load and validate lookup
		var lc = loader.load(); // includes regex validation

		// Validate input file presence
		Path input = Path.of(props.getInputFile());
		if (!Files.exists(input)) {
			throw new IllegalStateException("Input file not found: " + input.toAbsolutePath());
		}

		// Extract all entries from statement
		List<StatementEntry> all = extractor.extractAll(input);

		// Match
		List<StatementEntry> matches = new ArrayList<>();
		for (LookupItem wanted : lc.items) {
			for (StatementEntry se : all) {
				if (
						lc.trn.equals(se.getTransactionReferenceNumber()) &&
								lc.account.equals(se.getAccountIdentification()) &&
								wanted.getReference().equalsIgnoreCase(se.getReference()) &&
								wanted.getAmount().compareTo(se.getAmount()) == 0
				) {
					matches.add(se);
				}
			}
		}

		// Output
		output.write(lc.trn, lc.account, matches);

		System.out.println("Matching complete. Found " + matches.size() + " entries.");
		System.out.println("Results written to folder: " + props.getOutputDir());
	}
}
