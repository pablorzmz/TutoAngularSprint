package com.bolsadeideas.springboot.backend.apirest.dao;

import com.bolsadeideas.springboot.backend.apirest.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IClienteDao extends JpaRepository<Cliente,Long> {

}
