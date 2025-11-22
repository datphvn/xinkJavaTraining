package com.xink.training.day13.exercise1;

import com.xink.training.day13.model.Address;
import com.xink.training.day13.model.User;
import org.assertj.core.api.AbstractObjectAssert;

import java.util.Objects;

public class UserAssertions {
    
    public static UserAssert assertThat(User actual) {
        return new UserAssert(actual);
    }
    
    public static class UserAssert extends AbstractObjectAssert<UserAssert, User> {
        
        public UserAssert(User actual) {
            super(actual, UserAssert.class);
        }
        
        public UserAssert hasName(String expectedName) {
            isNotNull();
            if (!Objects.equals(actual.getName(), expectedName)) {
                failWithMessage("Expected user name to be <%s> but was <%s>", 
                    expectedName, actual.getName());
            }
            return this;
        }
        
        public UserAssert hasValidEmail() {
            isNotNull();
            if (actual.getEmail() == null || !actual.getEmail().contains("@")) {
                failWithMessage("Expected user to have valid email but was <%s>", 
                    actual.getEmail());
            }
            return this;
        }
        
        public UserAssert isAdult() {
            isNotNull();
            if (actual.getAge() < 18) {
                failWithMessage("Expected user to be adult but age was <%d>", 
                    actual.getAge());
            }
            return this;
        }
        
        public UserAssert hasAddress() {
            isNotNull();
            if (actual.getAddress() == null) {
                failWithMessage("Expected user to have address but was null");
            }
            return this;
        }
        
        public UserAssert hasAddressInCity(String expectedCity) {
            hasAddress();
            if (!Objects.equals(actual.getAddress().getCity(), expectedCity)) {
                failWithMessage("Expected user address city to be <%s> but was <%s>",
                    expectedCity, actual.getAddress().getCity());
            }
            return this;
        }
    }
}

