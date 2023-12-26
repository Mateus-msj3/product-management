package io.github.msj.productmanagement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracaoCamposDTO {

    private Long id;

    private List<String> camposOcultos;

}
