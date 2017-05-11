package com.example.giner.gymgo.Objetos;

import java.util.ArrayList;

public class Rutina {

	private int id_rutina;
	private String descripcion_rutina;
	private int cantidad_dias;
	private int objetivo;
	private int id_usuario;
	private ArrayList<Rutina_Ejercicio>ejercicios;
	
	public int getId_rutina() {
		return id_rutina;
	}
	public void setId_rutina(int id_rutina) {
		this.id_rutina = id_rutina;
	}
	public String getDescripcion_rutina() {
		return descripcion_rutina;
	}
	public void setDescripcion_rutina(String descripcion_rutina) {
		this.descripcion_rutina = descripcion_rutina;
	}
	public int getCantidad_dias() {
		return cantidad_dias;
	}
	public void setCantidad_dias(int cantidad_dias) {
		this.cantidad_dias = cantidad_dias;
	}
	public int getObjetivo() {
		return objetivo;
	}
	public void setObjetivo(int objetivo) {
		this.objetivo = objetivo;
	}
	public int getId_usuario() {
		return id_usuario;
	}
	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}
	public ArrayList<Rutina_Ejercicio> getEjercicios() {
		return ejercicios;
	}
	public void setEjercicios(ArrayList<Rutina_Ejercicio> ejercicios) {
		this.ejercicios = ejercicios;
	}

	public String toString(){
		return "Rutina "+this.id_rutina;
	}

}
