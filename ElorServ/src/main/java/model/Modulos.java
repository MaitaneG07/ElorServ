package model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "modulos")
public class Modulos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "nombre_eus", length = 200)
    private String nombreEus;

    @Column(name = "horas", nullable = false)
    private Integer horas;

    @Column(name = "ciclo_id", nullable = false)
    private Integer cicloId;

    @Column(name = "curso", nullable = false)
    private Integer curso;

    // Constructores
    public Modulos() {
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreEus() {
        return nombreEus;
    }

    public void setNombreEus(String nombreEus) {
        this.nombreEus = nombreEus;
    }

    public Integer getHoras() {
        return horas;
    }

    public void setHoras(Integer horas) {
        this.horas = horas;
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

	@Override
	public int hashCode() {
		return Objects.hash(cicloId, curso, horas, id, nombre, nombreEus);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Modulos other = (Modulos) obj;
		return Objects.equals(cicloId, other.cicloId) && Objects.equals(curso, other.curso)
				&& Objects.equals(horas, other.horas) && Objects.equals(id, other.id)
				&& Objects.equals(nombre, other.nombre) && Objects.equals(nombreEus, other.nombreEus);
	}

	@Override
	public String toString() {
		return "Modulos [id=" + id + ", nombre=" + nombre + ", nombreEus=" + nombreEus + ", horas=" + horas
				+ ", cicloId=" + cicloId + ", curso=" + curso + "]";
	}
    
    

}
