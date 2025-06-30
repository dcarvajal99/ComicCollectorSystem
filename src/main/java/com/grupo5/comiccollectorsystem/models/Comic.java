package com.grupo5.comiccollectorsystem.models;

public class Comic {
    private String id;
    private String titulo;
    private String autor;
    private Boolean estado;
    private String asignadoA; // email del usuario al que se asigna el comic

    public Comic(String titulo, String autor, Boolean estado) {
        this.titulo = titulo;
        this.autor = autor;
        this.estado = estado;
        this.asignadoA = null;
    }

    public Comic(String titulo, String autor, Boolean estado, String asignadoA) {
        this.titulo = titulo;
        this.autor = autor;
        this.estado = estado;
        this.asignadoA = asignadoA;
    }

    public Comic(String id, String titulo, String autor, Boolean estado, String asignadoA) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.estado = estado;
        this.asignadoA = asignadoA;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getAsignadoA() {
        return asignadoA;
    }

    public void setAsignadoA(String asignadoA) {
        this.asignadoA = asignadoA;
    }

    @Override
    public String toString() {
        String estadoStr = estado ? "Disponible" : "Prestado a: " + (asignadoA != null ? asignadoA : "-");
        return "Id: " + id + ", Título: " + titulo + ", Autor: " + autor + ", Estado: " + estadoStr;
    }
}
