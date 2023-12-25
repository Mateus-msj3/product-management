package io.github.msj.productmanagement.exception;

import org.hibernate.service.spi.ServiceException;

import java.util.Arrays;
import java.util.List;

public class ValidacaoErro {

    private List<String> errors;

    public ValidacaoErro(List<String> errors) {
        this.errors = errors;
    }

    public ValidacaoErro(String message) {
        this.errors = Arrays.asList(message);
    }

    public ValidacaoErro(ServiceException exception) {
        this.errors = Arrays.asList(exception.getMessage());
    }

    public List<String> getErrors() {
        return errors;
    }
}
