package com.example.giner.gymgo.Objetos;

public class Revision {
	
	private int id_revision;
	private double peso_ideal;
	private double imc_min;
	private double imc_max;
	private int id_usuario;
	
	
	public int getId_revision() {
		return id_revision;
	}
	public void setId_revision(int id_revision) {
		this.id_revision = id_revision;
	}
	public double getPeso_ideal() {
		return peso_ideal;
	}
	public void setPeso_ideal(double peso_ideal) {
		this.peso_ideal = peso_ideal;
	}
	public double getImc_min() {
		return imc_min;
	}
	public void setImc_min(double imc_min) {
		this.imc_min = imc_min;
	}
	public double getImc_max() {
		return imc_max;
	}
	public void setImc_max(double imc_max) {
		this.imc_max = imc_max;
	}
	public int getId_usuario() {
		return id_usuario;
	}
	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}
	

}
