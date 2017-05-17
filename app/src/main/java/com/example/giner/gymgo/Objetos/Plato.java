package com.example.giner.gymgo.Objetos;

public class Plato {
	
	private int id_plato;
	private String nombre;
	private String peso;

	public int getId_plato() {
		return id_plato;
	}
	public void setId_plato(int id_plato) {
		this.id_plato = id_plato;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPeso() {
		return peso;
	}
	public void setPeso(String peso) {
		this.peso = peso;
	}

	public String toString(){
		return this.getNombre();
	}

}
