package com.example.giner.gymgo.Objetos;

public class Revision_user {
	
	private int id_revision;
	private double peso_revision;
	private double altura_revision;
	private double imc_revision;
	private String fecha_revision;

	public Revision_user() {
	}

	public Revision_user(int id_revision, double peso_revision, double altura_revision, double imc_revision, String fecha_revision) {
		this.id_revision = id_revision;
		this.peso_revision = peso_revision;
		this.altura_revision = altura_revision;
		this.imc_revision = imc_revision;
		this.fecha_revision = fecha_revision;
	}

	public int getId_revision() {
		return id_revision;
	}
	public void setId_revision(int id_revision) {
		this.id_revision = id_revision;
	}
	public double getPeso_revision() {
		return peso_revision;
	}
	public void setPeso_revision(double peso_revision) {
		this.peso_revision = peso_revision;
	}
	public double getAltura_revision() {
		return altura_revision;
	}
	public void setAltura_revision(double altura_revision) {
		this.altura_revision = altura_revision;
	}
	public double getImc_revision() {
		return imc_revision;
	}
	public void setImc_revision(double imc_revision) {
		this.imc_revision = imc_revision;
	}
	public String getFecha_revision() {
		return fecha_revision;
	}
	public void setFecha_revision(String fecha_revision) {
		this.fecha_revision = fecha_revision;
	}

}
