import io.restassured.RestAssured;

import java.util.ArrayList;
import java.util.List;
import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;

public class EmployeeID {

    public static void main(String[] args) {

        RestAssured.baseURI = "https://reqres.in";

        // Fetch employee IDs from page 1
        String responsePage1 = given()
                .param("page", 1)
                .when()
                .get("/api/users")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .asString();

        // Fetch employee IDs from page 2
        String responsePage2 = given()
                .param("page", 2)
                .when()
                .get("/api/users")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .asString();

        // Extract all employee IDs between 1 and 10 from both pages
        List<Integer> employeeIdsPage1 = from(responsePage1)
                .getList("data.findAll { it.id >= 1 && it.id <= 6 }.id");
        List<Integer> employeeIdsPage2 = from(responsePage2)
                .getList("data.findAll { it.id >= 7 && it.id <= 10 }.id");
        List<Integer> allEmployeeIds = new ArrayList<>();
        allEmployeeIds.addAll(employeeIdsPage1);
        allEmployeeIds.addAll(employeeIdsPage2);

        System.out.println("Employee IDs between 1 and 10: " + allEmployeeIds);
    }
}

