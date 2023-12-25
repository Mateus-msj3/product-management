package io.github.msj.productmanagement.controller.handler;

import io.github.msj.productmanagement.exception.MensagemErro;
import io.github.msj.productmanagement.exception.NotFoundException;
import io.github.msj.productmanagement.exception.ValidacaoErro;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionAdvice.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidacaoErro handleValidationErrors(MethodArgumentNotValidException exception) {
        logException(exception);
        BindingResult bindingResult = exception.getBindingResult();
        List<String> messages = bindingResult.getAllErrors().stream()
                .map(objectError -> objectError.getDefaultMessage())
                .collect(Collectors.toList());
        return new ValidacaoErro(messages);
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidacaoErro handleRuleBusinessException(ServiceException exception) {
        logException(exception);
        return new ValidacaoErro(exception);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<MensagemErro> resourceNotFoundException(NotFoundException ex, WebRequest request) {
        logException(ex);
        MensagemErro mensagemErro = new MensagemErro(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<MensagemErro>(mensagemErro, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MensagemErro> globalExceptionHandler(Exception ex, WebRequest request) {
        logException(ex);
        MensagemErro mensagemErro = new MensagemErro(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<MensagemErro>(mensagemErro, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void logException(Exception exception) {
        logger.error("Exceção detectada:", exception);
    }

}
