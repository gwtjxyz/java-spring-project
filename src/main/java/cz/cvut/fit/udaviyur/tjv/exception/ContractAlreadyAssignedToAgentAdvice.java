package cz.cvut.fit.udaviyur.tjv.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ContractAlreadyAssignedToAgentAdvice {

    @ResponseBody
    @ExceptionHandler(ContractAlreadyAssignedToAgentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String ContractAlreadyAssignedToAgentHandler(ContractAlreadyAssignedToAgentException e) {
        return e.getMessage();
    }
}
