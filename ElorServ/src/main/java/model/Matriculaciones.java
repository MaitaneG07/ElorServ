package model;

import jakarta.persistence.*;
import java.io.Serializable;

import java.time.*;
import java.util.Objects;

@Entity
@Table(name = "matriculaciones")
public class Matriculaciones implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "alum_id", nullable = false)
    private Integer alumId;

    @Column(name = "ciclo_id", nullable = false)
    private Integer cicloId;

    @Column(name = "curso", nullable = false)
    private Integer curso;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    // Constructores
    public Matriculaciones() {
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAlumId() {
        return alumId;
    }

    public void setAlumId(Integer alumId) {
        this.alumId = alumId;
    }

    public Integer getCicloId() {
        return cicloId;
    }

    public void setCicloId(Integer cicloId) {
        this.cicloId = cicloId;
    }

    public Integer getCurso() {
        return curso;
    }

    public void setCurso(Integer curso) {
        this.curso = curso;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

	@Override
	public int hashCode() {
		return Objects.hash(alumId, cicloId, curso, fecha, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Matriculaciones other = (Matriculaciones) obj;
		return Objects.equals(alumId, other.alumId) && Objects.equals(cicloId, other.cicloId)
				&& Objects.equals(curso, other.curso) && Objects.equals(fecha, other.fecha)
				&& Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Matriculaciones [id=" + id + ", alumId=" + alumId + ", cicloId=" + cicloId + ", curso=" + curso
				+ ", fecha=" + fecha + "]";
	}
    
    

}
