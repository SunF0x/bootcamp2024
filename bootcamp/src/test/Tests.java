package test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.restassured.response.Response;
import net.datafaker.Faker;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.json.JSONObject;

import java.util.Random;

import java.util.Base64;


public class Tests {
    String base_url = "http://localhost:5050";

    static String msisdn = "79876543212";
    Faker faker = new Faker();

    public static String encode(String str1, String str2) {
        return new String(Base64.getEncoder().encode((str1 + ":" + str2).getBytes()));
    }

    public static String getRandomIntegerBetweenRange(int min, int max){
        Random rn = new Random();
        String x = "";
        x += rn.nextInt((max - min + 1)) + min;
        return x;
    }

    @Test
    public void testGetClients()
    {
        String authorization = encode("admin", "admin");
        Response response = given().when()
                .header("authorization", "Basic " + authorization).
                get(base_url+"/clients");
        Assertions.assertEquals(200, response.statusCode());
        System.out.println(response.asPrettyString());
    }

    @Test
    public void testPostClientAdd() throws JSONException {
        String authorization = encode("admin", "admin");
        JSONObject requestParams = new JSONObject();
        String new_msisdn = "79" + getRandomIntegerBetweenRange(1000, 9999)
                + getRandomIntegerBetweenRange(10000, 99999);
        System.out.println(new_msisdn);
        requestParams.put("msisdn", new_msisdn);
        requestParams.put("tariffId", "11");
        requestParams.put("name", faker.name().firstName());
        requestParams.put("money", getRandomIntegerBetweenRange(1,1000));

        Response response = given().when()
                .header("authorization", "Basic " + authorization)
                .body(requestParams.toString())
                .contentType("application/json")
                .when().post(base_url+"/clients");
        Assertions.assertEquals(200, response.statusCode());
        System.out.println(response.asPrettyString());
    }

    @Test
    public void FailTestPostClientAdd1() throws JSONException {
        String authorization = encode("admin", "admin");
        JSONObject requestParams = new JSONObject();
        String new_msisdn = "79" + getRandomIntegerBetweenRange(1000, 9999)
                + getRandomIntegerBetweenRange(10000, 99999);
        System.out.println(new_msisdn);
        requestParams.put("msisdn", new_msisdn);
        requestParams.put("tariffId", -100);
        requestParams.put("name", 0);
        requestParams.put("money", getRandomIntegerBetweenRange(-1000,0));

        Response response = given().when()
                .header("authorization", "Basic " + authorization)
                .body(requestParams.toString())
                .contentType("application/json")
                .when().post(base_url+"/clients");
        Assertions.assertEquals(400, response.statusCode());
        System.out.println(response.asPrettyString());
    }


    @Test
    public void testGetBalance() {
        String authorization = encode(msisdn, msisdn);
        Response response = given().when()
                .header("authorization", "Basic " + authorization)
                .get(base_url+"/clients/"+msisdn+"/balance");
        Assertions.assertEquals(200, response.statusCode());
        System.out.println(response.jsonPath().getString("money"));
    }

    @Test
    public void testPatchBalance() throws JSONException {
        String authorization = encode(msisdn, msisdn);
        JSONObject requestParams = new JSONObject();
        requestParams.put("msisdn", msisdn);
        requestParams.put("money", getRandomIntegerBetweenRange(1,1000));

        Response response = given().when()
                .header("authorization", "Basic " + authorization)
                .body(requestParams.toString())
                .contentType("application/json")
                .when().patch(base_url+"/clients/"+msisdn+"/balance");

        Assertions.assertEquals(200, response.statusCode());
        System.out.println(response.asPrettyString());
    }

    @Test
    public void FailTestPatchBalance1() throws JSONException {
        String authorization = encode(msisdn, msisdn);
        JSONObject requestParams = new JSONObject();
        requestParams.put("msisdn", msisdn);
        requestParams.put("money", getRandomIntegerBetweenRange(-1000,0));

        Response response = given().when()
                .header("authorization", "Basic " + authorization)
                .body(requestParams.toString())
                .contentType("application/json")
                .when().patch(base_url+"/clients/"+msisdn+"/balance");

        Assertions.assertEquals(400, response.statusCode());
        System.out.println(response.asPrettyString());
    }

    @Test
    public void FailTestPatchBalance2() throws JSONException {
        String authorization = encode(msisdn, msisdn);
        JSONObject requestParams = new JSONObject();
        requestParams.put("msisdn", 8888);
        requestParams.put("money", getRandomIntegerBetweenRange(1000,2000));

        Response response = given().when()
                .header("authorization", "Basic " + authorization)
                .body(requestParams.toString())
                .contentType("application/json")
                .when().patch(base_url+"/clients/"+msisdn+"/balance");

        Assertions.assertEquals(403, response.statusCode());
        System.out.println(response.asPrettyString());
    }

    @Test
    public void testPatchTariffID() throws JSONException {
        String authorization = encode("admin", "admin");
        JSONObject requestParams = new JSONObject();
        requestParams.put("msisdn", msisdn);
        requestParams.put("tariffId", "13");

        Response response = given().when()
                .header("authorization", "Basic " + authorization)
                .body(requestParams.toString())
                .contentType("application/json")
                .when().patch(base_url+"/clients/tariffID");
        Assertions.assertEquals(200, response.statusCode());
        System.out.println(response.asPrettyString());
    }

//    @Test
//    public void FailTestPatchTariffID1() throws JSONException {
//        String authorization = encode("admin", "admin");
//        JSONObject requestParams = new JSONObject();
//        requestParams.put("msisdn", msisdn);
//        requestParams.put("tariffId", -666);
//
//        Response response = given().when()
//                .header("authorization", "Basic " + authorization)
//                .body(requestParams.toString())
//                .contentType("application/json")
//                .when().patch(base_url+"/clients/tariffID");
//        Assertions.assertEquals(400, response.statusCode());
//        System.out.println(response.asPrettyString());
//    }

    @Test
    public void FailTestPatchTariffID2() throws JSONException {
        String authorization = encode("admin", "admin");
        JSONObject requestParams = new JSONObject();
        requestParams.put("msisdn", 8888);
        requestParams.put("tariffId", "-1");

        Response response = given().when()
                .header("authorization", "Basic " + authorization)
                .body(requestParams.toString())
                .contentType("application/json")
                .when().patch(base_url+"/clients/tariffID");
        Assertions.assertEquals(403, response.statusCode());
        System.out.println(response.asPrettyString());
    }

}
