package com.qnecesitas.tiendadcero_v10;

public class Modelo_EditarProductos {

    private String nombre;
    private int precio;
    private boolean estado;
    private int id;
    private String descripcion;

    public Modelo_EditarProductos(String nombre, int precio, boolean estado, int id, String descripcion) {
        this.nombre = nombre;
        this.precio = precio;
        this.estado = estado;
        this.id = id;
        this.descripcion = descripcion;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
