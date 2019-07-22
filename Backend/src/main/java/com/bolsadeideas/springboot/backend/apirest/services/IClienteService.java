package com.bolsadeideas.springboot.backend.apirest.services;

import com.bolsadeideas.springboot.backend.apirest.model.entity.Cliente;

import java.util.List;

public interface IClienteService {

    public List<Cliente> findAll();
    public Cliente findById(Long id);
    public Cliente save(Cliente cliente);
    public void delete(Long id);

}
