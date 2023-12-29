package io.github.msj.productmanagement.service.util;

import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class OrdenacaoUtil {

    public static Sort.Direction obterCriterioOrdenacao(String criterio) {
        if ("desc".equals(criterio)) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }

    public static List<Sort.Order> construirCriteriosDeOrdenacao(String[] parametrosOrdenacao) {
        List<Sort.Order> orders = new ArrayList<>();

        if (parametrosOrdenacao[0].contains(",")) {
            for (String campoOrdenacao : parametrosOrdenacao) {
                String[] campo = campoOrdenacao.split(",");
                orders.add(new Sort.Order(obterCriterioOrdenacao(campo[1]), campo[0]));
            }
        } else {
            orders.add(new Sort.Order(obterCriterioOrdenacao(parametrosOrdenacao[1]), parametrosOrdenacao[0]));
        }

        return orders;
    }
}
