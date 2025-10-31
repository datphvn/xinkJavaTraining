package com.bigdata.io;

import java.io.IOException;

public interface OutputFormatter<T> {
    String format(T record) throws IOException;
}