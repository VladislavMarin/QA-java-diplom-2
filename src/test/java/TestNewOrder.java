import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.Utils;
import utils.user_reg_and_login_response_deserialization.ResponseFinalDeserializationClass;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static utils.Utils.API_AUTH_LOGIN;
import static utils.Utils.requestSpec;

/**
 * Класс для тестирования создания заказа
 */
public class TestNewOrder {

    //Ручка для создания заказа
    String endPoint = Utils.API_ORDERS;
    //Тело запроса для создания заказа
    File json =
            new File("src/test/java/resources/new_order/NewOrderBody.json");
    //Тело запроса без ингредиентов для создания заказа
    File jsonWithoutIngredient =
            new File("src/test/java/resources/new_order/NewOrderBodyWithoutIngredient.json");
    //Тело запроса с игредиентами для создания заказа
    File jsonWithIngredients =
            new File("src/test/java/resources/new_order/NewOrderBodyWithIngredients.json");
    //Пустое тело в запросе
    File jsonWithNullBody =
            new File("src/test/java/resources/new_order/NewOrderNullBody.json");
    //Тело запроса с невалидным хэшем
    File jsonWithNoValidHash =
            new File("src/test/java/resources/new_order/NewOrderNoValidHash.json");

    Response response;

    @After
    public void tearDown() {
        Utils.deleteUser();
    }

    @Before
    public void setUp() {
        Utils.createNewUser();
    }

    /**
     * Тест на создание заказа без авторизации
     */
    @Test
    public void testNewOrderWithoutAuth() {

        File json = new File("src/test/java/resources/new_order/NewOrderBody.json");

        Response response = given().
                spec(requestSpec).
                body(json).
                when().
                post(endPoint);

        response.then().assertThat().
                body("success", equalTo(true)).
                and().
                statusCode(200);
    }

    /**
     * Тест на создание заказа с авторизацией
     */
    @Test
    public void testNewOrderWithAuth() {

        newOrder(json);

        response.then().assertThat().
                body("success", equalTo(true)).
                and().
                statusCode(200);
    }

    /**
     * Тест на создание заказа без авторизации
     */
    @Test
    public void testNewOrderWithoutIngredients() {
        newOrder(jsonWithoutIngredient);

        response.then().assertThat().
                body("success", equalTo(false)).
                and().
                statusCode(400);

    }

    /**
     * Тест на создание заказа с ингредиентами
     */
    @Test
    public void testNewOrderWithIngredients() {
        newOrder(jsonWithIngredients);

        response.then().assertThat().
                body("success", equalTo(true)).
                and().
                statusCode(200);
    }

    @Test
    public void testNewOrderWithNullBody() {
        newOrder(jsonWithNullBody);

        response.then().
                statusCode(400);
    }

    @Test
    public void testNewOrderWithNoValidHash() {
        newOrder(jsonWithNoValidHash);

        response.then().
                statusCode(500);
    }

    /**
     * Метод для запроса на создание заказа
     *
     * @param json тело запроса
     * @return ответ для проверок
     */
    public Response newOrder(File json) {

        File jsonFromAuth = new File("src/test/java/resources/login/UserLoginValidData.json");

        ResponseFinalDeserializationClass responseAuth = given().
                spec(requestSpec).
                body(jsonFromAuth).
                post(API_AUTH_LOGIN).
                body().
                as(ResponseFinalDeserializationClass.class);

        response = given().
                spec(requestSpec).
                header("Authorization", responseAuth.getAccessToken()).
                body(json).
                when().
                post(endPoint);

        return response;
    }
}
