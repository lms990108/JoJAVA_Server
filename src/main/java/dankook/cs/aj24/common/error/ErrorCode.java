package dankook.cs.aj24.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    /* 400 BAD_REQUEST : 잘못된 요청 */
    POST_DELETED(HttpStatus.BAD_REQUEST, "삭제된 게시글입니다."),
    REVIEW_DELETED(HttpStatus.BAD_REQUEST, "삭제된 리뷰입니다."),
    COMMENT_DELETED(HttpStatus.BAD_REQUEST, "삭제된 댓글입니다."),
    EMPTY_FILE_EXCEPTION(HttpStatus.BAD_REQUEST, "빈 파일입니다."),
    NO_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "파일 확장자가 없습니다."),  // 스펠링 수정
    INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "허용되지 않는 파일 확장자입니다."),  // 스펠링 수정
    AUTH_CODE_EXTENSION(HttpStatus.BAD_REQUEST, "로그인을 실패하였습니다(임시)"),


    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "권한 정보가 없는 토큰입니다."),
    USER_NOT_AUTHENTICATED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다"),

    /* 403 FORBIDDEN : 권한이 없는 사용자 */
    USER_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "권한이 없는 사용자입니다."),

    /* 404 NOT_FOUND : Resource를 찾을 수 없음 */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 정보의 사용자를 찾을 수 없습니다."),
    OBJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "대상을 찾을 수 없습니다."),

    /* 409 : CONFLICT : Resource의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "데이터가 이미 존재합니다."),

    /* 410 : GONE : 리소스가 더 이상 유효하지 않음 */
    USER_ALREADY_DELETED(HttpStatus.GONE, "탈퇴된 사용자입니다."),

    /* 500 INTERNAL_SERVER_ERROR : 서버 내부 에러 */
    IO_EXCEPTION_ON_IMAGE_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드 중 입출력 오류가 발생했습니다."),
    PUT_OBJECT_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "S3에 객체를 업로드하는 중 예외가 발생했습니다."),
    IO_EXCEPTION_ON_IMAGE_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 삭제 중 입출력 오류가 발생했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
