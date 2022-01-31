package services;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import io.restassured.http.ContentType;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;

public class baseOfBooking {
    String token;
    Integer bookingid;

    @BeforeTest
    public void postCreatedToken() {

        String postData= "{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}";

        Response response = given()
                .body(postData)
                .header("Content-Type", "application/json")
                .log().all(). //header,params
                when()
                .post("https://restful-booker.herokuapp.com/auth");
        Assert.assertEquals(response.getStatusCode(),200);
        System.out.println(response.then().log().all());  //log görmek için
         token = response.jsonPath().getString("token");
    }

    @BeforeTest
    public void postCreateBooking() {

        String postData= "{\n" +
                "    \"firstname\" : \"Jim\",\n" +
                "    \"lastname\" : \"Brown\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2018-01-01\",\n" +
                "        \"checkout\" : \"2019-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";

        Response response1 = given()
                .body(postData)
                .header("Content-Type","application/json")
                .log().all(). //header,params
                when()
                .post("https://restful-booker.herokuapp.com/booking");
        Assert.assertEquals(response1.getStatusCode(),200);
        System.out.println(response1.then().log().all());  //log görmek için
        bookingid = response1.jsonPath().getInt("bookingid");
    }


    public void putUpdateBooking() {

        Map<String,Object> queryParams = new HashMap<>();
        queryParams.put("firstname","Academy");
        queryParams.put("lastname","QA");

        given()
                .log().all()
                .queryParams(queryParams)
                .header("Content-Type", "application/json")
                .header("Cookie", token)
                .log().all(). //header,params
                when()
                .put("https://restful-booker.herokuapp.com/booking/"+bookingid).
                then() //assertion
                .statusCode(200)
                .log().all();

    }

    @Test
    public void getBooking(){
        given()
                .log().all(). //header,params
                when()
                .get("https://restful-booker.herokuapp.com/booking/"+bookingid).
                then() //assertion
                .statusCode(200)
                .log().all();

    }

    @AfterTest
    public void deleteBooking(){
        given()
                .header("Content-Type","application/json")
                .header("Cookie",token)
                .log().all(). //header,params
                when()
                .delete("https://restful-booker.herokuapp.com/booking/"+bookingid).
                then() //assertion
                .statusCode(200)
                .log().all();

    }

}
