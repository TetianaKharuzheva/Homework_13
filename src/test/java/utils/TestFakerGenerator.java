package utils;

import com.github.javafaker.Faker;

public class TestFakerGenerator {

    public static String fakerCustomerName() {
        return Faker.instance().letterify("??????");
    }

    public static String fakerCustomerPhone() {
        return Faker.instance().numerify("########");
    }
}
