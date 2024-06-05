package utils;

import org.apache.commons.lang3.RandomStringUtils;

public class TestDataGenerator {

    public static String generateRandomCustomerName(){
        return RandomStringUtils.randomAlphabetic(5);
    }

    public static String generateRandomComment(){
        return RandomStringUtils.randomAlphabetic(8);
    }

    public static String generateRandomCustomerPhone(){
        return RandomStringUtils.randomNumeric(8);
    }
}
