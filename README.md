# File Extractor

A simple Spring Boot CLI tool to parse bank statement text files and extract
specific transaction details into a custom text format.

## Features
- Reads `.txt` input files containing multiple message sections.
- Extracts:
    - Transaction Reference Number
    - Account Identification
    - Reference
    - Amount
- Outputs **only** the requested text format

## Requirements
- Java 17+
- Gradle 

## How to Run
./gradlew bootRun -x test

