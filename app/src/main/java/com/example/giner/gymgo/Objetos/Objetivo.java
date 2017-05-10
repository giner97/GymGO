package com.example.giner.gymgo.Objetos;

public class Objetivo {
	
	private int id_objetivo;
	private String descripcion;
	
	
	public int getId_objetivo() {
		return id_objetivo;
	}
	public void setId_objetivo(int id_objetivo) {
		this.id_objetivo = id_objetivo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String toString(){
		return this.descripcion;
	}
	

}
