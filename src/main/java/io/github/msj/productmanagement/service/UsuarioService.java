package io.github.msj.productmanagement.service;

import io.github.msj.productmanagement.repository.UserRepository;
import io.github.msj.productmanagement.security.user.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    public UserDetailsImpl obterUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return (UserDetailsImpl) authentication.getPrincipal();
        }

        return null;
    }

    public boolean isEstoquista() {
        UserDetailsImpl userDetails = obterUsuarioLogado();
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            if ("ROLE_ESTOQUISTA".equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }

}
