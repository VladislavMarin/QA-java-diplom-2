import io.restassured.response.Response;
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
 * Класс для тестирования получения заказа отдельного пользователя
 * Класс наследуется от TestNewOrder тк для проверок потребуется создавать
 */

public class TestGetOrder extends TestNewOrder{

    //РУчка для создания заказов
    String endPoint = Utils.API_ORDERS;

    @Override
    @Before
    public void setUp() {
        super.setUp();

        newOrder(json);

        response.then().assertThat().
                body("success", equalTo(true)).
                and().
                statusCode(200);
    }

    /**
     * Тест для получения заказов для определенного пользователя с авторизацией
     */
    @Test
    public void testGetOrderWithAuthorization() {


        File jsonFromAuth = new File("src/test/java/resources/login/UserLoginValidData.json");

        ResponseFinalDeserializationClass responseAuth = given().
                spec(requestSpec).
                body(jsonFromAuth).
                post(API_AUTH_LOGIN).
                body().
                as(ResponseFinalDeserializationClass.class);

        Response responseUserGetOrder = given().
                spec(requestSpec).
                header("Authorization", responseAuth.getAccessToken()).
                when().
                get(endPoint);

        responseUserGetOrder.then().statusCode(200);
    }
    /**
     * Тест для получения заказов для определенного пользователя без авторизации
     */
    @Test
    public void testGetOrderWithoutAuthorization() {
        Response responseUserGetOrder = given().
                spec(requestSpec).
                when().
                get(endPoint);

        responseUserGetOrder.then().
                body("success", equalTo(false)).
                and().
                statusCode(401);
    }
}
