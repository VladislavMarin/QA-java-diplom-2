package utils;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.user_reg_and_login_response_deserialization.ResponseFinalDeserializationClass;

import java.io.File;

import static io.restassured.RestAssured.given;

/**
 * Утилитный класс
 *
 * Сразу хочу огововрить, что сначала сделал метод получения Bearer-токена и вытащил в этот класс,
 * Но при написании тестов было неудобно использовать метод. Поэтому каждый раз при получении токена использую запрос.
 * Извините)
 */
public class Utils {

    public static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("https://stellarburgers.nomoreparties.site")
            .build().header("Content-Type", "application/json");

    public static final String API_AUTH_REGISTER = "/api/auth/register";

    public static final String API_AUTH_LOGIN = "/api/auth/login";

    public static final String API_AUTH_USER = "/api/auth/user";

    public static final String API_ORDERS = "/api/orders";


    /**
     * Метод регистрации нового пользователя
     */
    public static void createNewUser() {

        File json = new File("src/test/java/resources/registration/NewUserValidData.json");

        Response response = given().
                spec(requestSpec).
                body(json).
                when().
                post(API_AUTH_REGISTER);

        response.then().statusCode(200);
    }

    /**
     * Метод удаления нового пользователя
     */
    public static void deleteUser() {

        File json = new File("src/test/java/resources/login/UserLoginValidData.json");

        ResponseFinalDeserializationClass responseAuth = given().
                spec(requestSpec).
                body(json).
                post(API_AUTH_LOGIN).
                body().
                as(ResponseFinalDeserializationClass.class);

        Response response = given().
                spec(requestSpec).
                header("Authorization", responseAuth.getAccessToken()).
                when().
                delete(API_AUTH_USER);

        response.then().assertThat().statusCode(202);
    }
}
