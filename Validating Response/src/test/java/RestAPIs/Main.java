package RestAPIs;

import io.restassured.module.jsv.JsonSchemaValidator;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.testng.annotations.Test;
import java.io.IOException;
import java.io.InputStream;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

public class Main {

    @Test
    public void testStatusCode() {
        // Validate status code
        given()
                .when()
                .get("https://reqres.in/api/users?page=1")
                .then()
                .assertThat()
                .statusCode(200);
        System.out.println("Response Status code test passed successfully.");
    }

    @Test
    public void testResponseHeaders() throws IOException {
        String url = "https://reqres.in/api/users";
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            Headers headers = response.headers();
            assertEquals("max-age=14400", headers.get("Cache-Control"));
            assertEquals("application/json; charset=utf-8", headers.get("Content-Type"));
            assertEquals("1.1 vegur", headers.get("Via"));
            assertTrue(headers.get("X-Powered-By").startsWith("Express"));
            assertNotNull(headers.get("ETag"));
            assertNotNull(headers.get("Date"));
        }
        System.out.println("Response headers test passed successfully.");
    }



    @Test
    public void testResponseBody() {
        // Validate response body
        given()
                .when()
                .get("https://reqres.in/api/users?page=1")
                .then()
                .assertThat()
                .body("page", equalTo(1))
                .body("per_page", equalTo(6))
                .body("total", greaterThan(0))
                .body("data", not(empty()))
                .body("data.id", hasItems(1, 2, 3, 4, 5, 6));
        System.out.println("Response body test passed successfully");
    }

    @Test
    public void testResponseTime() {
        // Validate response time
        given()
                .when()
                .get("https://reqres.in/api/users?page=1")
                .then()
                .assertThat()
                .time(lessThan(5000L)); // Response time in milliseconds
        System.out.println("Response time test passed successfully");
    }

    @Test
    public void testResponseSchema() {
        // Validate response schema
        given()
                .when()
                .get("https://reqres.in/api/users?page=1")
                .then()
                .assertThat()
                .body(matchesJsonSchema(getSchemaFile("schema.json")));
        System.out.println("Response schema test passed successfully");
    }

    private JsonSchemaValidator matchesJsonSchema(InputStream schemaStream) {
        return JsonSchemaValidator.matchesJsonSchema(schemaStream);
    }


    public static InputStream getSchemaFile(String fileName) {
        ClassLoader classLoader = Main.class.getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }
}
