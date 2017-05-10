package com.example.giner.gymgo.Objetos;

public class Dieta {


	private int id_dieta;
	private int calorias_totales;
	private String descripcion;
	private int objetivo;
	
	public int getId_dieta() {
		return id_dieta;
	}
	public void setId_dieta(int id_dieta) {
		this.id_dieta = id_dieta;
	}
	public int getCalorias_totales() {
		return calorias_totales;
	}
	public void setCalorias_totales(int calorias_totales) {
		this.calorias_totales = calorias_totales;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getObjetivo() {
		return objetivo;
	}
	public void setObjetivo(int objetivo) {
		this.objetivo = objetivo;
	}

}
