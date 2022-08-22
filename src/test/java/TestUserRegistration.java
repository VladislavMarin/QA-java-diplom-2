import io.restassured.response.Response;
import org.junit.Test;
import utils.Utils;

import java.io.File;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

/**
 * Класс для тестирования регистрации пользователя
 */
public class TestUserRegistration {

    Response response;

    //Ручка для создания пользователя
    String endPoint = Utils.API_AUTH_REGISTER;

    //Тело запроса с валидными данными для регистрации пользователя
    File json = new File("src/test/java/resources/registration/NewUserValidData.json");

    //Тело запроса с невалидными данными. Отсутсвует "password"
    File jsonNoPassword = new File("src/test/java/resources/registration/NewUserNoValidDataNoPassword.json");
    //Тело запроса с невалидными данными. Отсутсвует "email"
    File jsonNoEmail = new File("src/test/java/resources/registration/NewUserNoValidDataNoEmail.json");
    //Тело запроса с невалидными данными. Отсутсвует "name"
    File jsonNoName = new File("src/test/java/resources/registration/NewUserNoValidDataNoName.json");

    /**
     * Тест на создание уникального пользователя;
     */
    @Test
    public void testNewUserRegistration() {

        Response response = given().
                spec(Utils.requestSpec).
                body(json).
                when().
                post(endPoint);

        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);

        Utils.deleteUser();
    }

    /**
     * Тест на создание пользователя, который уже зарегистрирован;
     */
    @Test
    public void testNewUserRegistrationWhoIsRegistered() {

        Utils.createNewUser();

        Response response = given().
                spec(Utils.requestSpec).
                body(json).
                when().
                post(endPoint);

        response.then().assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"))
                .and()
                .statusCode(403);

        Utils.deleteUser();
    }

    /**
     * Тест на создание пользователя  без заполнения одного/всех из обязательных полей
     */
    @Test
    public void testNewUerRegistrationOneFieldNoFilled() {

        registrationRequest(jsonNoPassword);
        registrationRequest(jsonNoEmail);
        registrationRequest(jsonNoName);

        response.then().assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    /**
     * Метод необходимый для регистрации пользователя без обязательного поля
     *
     * @param json
     */
    public Response registrationRequest(File json) {
         response = given().
                spec(Utils.requestSpec).
                body(json).
                when().
                post(this.endPoint);
        return response;
    }
}