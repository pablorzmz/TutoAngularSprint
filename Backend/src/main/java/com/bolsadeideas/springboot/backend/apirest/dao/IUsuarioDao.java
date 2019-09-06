package com.bolsadeideas.springboot.backend.apirest.dao;

import com.bolsadeideas.springboot.backend.apirest.model.entity.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {

    public Usuario findByUserName(String username);
}
