import java.net.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        URL url = new URL("https://www.facebook.com/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        List<String> cookies = connection.getHeaderFields().get("Set-Cookie");
        if (cookies != null) {
            for (String cookie : cookies) {
                System.out.println(cookie);
            }
        } else {
            System.out.println("No cookies found.");
        }
        connection.disconnect();
    }
}


