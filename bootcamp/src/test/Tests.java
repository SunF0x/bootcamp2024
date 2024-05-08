package test;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.json.JSONObject;

import java.util.Random;


public class Tests {
    String base_url = "https://108909b1-ee03-45a1-b416-a9a60a2f0589.mock.pstmn.io";

    public static String getRandomIntegerBetweenRange(int min, int max){
        Random rn = new Random();
        String x = "";
        x += rn.nextInt((max - min + 1)) + min;
        return x;
    }

    String msisdn = "8" + getRandomIntegerBetweenRange(10000, 99999) + getRandomIntegerBetweenRange(10000, 99999);


    @Test
    public void testGetClientStatusCode()
    {
        get(base_url+"/clients").then().statusCode(200);
    }

    @Test
    public void testGetClientField() {
        get(base_url+"/clients").then().assertThat()
            .body("$", hasKey("clients"));
    }

    @Test
    public void testPostClientAdd() throws JSONException {
        JSONObject requestParams = new JSONObject();
        requestParams.put("msisdn", msisdn);
        requestParams.put("tariffId", "11");
        requestParams.put("money", getRandomIntegerBetweenRange(1,1000));

        given()
                .body(requestParams.toString())
                .contentType("application/json")
                .when().post(base_url+"/clients").then()
                .statusCode(200).assertThat()
                .body("$", hasKey("msisdn"))
                .body("$", hasKey("tariffId"))
                .body("$", hasKey("money"));
    }

//    @Test
//    public void testPostClientAddFail() throws JSONException {
//        JSONObject requestParams = new JSONObject();
//        requestParams.put("msisdn", 9);
//        requestParams.put("tariffId", 0);
//        requestParams.put("money", 100);
//
//        given()
//                .body(requestParams.toString())
//                .contentType("application/json")
//                .when().post(base_url+"/clients").then()
//                .statusCode(400); //ожидается 400 из-за некорректных данных
//    }

    @Test
    public void testGetBalance() {
        get(base_url+"/clients/"+msisdn+"/balance").then()
                .statusCode(200).assertThat()
                .body("$", hasKey("msisdn")).body("$", hasKey("money")); //ожидается 400
    }

    @Test
    public void testPatchBalance() throws JSONException {
        JSONObject requestParams = new JSONObject();
        requestParams.put("msisdn", msisdn);
        requestParams.put("money", getRandomIntegerBetweenRange(1,1000));

        given()
                .body(requestParams.toString())
                .contentType("application/json")
                .when().patch(base_url+"/clients/"+msisdn+"/balance").then()
                .statusCode(200).assertThat()
                .body("$", hasKey("msisdn")).body("$", hasKey("money"));
    }

    @Test
    public void testPatchTariffID() throws JSONException {
        JSONObject requestParams = new JSONObject();
        requestParams.put("msisdn", msisdn);
        requestParams.put("tariffId", "12");

        given()
                .body(requestParams.toString())
                .contentType("application/json")
                .when().patch(base_url+"/clients/"+msisdn+"/tariffID").then()
                .statusCode(200).assertThat()
                .body("$", hasKey("msisdn"))
                .body("$", hasKey("tariffId"));
    }

}
