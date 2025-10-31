package com.bigdata.util;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Predicate;
import java.util.List;
import java.util.ArrayList;

// Utility for Validation
public interface ValidationRule<T> extends Predicate<T> {}

