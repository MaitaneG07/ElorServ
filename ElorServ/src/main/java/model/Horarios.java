package model;

import jakarta.persistence.*;
import java.io.Serializable;

import java.time.*;
import java.util.Objects;

@Entity
@Table(name = "horarios")
public class Horarios implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "dia", nullable = false, length = 9)
    private String dia;

    @Column(name = "hora", nullable = false)
    private Integer hora;

    @Column(name = "profe_id", nullable = false)
    @ManyToOne
    @JoinColumn(name = "profe_id")
    private Users users;

    @Column(name = "modulo_id", nullable = false)
    @ManyToOne
    @JoinColumn(name = "modulo_id")
    private Modulos modulos;

    @Column(name = "aula", length = 50)
    private String aula;

    @Column(name = "observaciones", length = 255)
    private String observaciones;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructores
    public Horarios() {
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public Integer getHora() {
        return hora;
    }

    public void setHora(Integer hora) {
        this.hora = hora;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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

    public Modulos getModulos() {
        return modulos;
    }

    public void setModulos(Modulos modulos) {
        this.modulos = modulos;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

	@Override
	public int hashCode() {
		return Objects.hash(aula, createdAt, dia, hora, id, modulos, observaciones, updatedAt, users);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Horarios other = (Horarios) obj;
		return Objects.equals(aula, other.aula) && Objects.equals(createdAt, other.createdAt)
				&& Objects.equals(dia, other.dia) && Objects.equals(hora, other.hora) && Objects.equals(id, other.id)
				&& Objects.equals(modulos, other.modulos) && Objects.equals(observaciones, other.observaciones)
				&& Objects.equals(updatedAt, other.updatedAt) && Objects.equals(users, other.users);
	}

	@Override
	public String toString() {
		return "Horarios [id=" + id + ", dia=" + dia + ", hora=" + hora + ", users=" + users + ", modulos=" + modulos
				+ ", aula=" + aula + ", observaciones=" + observaciones + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + "]";
	}
    
    

}
