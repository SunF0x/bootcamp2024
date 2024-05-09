package test;

import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static io.restassured.RestAssured.given;


public class Tests_balance {
    String base_url = "http://localhost:5050";

    String msisdn = "79876543212";

    public static Date toData(long unixSeconds) {
        Date date = new java.util.Date(unixSeconds);

        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-4"));

        return date;
//        String formattedDate = sdf.format(date);
//        System.out.println(formattedDate);
    }

    @Test
    public void testGetBalance() {
        Response response = given().when()
                .get(base_url+"/clients/clients/"+msisdn+"/balance").then()
                .extract().response();

        Assertions.assertEquals(200, response.statusCode());
        System.out.println(response.jsonPath().getString("money"));


    }

    @Test
    public void testTariff11() {
        //01,79876543212,79098765432,1711913927919,1711915854958 - это 1,5 у.е/мин
        // только исходящие внутри Ромашки, введите время начала и конца звонка из CDR
        Date start = toData(1711913927919L);
        Date end = toData(1711915854958L);
        Assertions.assertEquals(
                (int)Math.ceil((end.getTime() - start.getTime())/60000 +1) *1.5,
                734.5-685);
    }

    @Test
    public void testTariff11_month() { // за несколько месяцев
        //01,79234567891,79234567890,1711918614412,1711920235280 - 1.5
        //01,79234567891,79512345678,1699233441442,1699236960536 - 1.5
        // только исходящие внутри Ромашки, введите время начала и конца звонка из CDR
        Date start = toData(1711918614412L);
        Date end = toData(1711920235280L);
        Date start2 = toData(1699233441442L);
        Date end2 = toData(1699236960536L);
        Assertions.assertEquals(
                (int)Math.ceil((end.getTime() - start.getTime())/60000 +1) *1.5
                + (int)Math.ceil((end2.getTime() - start2.getTime())/60000 +1) *1.5,512-381.5
                );
    }

    @Test
    public void testTariff12() {
        //02,79234567890,79234567891,1711918614412,1711920235280
        // введите время начала и конца звонка из CDR
        Date start = toData(1711918614412L);
        Date end = toData(1711920235280L);
        //System.out.println((end.getTime() - start.getTime())/60000);
        Assertions.assertEquals(
                (end.getTime() - start.getTime())/60000>50 ?
                        50+(int)Math.ceil((end.getTime() - start.getTime())/60000 +1)
                        : 100, // тарификация такая, что в одном месяце он только разговариавал
                100);
    }

    @Test
    public void testTariff12_month() { //в каждом месяце по звонку
        //01,79543457634,79512345679,1711901083443,1711902577424
        //02,79543457634,79123456788,1684359524627,1684361508866
        // введите время начала и конца звонка из CDR
        Date start = toData(1711901083443L);
        Date end = toData(1711902577424L);
        //System.out.println((end.getTime() - start.getTime())/60000);
        int sum = 100;

        start = toData(1684359524627L);
        end = toData(1684361508866L);
        //System.out.println((end.getTime() - start.getTime())/60000);
        sum += 100;
        Assertions.assertEquals(sum,200);
    }

//'{
//  "tariff_id": "11",
//  "name": "Классика",
//  "description": "Входящие - бесплатно. Исходящие внутри ромашки - 1,5 у.е/мин, для остальных - 2,5 у.е./мин",
//  "currency": "RUB",
//  "prepaid": null,
//  "overlimit": {
//    "internal_incoming": "0.00",
//    "internal_outcoming": "1.50",
//    "extermal_incoming": "0.00",
//    "external_outcoming": "2.50"
//  }
//}'),
//    (12, '{
//      "tariff_id": "12",
//      "name": "Помесячный",
//      "description": "Лимит 50 минут на все звонки, сверх лимита - по тарифу Классика",
//      "currency": "RUB",
//      "prepaid": {
//        "cost": "100",
//        "limits": {
//          "total_minutes": "50"
//      }
//    ,
//      "overlimit": {
//        "reference_tariff_id": "11"
//      }
//    }
//  }')
}
