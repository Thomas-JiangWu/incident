package hsbc.incident.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response<T> {
    private Integer statusCode;
    private String message;
    private T data;

    public static Response<Object> fail(Integer statusCode, String message) {
        return new Response<>(statusCode, message, null);
    }

    public static <T> Response<T> success(T data) {
        return new Response<T>(Constants.RESPONSE_CODE_SUCCESS, "", data);
    }
}