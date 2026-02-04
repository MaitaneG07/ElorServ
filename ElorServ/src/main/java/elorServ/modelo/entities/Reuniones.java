package elorServ.modelo.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.*;

@Entity
@Table(name = "reuniones")
public class Reuniones implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_reunion")
	private Integer idReunion;

	@Column(name = "estado", length = 9)
	private String estado;

	@Column(name = "estado_eus", length = 10)
	private String estadoEus;

//    @Column(name = "profesor_id")
	@ManyToOne
	@JoinColumn(name = "profesor_id")
	private Users profesor;

//    @Column(name = "alumno_id")
	@ManyToOne
	@JoinColumn(name = "alumno_id")
	private Users alumno;

	@Column(name = "id_centro", length = 20)
	private String idCentro;

	@Column(name = "titulo", length = 150)
	private String titulo;

	@Column(name = "asunto", length = 200)
	private String asunto;

	@Column(name = "aula", length = 20)
	private String aula;

	@Column(name = "fecha")
	private LocalDateTime fecha;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	// Constructores
	public Reuniones() {
		
	}
	
	public Reuniones(String estado, String titulo, String asunto, String aula, Integer idAlumno, Integer idProfesor,
			LocalDateTime fechaHora, Integer idCentro) {

		this.estado = estado;
		this.titulo = titulo;
		this.asunto = asunto;
		this.aula = aula;
		this.fecha = fechaHora;
		this.idCentro = String.valueOf(idCentro);
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();

		Users profesor = new Users();
		profesor.setId(idProfesor);
		this.profesor = profesor;

		Users alumno = new Users();
		alumno.setId(idAlumno);
		this.alumno = alumno;
	}

	// Getters y Setters
	public Integer getIdReunion() {
		return idReunion;
	}

	public void setIdReunion(Integer idReunion) {
		this.idReunion = idReunion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEstadoEus() {
		return estadoEus;
	}

	public void setEstadoEus(String estadoEus) {
		this.estadoEus = estadoEus;
	}

	public String getIdCentro() {
		return idCentro;
	}

	public void setIdCentro(String idCentro) {
		this.idCentro = idCentro;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getAula() {
		return aula;
	}

	public void setAula(String aula) {
		this.aula = aula;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Users getProfesores() {
		return profesor;
	}

	public void setProfesores(Users profesor) {
		this.profesor = profesor;
	}

	public Users getAlumnos() {
		return alumno;
	}

	public void setAlumnos(Users alumno) {
		this.alumno = alumno;
	}

}
