package com.example.giner.gymgo.Objetos;

import java.util.ArrayList;

public class Usuario {

	private String uid;
	private String nombre;
	private String apellidos;
	private String email;
	private double peso;
	private double altura;
	private int objetivo;
	private ArrayList<Revision_user> revisiones;
	private Rutina_User rutina;
	private Dieta dieta;
	private double pesoIdeal;

	public Usuario() {

	}

	public Usuario(String uid, String nombre, String apellidos, String email, double peso, int altura, int objetivo, ArrayList<Revision_user> revisiones, Rutina_User rutina, Dieta dieta, double pesoIdeal) {
		this.uid = uid;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.peso = peso;
		this.altura = altura;
		this.objetivo = objetivo;
		this.revisiones = revisiones;
		this.rutina = rutina;
		this.dieta = dieta;
		this.pesoIdeal = pesoIdeal;
	}

	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	public double getAltura() {
		return altura;
	}
	public void setAltura(double altura) {
		this.altura = altura;
	}
	public int getObjetivo() {
		return objetivo;
	}
	public void setObjetivo(int objetivo) {
		this.objetivo = objetivo;
	}
	public ArrayList<Revision_user> getRevisiones() {
		return revisiones;
	}
	public void setRevisiones(ArrayList<Revision_user> revisiones) {
		this.revisiones = revisiones;
	}
	public Rutina_User getRutina() {
		return rutina;
	}
	public void setRutina(Rutina_User rutina) {
		this.rutina = rutina;
	}
	public Dieta getDieta() {
		return dieta;
	}
	public void setDieta(Dieta dienta) {
		this.dieta = dienta;
	}
	public double getPesoIdeal() {
		return pesoIdeal;
	}
	public void setPesoIdeal(double pesoIdeal) {
		this.pesoIdeal = pesoIdeal;
	}
}
