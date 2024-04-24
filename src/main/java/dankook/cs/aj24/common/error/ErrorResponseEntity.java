package dankook.cs.aj24.common.error;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

// 에러 객체 클래스
@Data
@Builder // 객체 생성 패턴인 Builder 패턴을 적용
public class ErrorResponseEntity {
    private int status;
    private String code;
    private String message;

    // ErrorCode 객체를 받아 상태 코드, 에러 코드, 에러 메시지를 포함하는 ErrorResponseEntity 객체를 ResponseEntity로 감싸서 반환
    public static ResponseEntity<ErrorResponseEntity> toResponseEntity(ErrorCode e){
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ErrorResponseEntity.builder()
                        .status(e.getHttpStatus().value())
                        .code(e.name())
                        .message(e.getMessage())
                        .build()
                );
    }
}