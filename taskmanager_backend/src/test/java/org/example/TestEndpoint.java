package org.example;

import io.restassured.RestAssured;
import io.restassured.RestAssured.*;

public class TestEndpoint {

  public static void main(String[] args){
    String token ="exampleToken123";

    String hello = String.valueOf(RestAssured.given()
        .header("Authorization", "Bearer "+token)
        .when()
        .get("http://localhost:8000/task")
        .then()
        .statusCode(200)
        .extract()
        .body());
  }

}
