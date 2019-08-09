package com.bolsadeideas.springboot.backend.apirest.model.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "No puede ser vacío")
    @Size(min = 4, max = 12, message = "El tamaño debe ser entre 4 y 12 caracteres.")
    @Column(nullable = false)
    private String nombre;

    @NotEmpty(message = "No puede ser vacío")
    private String apellido;

    @NotEmpty(message = "No puede ser vacío")
    @Email(message = "No es una dirección de correo válida.")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "create_at")
    @Temporal(TemporalType.DATE)
    private Date createdAt;

    public Long getId() {
        return id;
    }

    @PrePersist
    public void prePersist(){
        createdAt = new Date();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    private static final long serialVersionUID = -2577117508487543501L;
}
