package com.bolsadeideas.springboot.backend.apirest.controllers;

import com.bolsadeideas.springboot.backend.apirest.model.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.services.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    @GetMapping("/clientes/page/{page}")
    public Page<Cliente> index(@PathVariable int page) {
        return this.clienteService.findAll( PageRequest.of(page, 5));
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
    public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult result)
    {
        Cliente clienteNuevo = null;
        Map<String,Object> response = new HashMap<>();

        // Errores de validación
        if (result.hasErrors()){

            /*
            // Forma vieja de hacerlo
            List<String> errors = new ArrayList<>();
            for (FieldError err: result.getFieldErrors()) {
                errors.add( "El campo '" +  err.getField() + "' " + err.getDefaultMessage());
            }
            */

            List<String> errors = result.getFieldErrors().stream().map(
                    e -> "El campo '" + e.getField() + "' " + e.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }

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
    public ResponseEntity<?> update(@Valid @RequestBody Cliente cliente, BindingResult result, @PathVariable Long id) {

        Cliente clienteActual = null;
        Cliente clienteActualizado = null;
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()){
            // Forma vieja de hacerlo
            List<String> errors = new ArrayList<>();
            for (FieldError err: result.getFieldErrors()) {
                errors.add( "El campo '" +  err.getField() + "' " + err.getDefaultMessage());
                System.out.println("\n\n\nEl campo '" +  err.getField() + "' " + err.getDefaultMessage());
            }
            response.put("errors", errors);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }else{
            System.out.println("\n\n\n No hay errores");
        }

        clienteActual = clienteService.findById(id);

        if (clienteActual == null){
            System.out.println("\n\n\n Cliente nulll");
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
            System.out.println("Error al actualizar el cliente en la base de datos.");
            response.put("mensaje","Error al actualizar el cliente en la base de datos.");
            response.put("error",ex.getMessage().concat(": ".concat(ex.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","Cliente actualizado con éxito!");
        response.put("cliente",clienteActualizado);
        System.out.println("Cliente actualizado con éxito!");
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
