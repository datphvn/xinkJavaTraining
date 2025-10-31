package com.bigdata.model;

public class ValidatedRecord {
    private final RawRecord rawRecord;
    public ValidatedRecord(RawRecord rawRecord) { this.rawRecord = rawRecord; }
    public RawRecord getRawRecord() { return rawRecord; }
}
