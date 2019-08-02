package com.bolsadeideas.springboot.backend.apirest.controllers;

import com.bolsadeideas.springboot.backend.apirest.model.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.services.IClienteService;
import net.bytebuddy.description.type.TypeDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class ClienteRestController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping("/clientes")
    public List<Cliente> index()
    {
       return clienteService.findAll();
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<?> show(@PathVariable Long id)
    {
        Cliente cliente = null;
        Map<String,Object> respose = new HashMap<>();
        try {
            cliente = clienteService.findById(id);
        }catch (DataAccessException ex){
            respose.put("mensaje","Error al realizar la consulta en la base de datos.");
            respose.put("error",ex.getMessage().concat(": ".concat(ex.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String,Object>>(respose,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (cliente == null){
            respose.put("mensaje","El cliente con id ".concat(id.toString().concat(" no existe.")));
            return new ResponseEntity<Map<String,Object>>(respose,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Cliente>(cliente,HttpStatus.OK);
    }

    @PostMapping("/clientes")
    public ResponseEntity<?> create(@RequestBody Cliente cliente)
    {
        Cliente clienteNuevo = null;
        Map<String,Object> response = new HashMap<>();
        try{
            clienteNuevo = clienteService.save(cliente);
        }catch (DataAccessException ex){
            response.put("mensaje","Error al realizar el insert en la base de datos.");
            response.put("error",ex.getMessage().concat(": ".concat(ex.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","Cliente creado con éxito!");
        response.put("cliente",clienteNuevo);
        return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/clientes/{id}")
    public ResponseEntity<?> update(@RequestBody  Cliente cliente, @PathVariable Long id )
    {
        Cliente clienteActual = null;
        Cliente clienteActualizado = null;
        Map<String, Object> response = new HashMap<>();

        clienteActual = clienteService.findById(id);

        if (clienteActual == null){
            response.put("mensaje","Error: no se pudo editar. El cliente con id ".concat(id.toString().concat(" no existe.")));
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
        }

        try {
            clienteActual.setApellido(cliente.getApellido());
            clienteActual.setNombre(cliente.getNombre());
            clienteActual.setEmail(cliente.getEmail());

            // Save también hace la actualización (operación merge)
            clienteActualizado = clienteService.save(clienteActual);

        }catch (DataAccessException ex){
            response.put("mensaje","Error al actualizar el cliente en la base de datos.");
            response.put("error",ex.getMessage().concat(": ".concat(ex.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","Cliente actualizado con éxito!");
        response.put("cliente",clienteActualizado);
        return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id)
    {
        Map<String, Object> response = new HashMap<>();

        try{
            // Spring crud repository ya valida que el id sea valido, Si no lo es, se cae
            clienteService.delete(id);
        }catch (DataAccessException ex)
        {
            response.put("mensaje","Error al eliminar el cliente en la base de datos.");
            response.put("error",ex.getMessage().concat(": ".concat(ex.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje","Cliente eliminado con éxito!");
        return new ResponseEntity<Map<String, Object>>(response,HttpStatus.OK);
    }
}
