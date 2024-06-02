package api;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

public class ApiDeliveryTest {
    public static final String BASE_URL = "https://backend.tallinn-learning.ee";
    public static final String BASE_PATH = "/test-orders/";

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
}
