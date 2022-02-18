package cz.cvut.fit.udaviyur.tjv.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ClientAlreadyPresentAdvice {

    @ResponseBody
    @ExceptionHandler(ClientAlreadyPresentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String AgentAlreadyPresentHandler(ClientAlreadyPresentException e) {
        return e.getMessage();
    }
}