package io.github.msj.productmanagement.service;

import io.github.msj.productmanagement.model.dto.ConfiguracaoCamposDTO;
import io.github.msj.productmanagement.model.entities.ConfiguracaoCampos;
import io.github.msj.productmanagement.repository.ConfiguracaoCamposRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@AllArgsConstructor
public class ConfiguracaoCamposService {

    private final ConfiguracaoCamposRepository configuracaoCamposRepository;

    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public ConfiguracaoCamposDTO obterConfiguracao() {
        ConfiguracaoCampos ultimaConfiguracao = configuracaoCamposRepository.findTopByOrderByIdDesc();

        if (Objects.nonNull(ultimaConfiguracao)) {
            return modelMapper.map(ultimaConfiguracao, ConfiguracaoCamposDTO.class);
        } else {
            return new ConfiguracaoCamposDTO();
        }
    }

    @Transactional
    public ConfiguracaoCamposDTO criarConfiguracao(ConfiguracaoCamposDTO configuracao) {
        ConfiguracaoCampos configuracaoCampos = configuracaoCamposRepository
                .save(modelMapper.map(configuracao, ConfiguracaoCampos.class));
        return modelMapper.map(configuracaoCampos, ConfiguracaoCamposDTO.class);
    }

}
