package com.diegocarvajal.comiccollectorsystem.models;

/**
 * Clase que representa a un usuario del sistema.
 * Contiene información como id, nombre, apellido, email y teléfono.
 */
public class Usuario {
    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;

    public Usuario(String id, String nombre, String apellido, String email, String telefono) {
        // Constructor principal que recibe todos los datos del usuario
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        // Devuelve una representación legible del usuario para mostrar en consola
        return "Id: " + id + ", Nombre: " + nombre + ", Apellido: " + apellido + ", Email: " + email + ", Teléfono: " + telefono;
    }
}
