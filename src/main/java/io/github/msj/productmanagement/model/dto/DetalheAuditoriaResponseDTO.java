package io.github.msj.productmanagement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalheAuditoriaResponseDTO {

    private List<AlteracaoCampoDTO> camposAlterados = new ArrayList<>();
}
