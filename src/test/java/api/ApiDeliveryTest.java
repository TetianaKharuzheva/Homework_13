package api;

import com.google.gson.Gson;
import dto.TestOrderDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import specs.RequestSpecOrders;
import utils.TestDataGenerator;
import utils.TestFakerGenerator;

public class ApiDeliveryTest {
    public static final String BASE_URL = "https://backend.tallinn-learning.ee";
    public static final String BASE_PATH = "/test-orders/";

    // GET with query params
    @Test
    public void authenticateUser() {
        RestAssured
                .given()
                .queryParam("username", "Tata")
                .queryParam("password", "123456")
                .log()
                .all()
                .get(BASE_URL + BASE_PATH)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    // Lesson_11
    @Test
    public void createOrderWithDtoPattern() {
        // order creation
        TestOrderDto orderDtoRequest = new TestOrderDto("tata", "223344", "Hello");
        // serialization from java to json
        String requestBodyAsJson = new Gson().toJson(orderDtoRequest);
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBodyAsJson)
                .log()
                .all()
                .post(BASE_URL + BASE_PATH)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void createOrderWithDtoPatternAndSetters() {
        // order creation by default constructor
        TestOrderDto orderDtoRequest = new TestOrderDto();
        orderDtoRequest.setComment("Hello");
        orderDtoRequest.setCustomerName("Tata");
        orderDtoRequest.setCustomerPhone("223344");
        // serialization from java to json
        String requestBodyAsJson = new Gson().toJson(orderDtoRequest);
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBodyAsJson)
                .log()
                .all()
                .post(BASE_URL + BASE_PATH)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void createOrderWithDtoPatternAndSettersAndRandomValues() {
        // order creation by default constructor
        TestOrderDto orderDtoRequest = new TestOrderDto();
        //String comment = RandomStringUtils.randomAlphabetic(8);
        //String customerName = RandomStringUtils.randomAlphabetic(5);
        //String customerPhone = RandomStringUtils.randomNumeric(8);
        orderDtoRequest.setComment(TestDataGenerator.generateRandomComment());
        orderDtoRequest.setCustomerName(TestDataGenerator.generateRandomCustomerName());
        orderDtoRequest.setCustomerPhone(TestDataGenerator.generateRandomCustomerPhone());
        // serialization from java to json
        String requestBodyAsJson = new Gson().toJson(orderDtoRequest);
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBodyAsJson)
                .log()
                .all()
                .post(BASE_URL + BASE_PATH)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    // Homework_11
    @Test
    public void createOrderWithDtoPatternHalfRandomValuesHalfFakerValues() {
        TestOrderDto orderDtoRequest = new TestOrderDto();
        orderDtoRequest.setComment(TestDataGenerator.generateRandomComment());
        orderDtoRequest.setCustomerName(TestFakerGenerator.fakerCustomerName());
        orderDtoRequest.setCustomerPhone(TestFakerGenerator.fakerCustomerPhone());
        String requestBodyAsJson = new Gson().toJson(orderDtoRequest);
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBodyAsJson)
                .log()
                .all()
                .post(BASE_URL + BASE_PATH)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    //Lesson_12
    @Test
    public void createOrderWithDSpec() {
        TestOrderDto orderDtoRequest = new TestOrderDto();
        orderDtoRequest.setComment(TestDataGenerator.generateRandomComment());
        orderDtoRequest.setCustomerName(TestDataGenerator.generateRandomCustomerName());
        orderDtoRequest.setCustomerPhone(TestDataGenerator.generateRandomCustomerPhone());
        String requestBodyAsJson = new Gson().toJson(orderDtoRequest);
        RestAssured
                .given()
                .spec(RequestSpecOrders.getSpec())
                .body(requestBodyAsJson)
                .log()
                .all()
                .post(BASE_URL + BASE_PATH)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void checkOrderResponseBody() {
        String requestBodyUglyWay = "{\n" +
                "  \"status\": \"OPEN\",\n" +
                "  \"courierId\": 0,\n" +
                "  \"customerName\": \"Tata\",\n" +
                "  \"customerPhone\": \"11223344\",\n" +
                "  \"comment\": \"hello\",\n" +
                "  \"id\": 0\n" +
                "} ";
        Gson gson = new Gson();
        Response responseBody = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBodyUglyWay)
                .log()
                .all()
                .post(BASE_URL + BASE_PATH)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .extract()
                .response();
        // Transformation from Json to Java object of order dto class
        TestOrderDto order = gson.fromJson(responseBody.asString(), TestOrderDto.class);
        Assertions.assertEquals("OPEN", order.getStatus());
        Assertions.assertEquals("Tata", order.getCustomerName());
        Assertions.assertEquals("11223344", order.getCustomerPhone());
        Assertions.assertEquals("hello", order.getComment());
        Assertions.assertNotNull(order.getId());
    }
}
