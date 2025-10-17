package com.library.domain.model;

import com.library.domain.exception.ValidationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a book category in the library system.
 * Categories can be organized in a hierarchical structure.
 * Pure OOP implementation without any framework dependencies.
 */
public class Category extends BaseEntity {
    private String categoryId;
    private String name;
    private String description;
    private Category parent;
    private final Set<Category> children;
    private final Set<Book> books;

    /**
     * Constructor for creating a new Category.
     * @param categoryId Unique identifier for the category
     * @param name The name of the category
     * @param description The description of the category
     * @throws IllegalArgumentException if categoryId or name is null or blank
     */
    public Category(String categoryId, String name, String description) {
        super();
        this.categoryId = Objects.requireNonNull(categoryId, "Category ID cannot be null");
        this.name = Objects.requireNonNull(name, "Category name cannot be null");
        this.description = description;
        this.parent = null;
        this.children = new HashSet<>();
        this.books = new HashSet<>();
    }

    // Getters
    public String getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getParent() {
        return parent;
    }

    public Set<Category> getChildren() {
        return new HashSet<>(children);
    }

    public Set<Book> getBooks() {
        return new HashSet<>(books);
    }

    // Setters
    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Category name cannot be null");
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Business methods

    /**
     * Adds a child category to this category.
     * @param child The child category to add
     */
    public void addChild(Category child) {
        Objects.requireNonNull(child, "Child category cannot be null");
        if (child.equals(this)) {
            throw new IllegalArgumentException("A category cannot be its own parent");
        }
        children.add(child);
        child.parent = this;
    }

    /**
     * Removes a child category from this category.
     * @param child The child category to remove
     */
    public void removeChild(Category child) {
        Objects.requireNonNull(child, "Child category cannot be null");
        if (children.remove(child)) {
            child.parent = null;
        }
    }

    /**
     * Sets the parent category for this category.
     * @param parent The parent category
     */
    public void setParent(Category parent) {
        if (parent != null && parent.equals(this)) {
            throw new IllegalArgumentException("A category cannot be its own parent");
        }
        this.parent = parent;
    }

    /**
     * Adds a book to this category.
     * @param book The book to add
     */
    public void addBook(Book book) {
        Objects.requireNonNull(book, "Book cannot be null");
        books.add(book);
    }

    /**
     * Removes a book from this category.
     * @param book The book to remove
     */
    public void removeBook(Book book) {
        Objects.requireNonNull(book, "Book cannot be null");
        books.remove(book);
    }

    /**
     * Gets the number of books in this category.
     * @return The number of books
     */
    public int getBookCount() {
        return books.size();
    }

    /**
     * Gets the number of child categories.
     * @return The number of children
     */
    public int getChildCount() {
        return children.size();
    }

    /**
     * Checks if this category is a root category (has no parent).
     * @return true if this is a root category, false otherwise
     */
    public boolean isRootCategory() {
        return parent == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return Objects.equals(categoryId, category.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + categoryId + '\'' +
                ", name='" + name + '\'' +
                ", bookCount=" + books.size() +
                ", childCount=" + children.size() +
                '}';
    }

    @Override
    public void validate() throws ValidationException {
        Map<String, String> errors = new HashMap<>();
        if (categoryId == null || categoryId.isBlank()) {
            errors.put("categoryId", "Category ID is required");
        }
        if (name == null || name.isBlank()) {
            errors.put("name", "Category name is required");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    @Override
    public String getSummary() {
        return String.format("Category[id=%s, name=%s, books=%d, children=%d]", 
                categoryId, name, books.size(), children.size());
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", categoryId);
        map.put("name", name);
        map.put("description", description);
        map.put("bookCount", books.size());
        map.put("childCount", children.size());
        map.put("parentId", parent != null ? parent.getCategoryId() : null);
        return map;
    }

    @Override
    public void fromMap(Map<String, Object> data) {
        if (data == null) {
            return;
        }
        if (data.containsKey("name")) {
            this.name = (String) data.get("name");
        }
        if (data.containsKey("description")) {
            this.description = (String) data.get("description");
        }
    }
}
