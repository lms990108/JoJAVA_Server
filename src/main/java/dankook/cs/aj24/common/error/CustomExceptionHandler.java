package dankook.cs.aj24.common.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // 모든 컨트롤러에 대한 전역 예외 처리기를 제공하는 어노테이션
public class CustomExceptionHandler {
    @ExceptionHandler(CustomException.class) // CustomException이 발생할 경우 이 메서드가 처리합니다.
    protected ResponseEntity<ErrorResponseEntity> handleCustomException(CustomException e) {
        // 예외로부터 ErrorCode를 추출하여 ErrorResponseEntity로 변환한 후, ResponseEntity로 반환합니다.
        return ErrorResponseEntity.toResponseEntity(e.getErrorCode());
    }
}
