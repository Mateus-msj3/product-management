package io.github.msj.productmanagement.config;

import io.github.msj.productmanagement.model.entities.Auditoria;
import io.github.msj.productmanagement.security.user.UserDetailsImpl;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditoriaRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {

        String usuarioLogado = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(UserDetailsImpl.class::cast)
                .map(UserDetailsImpl::getName)
                .orElse("Usuário desconhecido");

        Auditoria auditoria = (Auditoria) revisionEntity;
        auditoria.setUsuario(usuarioLogado);

    }
}