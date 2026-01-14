package elorServ.modelo.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.*;

@Entity
@Table(name = "matriculaciones")
public class Matriculaciones implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "alum_id", nullable = false)
    @ManyToOne
    @JoinColumn(name = "alum_id")
    private Users users;

    @Column(name = "ciclo_id", nullable = false)
    @ManyToOne
    @JoinColumn(name = "ciclo_id")
    private Ciclos ciclos;

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

    public Ciclos getCiclos() {
        return ciclos;
    }

    public void setCiclos(Ciclos ciclos) {
        this.ciclos = ciclos;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

}
