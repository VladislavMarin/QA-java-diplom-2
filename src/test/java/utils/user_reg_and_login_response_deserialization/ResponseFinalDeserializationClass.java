package utils.user_reg_and_login_response_deserialization;

public class ResponseFinalDeserializationClass {

    private boolean success;

    private ResponseUserDeserializationClass user;

    private String accessToken;

    private String refreshToken;

    public ResponseUserDeserializationClass getUser() {
        return user;
    }

    public void setUser(ResponseUserDeserializationClass user) {
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
