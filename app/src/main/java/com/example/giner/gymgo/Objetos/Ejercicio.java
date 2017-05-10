package com.example.giner.gymgo.Objetos;

public class Ejercicio {
	private int id_ejercicio;
	private String nombreEjercicio;
	private String descripcion;
	private String explicacion;
	private String img;
	private int grupo_muscular;
	
	public int getId_ejercicio() {
		return id_ejercicio;
	}
	public void setId_ejercicio(int id_ejercicio) {
		this.id_ejercicio = id_ejercicio;
	}
	public String getNombreEjercicio() {
		return nombreEjercicio;
	}
	public void setNombreEjercicio(String nombreEjercicio) {
		this.nombreEjercicio = nombreEjercicio;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getExplicacion() {
		return explicacion;
	}
	public void setExplicacion(String explicacion) {
		this.explicacion = explicacion;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public int getGrupo_muscular() {
		return grupo_muscular;
	}
	public void setGrupo_muscular(int grupo_muscular) {
		this.grupo_muscular = grupo_muscular;
	}

}
