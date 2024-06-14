package api;

import com.google.gson.Gson;
import dto.LoginDto;
import dto.TestOrderDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class RealOrderTest {
    //    String tokenManually = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0LWtoYXJ1emhldmEiLCJleHAiOjE3MTgxNDk0OTQsImlhdCI6MTcxODEzMTQ5NH0.5XiFNcC0SvF_1yVsEuj7k4oKGRvA71cHxEji9Ln_w9H3PFdvpwRXFvPmBKG1Pwoyq9k5l9slv1vJbI_artdbuQ";
    public static final String BASE_URL = "https://backend.tallinn-learning.ee";
    static String tokenAutomation;


    @BeforeAll
    public static void setup() {
        String username = System.getenv("USERNAME");
        String password = System.getenv("PASSWORD");
        Assumptions.assumeTrue(username != null && password != null);
        Gson gson = new Gson();
        //LoginDto loginDtoJavaObject = new LoginDto("t-kharuzheva", "pgd3VbfT2nt");
        LoginDto loginDtoJavaObject = new LoginDto(username, password);
        // send post request to get token with username & password
        tokenAutomation = RestAssured
                .given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .when()
                .body(gson.toJson(loginDtoJavaObject))
                .post(BASE_URL + "/login/student")
                .then()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .extract()
                .body()
                .asString();
    }

    @Test
    public void createOrder() {
        Gson gson = new Gson();
        TestOrderDto realOrderJavaObject = new TestOrderDto("Alina", "112233", "one");
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenAutomation)
                .log()
                .all()
                .when()
                .body(gson.toJson(realOrderJavaObject))
                .post(BASE_URL + "/orders")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void checkExistingOrderById() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenAutomation)
                .log()
                .all()
                .when()
                .get(BASE_URL + "/orders/779")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }
}
