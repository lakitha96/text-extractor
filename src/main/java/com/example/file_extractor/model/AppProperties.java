package com.example.file_extractor.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lakithaprabudh
 */
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String lookupDir;
    private String outputDir;
    private String inputFile;

    public String getLookupDir() { return lookupDir; }
    public void setLookupDir(String lookupDir) { this.lookupDir = lookupDir; }
    public String getOutputDir() { return outputDir; }
    public void setOutputDir(String outputDir) { this.outputDir = outputDir; }
    public String getInputFile() { return inputFile; }
    public void setInputFile(String inputFile) { this.inputFile = inputFile; }
}
