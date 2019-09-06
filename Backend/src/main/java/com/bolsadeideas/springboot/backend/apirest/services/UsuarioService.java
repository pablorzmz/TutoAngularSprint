package com.bolsadeideas.springboot.backend.apirest.services;

import com.bolsadeideas.springboot.backend.apirest.dao.IUsuarioDao;
import com.bolsadeideas.springboot.backend.apirest.model.entity.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements UserDetailsService {


    private Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private IUsuarioDao usuarioDao;

    @Override
    @Transactional( readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuarioDao.findByUserName(username);

        if (usuario == null) {
            logger.error("Error en el login, no existe el usuario " + username+ " en el sistema.");
            throw  new UsernameNotFoundException("Error en el login, no existe el usuario " + username+ " en el sistema.");
        }

        List<GrantedAuthority> authorities  = usuario.getRoles()
                .stream()
                .map(
                rol -> new SimpleGrantedAuthority( rol.getNombre()))
                // rol -> { return new SimpleGrantedAuthority( rol.getNombre()); })
                .peek( authority -> logger.info( "Role: "+ authority.getAuthority()))
                .collect(Collectors.toList());

        return new User(
                usuario.getUserName(),
                usuario.getPassword(),
                usuario.getEnabled(),
                true,
                true,
                true,
                authorities
                );
    }
}
