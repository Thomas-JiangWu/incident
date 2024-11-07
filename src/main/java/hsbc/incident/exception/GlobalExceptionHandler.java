package hsbc.incident.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import hsbc.incident.common.Constants;
import hsbc.incident.common.Response;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Response<Object> oException(Exception e) {
        return Response.fail(Constants.RESPONSE_CODE_INTERNAL_ERROR, e.getMessage());
    }

    // handle query param validation
    @ExceptionHandler({ConstraintViolationException.class})
    public Response<Object> onMethodArgumentNotValidException(ConstraintViolationException e, HttpServletRequest request) {
        String message = e.getConstraintViolations().stream()
                .map(error -> String.format("%s%s", ((PathImpl) error.getPropertyPath()).getLeafNode(), error.getMessage()))
                .collect(Collectors.joining("; "));
        return Response.fail(Constants.RESPONSE_CODE_INVALID_PARAM, message);
    }

    // handle query param type mismatch
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Object onMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        return Response.fail(Constants.RESPONSE_CODE_INVALID_PARAM,
                String.format("%s=%s does not match with type %s", e.getName(), e.getValue(), e.getParameter().getParameterType().getSimpleName())
        );
    }

    // handle body param validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<Object> onMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("%s%s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining("; "));
        return Response.fail(Constants.RESPONSE_CODE_INVALID_PARAM, message);
    }

    // handle body param type mismatch
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Object onHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        if (e.getCause() instanceof InvalidFormatException) {
            InvalidFormatException cause = (InvalidFormatException) e.getCause();
            return Response.fail(Constants.RESPONSE_CODE_INVALID_PARAM,
                    String.format("%s=%s does not match with type %s",
                            cause.getPath().get(cause.getPath().size() - 1).getFieldName(), cause.getValue(), cause.getTargetType().getSimpleName())
            );
        }
        return Response.fail(Constants.RESPONSE_CODE_INVALID_PARAM, e.getMessage());
    }
}