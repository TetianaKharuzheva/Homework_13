package specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RequestSpecOrders {
    //Declare Specs as a static method
    public static RequestSpecification getSpec() {
        RequestSpecification requestSpec = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        return requestSpec;
    }


}
