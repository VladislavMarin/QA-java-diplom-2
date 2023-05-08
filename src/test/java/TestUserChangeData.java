import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import utils.Utils;
import utils.user_reg_and_login_response_deserialization.ResponseFinalDeserializationClass;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static utils.Utils.*;

/**
 * Класс для тестирования обновления данных пользователя
 */
public class TestUserChangeData {

    //Ручка изменения данных пользователя
    String endPoint = API_AUTH_USER;

    //Тело запроса с измененными данными пользователя
    File jsonChangeData = new File("src/test/java/resources/change_data_user/UserChangeData.json");
    //Тело запроса для авторизации пользователя
    File json = new File("src/test/java/resources/login/UserLoginValidData.json");
    //Тело запроса для авторизации пользователя с измененными данными
    File jsonFromDeleteChangeDataUser =
            new File("src/test/java/resources/change_data_user/UserChangeDataFromAuth.json");

    @Before
    public void setUp() {
        Utils.createNewUser();
    }

    @Test
    public void testUserChangeDataWithAuthorization() {

        /**
         * Запрос для получения Bearer-токена для запроса на изменение данных пользователя
         */
        ResponseFinalDeserializationClass responseAuth = given().
                spec(requestSpec).
                body(json).
                post(API_AUTH_LOGIN).
                body().
                as(ResponseFinalDeserializationClass.class);
        /**
         * Запрос на изменение данных пользователя
         */
        Response response = given().
                spec(requestSpec).
                header("Authorization", responseAuth.getAccessToken()).
                body(jsonChangeData).
                when().
                patch(endPoint);
        /**
         * Запрос для получения Bearer-токена для удаления пользователя с измененными данными
         */
        ResponseFinalDeserializationClass responseAuthFromDelete = given().
                spec(requestSpec).
                body(jsonFromDeleteChangeDataUser).
                post(API_AUTH_LOGIN).
                body().
                as(ResponseFinalDeserializationClass.class);
        /**
         * Запрос на удаление пользователя с измененными данными
         */
        Response responseFromDelete = given().
                spec(requestSpec).
                header("Authorization", responseAuthFromDelete.getAccessToken()).
                when().
                delete(endPoint);

        responseFromDelete.then().assertThat().statusCode(202);

        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void testUserChangeDataWithoutAuthorization() {

        /**
         * Запрос на изменение данных пользователя
         */
        Response response = given().
                spec(requestSpec).
                header("Authorization", "").
                body(jsonChangeData).
                when().
                patch(endPoint);

        response.then().assertThat().body("success", equalTo(false)).
                body("message", equalTo("You should be authorised")).
                and().
                statusCode(401);

        Utils.deleteUser();
    }
}
