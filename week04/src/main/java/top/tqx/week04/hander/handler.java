package top.tqx.week04.hander;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.tqx.week04.common.Result;
import top.tqx.week04.exception.BusinessException;

import java.util.StringJoiner;

@RestControllerAdvice
public class handler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handlerValidException(MethodArgumentNotValidException e){
        StringJoiner sj = new StringJoiner("；");
        for (FieldError error : e.getBindingResult().getFieldErrors()){
            sj.add(error.getDefaultMessage());
        }
        return Result.error("400",sj.toString());
    }

    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        return Result.error("500", "服务器异常，请稍后重试");
    }
}
