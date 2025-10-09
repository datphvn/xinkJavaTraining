package com.library.domain.model;

import java.util.Objects;

public class Author {
    private String name;

    public Author(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Author name cannot be empty");
        this.name = name;
    }

    public String getName() { return name; }

    @Override
    public String toString() { return name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Author)) return false;
        Author a = (Author) o;
        return Objects.equals(name, a.name);
    }

    @Override
    public int hashCode() { return Objects.hash(name); }
}
