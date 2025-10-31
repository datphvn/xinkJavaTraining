package com.bigdata.processors;

import com.bigdata.core.DataProcessor;
import com.bigdata.model.RawRecord;
import com.bigdata.model.ValidatedRecord;
import com.bigdata.util.ValidationRule;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ValidationProcessor implements DataProcessor<RawRecord, ValidatedRecord> {
    private final List<ValidationRule<RawRecord>> rules;

    public ValidationProcessor(List<ValidationRule<RawRecord>> rules) {
        this.rules = rules;
    }

    @Override
    public Stream<ValidatedRecord> process(Stream<RawRecord> input) {
        // Stream-based validation
        return input
                .map(this::validateRecord)
                .filter(Optional::isPresent) // Remove failed validations
                .map(Optional::get);         // Extract the ValidatedRecord
    }

    private Optional<ValidatedRecord> validateRecord(RawRecord record) {
        for (ValidationRule<RawRecord> rule : rules) {
            if (!rule.test(record)) {
                // Failed validation, return empty Optional
                return Optional.empty();
            }
        }
        return Optional.of(new ValidatedRecord(record));
    }
}