package api;

import com.google.gson.Gson;
import dto.TestOrderDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import specs.RequestSpecOrders;
import utils.TestDataGenerator;
import utils.TestFakerGenerator;

public class ApiDeliveryTest {
    public static final String BASE_URL = "https://backend.tallinn-learning.ee";
    public static final String BASE_PATH = "/test-orders/";

    // Homework_10
    //PUT _is ok
    @Test
    public void changeOrderDetails() {
        RestAssured
                .given()
                .log()
                .all()
                .header("api_key", "1111222233334444")
                .put(BASE_URL + BASE_PATH + "10")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    // DELETE _is OK
    @Test
    //@RepeatedTest(5)
    public void deleteOrderId() {
        RestAssured
                .given()
                .log()
                .all()
                .header("api_key", "3333114455667788")
                .delete(BASE_URL + BASE_PATH + "4")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

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

    // Lesson_10

    @Test
    public void checkOrderDetailsWithCorrectOrderId() {
        RestAssured
                .given()
                .log()
                .all()
                .get(BASE_URL + BASE_PATH + "1")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void checkErrorStatusCodeWithIncorrectOrderId() {
        RestAssured
                .given()
                .log()
                .all()
                .get(BASE_URL + BASE_PATH + "20")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void checkErrorStatusCodeWithNegativeNumberOrderId() {
        RestAssured
                .given()
                .log()
                .all()
                .get(BASE_URL + BASE_PATH + "-20")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void checkOrderDetailsWithCorrectOrderIdAndCheckOrderStatus() {
        String receiveOrderStatus = RestAssured
                .given()
                .log()
                .all()
                .get(BASE_URL + BASE_PATH + "4")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .extract()
                .path("status");

        Assertions.assertEquals("OPEN", receiveOrderStatus);
    }

    @Test
    public void checkOrderIdInResponse() {
        int orderIdRequested = 4;
        int receivedOrderID = RestAssured
                .given()
                .log()
                .all()
                .get(BASE_URL + BASE_PATH + orderIdRequested)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .extract()
                .path("id");

        Assertions.assertEquals(orderIdRequested, receivedOrderID);
    }

    @Test
    public void checkOrderCreation() {
        String requestBodyUglyWay = "{\n" +
                "  \"status\": \"OPEN\",\n" +
                "  \"courierId\": 0,\n" +
                "  \"customerName\": \"Tata\",\n" +
                "  \"customerPhone\": \"11223344\",\n" +
                "  \"comment\": \"hello\",\n" +
                "  \"id\": 0\n" +
                "} ";

        String receivedStatus = RestAssured
                .given()
                .contentType(ContentType.JSON)
                //.queryParam("username", "Olga")
                //.queryParam("password", "123456")
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
                .path("status");
        Assertions.assertEquals("OPEN", receivedStatus);
    }

    @Test
    public void createOrderWithIncorrectStatusAndCheckResponseMessage() {
        String requestBodyUglyWay = "{\n" +
                "  \"status\": \"CLOSED\",\n" +
                "  \"courierId\": 0,\n" +
                "  \"customerName\": \"Tata\",\n" +
                "  \"customerPhone\": \"11223344\",\n" +
                "  \"comment\": \"hello\",\n" +
                "  \"id\": 0\n" +
                "} ";

        String responseBody = RestAssured
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
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .extract()
                .asString();
        Assertions.assertEquals("Incorrect query", responseBody);
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
        Gson gson= new Gson();


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
        TestOrderDto order = gson.fromJson(responseBody.asString(),TestOrderDto.class);
        Assertions.assertEquals("OPEN", order.getStatus());
        Assertions.assertEquals("Tata", order.getCustomerName());
        Assertions.assertEquals("11223344", order.getCustomerPhone());
        Assertions.assertEquals("hello", order.getComment());
        Assertions.assertNotNull( order.getId());

    }


}
