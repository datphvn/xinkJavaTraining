package com.xink.training.day13.util;

import com.xink.training.day13.model.Address;
import com.xink.training.day13.model.User;

public class TestDataBuilder {
    
    public static User createDefaultUser() {
        return new User("John Doe", "john@example.com", 30);
    }
    
    public static User createUserWithAddress() {
        User user = createDefaultUser();
        Address address = new Address("123 Main St", "Springfield", "IL", "62701");
        user.setAddress(address);
        return user;
    }
}

