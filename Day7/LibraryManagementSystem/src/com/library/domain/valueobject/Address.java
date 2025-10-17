package com.library.domain.valueobject;

import java.util.Objects;

/**
 * Value object representing a physical address.
 * This is an immutable value object that enforces business rules for addresses.
 * Pure OOP implementation without any framework dependencies.
 */
public final class Address {
    private final String street;
    private final String city;
    private final String postalCode;
    private final String country;
    private final String stateProvince;

    protected Address() {
        // For JPA
        this.street = null;
        this.city = null;
        this.postalCode = null;
        this.country = null;
        this.stateProvince = null;
    }

    public Address(String street, String city, String postalCode, String country, String stateProvince) {
        this.street = Objects.requireNonNull(street, "Street cannot be null");
        this.city = Objects.requireNonNull(city, "City cannot be null");
        this.postalCode = Objects.requireNonNull(postalCode, "Postal code cannot be null");
        this.country = Objects.requireNonNull(country, "Country cannot be null");
        this.stateProvince = stateProvince; // Optional field
    }

    public String getStreet() { 
        return street; 
    }
    
    public String getCity() { 
        return city; 
    }
    
    public String getPostalCode() {
        return postalCode;
    }
    
    public String getCountry() { 
        return country; 
    }
    
    public String getStateProvince() {
        return stateProvince;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(street).append(", ");
        if (stateProvince != null && !stateProvince.isBlank()) {
            sb.append(stateProvince).append(", ");
        }
        sb.append(city).append(" ").append(postalCode).append(", ").append(country);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address address = (Address) o;
        return street.equals(address.street) && 
               city.equals(address.city) && 
               postalCode.equals(address.postalCode) && 
               country.equals(address.country) && 
               Objects.equals(stateProvince, address.stateProvince);
    }

    @Override
    public int hashCode() { 
        return Objects.hash(street, city, postalCode, country, stateProvince); 
    }
}
