package io.github.msj.productmanagement.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "configuracao_campos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracaoCampos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<String> camposOcultos;

}
