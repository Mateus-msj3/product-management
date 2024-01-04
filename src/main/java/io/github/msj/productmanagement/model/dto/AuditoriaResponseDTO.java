package io.github.msj.productmanagement.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class AuditoriaResponseDTO {
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "GMT-3")
    private Date data;

    private String operacaoRealizada;

    private String usuario;

    private ProdutoResponseDTO produto;

}
