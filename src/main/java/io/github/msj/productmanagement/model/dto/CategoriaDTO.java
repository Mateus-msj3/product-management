package io.github.msj.productmanagement.model.dto;

import io.github.msj.productmanagement.model.enums.TipoCategoria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaDTO implements Serializable {

    private Long id;

    private String nome;

    private Boolean ativo;

    private TipoCategoria tipo;
}