package com.example.giner.gymgo.Objetos;

public class Usuario {

	private String uid;
	private String nombre;
	private String apellidos;
	private String email;
	private double peso;
	private int altura;
	private int objetivo;
	private Revision_user revision;
	private Rutina_User rutina;
	private int dieta;

	public Usuario() {

	}

	public Usuario(String uid, String nombre, String apellidos, String email, double peso, int altura, int objetivo, Revision_user revision, Rutina_User rutina, int dieta) {
		this.uid = uid;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.peso = peso;
		this.altura = altura;
		this.objetivo = objetivo;
		this.revision = revision;
		this.rutina = rutina;
		this.dieta = dieta;
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
	public int getAltura() {
		return altura;
	}
	public void setAltura(int altura) {
		this.altura = altura;
	}
	public int getObjetivo() {
		return objetivo;
	}
	public void setObjetivo(int objetivo) {
		this.objetivo = objetivo;
	}
	public Revision_user getRevision() {
		return revision;
	}
	public void setRevision(Revision_user revision) {
		this.revision = revision;
	}
	public Rutina_User getRutina() {
		return rutina;
	}
	public void setRutina(Rutina_User rutina) {
		this.rutina = rutina;
	}
	public int getDieta() {
		return dieta;
	}
	public void setDieta(int dienta) {
		this.dieta = dienta;
	}
	
	
	
}
