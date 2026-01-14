package model;

import jakarta.persistence.*;
import java.io.Serializable;

import java.time.*;
import java.util.Objects;

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

    @Column(name = "profesor_id")
    private Integer profesorId;

    @Column(name = "alumno_id")
    private Integer alumnoId;

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

    public Integer getProfesorId() {
        return profesorId;
    }

    public void setProfesorId(Integer profesorId) {
        this.profesorId = profesorId;
    }

    public Integer getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(Integer alumnoId) {
        this.alumnoId = alumnoId;
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

	@Override
	public int hashCode() {
		return Objects.hash(alumnoId, asunto, aula, createdAt, estado, estadoEus, fecha, idCentro, idReunion,
				profesorId, titulo, updatedAt);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reuniones other = (Reuniones) obj;
		return Objects.equals(alumnoId, other.alumnoId) && Objects.equals(asunto, other.asunto)
				&& Objects.equals(aula, other.aula) && Objects.equals(createdAt, other.createdAt)
				&& Objects.equals(estado, other.estado) && Objects.equals(estadoEus, other.estadoEus)
				&& Objects.equals(fecha, other.fecha) && Objects.equals(idCentro, other.idCentro)
				&& Objects.equals(idReunion, other.idReunion) && Objects.equals(profesorId, other.profesorId)
				&& Objects.equals(titulo, other.titulo) && Objects.equals(updatedAt, other.updatedAt);
	}

	@Override
	public String toString() {
		return "Reuniones [idReunion=" + idReunion + ", estado=" + estado + ", estadoEus=" + estadoEus + ", profesorId="
				+ profesorId + ", alumnoId=" + alumnoId + ", idCentro=" + idCentro + ", titulo=" + titulo + ", asunto="
				+ asunto + ", aula=" + aula + ", fecha=" + fecha + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + "]";
	}
    
    

}
