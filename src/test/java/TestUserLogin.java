import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.Utils;

import java.io.File;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 * Класс для тестирования авторизации пользователя
 */
public class TestUserLogin {

    //Ручка для авторизации
    String endPoint = Utils.API_AUTH_LOGIN;

    //Тело запроса с валидными данными для авторизации
    File jsonLoginValidData = new File("src/test/java/resources/login/UserLoginValidData.json");

    //Тело запроса с невалидными данными для авторизации
    File jsonLoginNoValid = new File("src/test/java/resources/login/UserLoginNoValidData.json");

    @After
    public void tearDown()  {
        Utils.deleteUser();
    }

    @Before
    public void setUp() {
        Utils.createNewUser();
    }

    @Test
    public void testUserLoginValidData() {

        Response response = given()
                .spec(Utils.requestSpec)
                .body(jsonLoginValidData)
                .when()
                .post(endPoint);

        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void testUserLoginNoValidData() {

        Response response = given()
                .spec(Utils.requestSpec)
                .body(jsonLoginNoValid)
                .when()
                .post(endPoint);

        response.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);
    }
}
