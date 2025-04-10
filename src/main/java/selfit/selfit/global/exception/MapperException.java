package selfit.selfit.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MapperException extends Exception {
    private final ErrorCode errorCode;
}
