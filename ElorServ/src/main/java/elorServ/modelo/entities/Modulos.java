package elorServ.modelo.entities;

import jakarta.persistence.*;
import java.io.Serializable;

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

//    @Column(name = "ciclo_id", nullable = false)
    @ManyToOne
    @JoinColumn(name = "ciclo_id")
    private Ciclos ciclos;

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

    public Integer getCurso() {
        return curso;
    }

    public void setCurso(Integer curso) {
        this.curso = curso;
    }

    public Ciclos getCiclos() {
        return ciclos;
    }

    public void setCiclos(Ciclos ciclos) {
        this.ciclos = ciclos;
    }

}
