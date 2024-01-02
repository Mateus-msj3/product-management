package io.github.msj.productmanagement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlteracaoCampoDTO {

    private String campo;

    private String valorAnterior;

    private String valorAtual;
}
