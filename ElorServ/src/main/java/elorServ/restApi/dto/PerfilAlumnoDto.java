package elorServ.restApi.dto;

import java.time.LocalDate;

public class PerfilAlumnoDto {
    private Integer userId;
    private String nombre;
    private String apellidos;
    private String email;

    private Integer cicloId;
    private String cicloNombre;
    private Integer curso;
    private LocalDate fechaMatricula;

    public PerfilAlumnoDto(Integer userId, String nombre, String apellidos, String email,
                           Integer cicloId, String cicloNombre, Integer curso, LocalDate fechaMatricula) {
        this.userId = userId;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.cicloId = cicloId;
        this.cicloNombre = cicloNombre;
        this.curso = curso;
        this.fechaMatricula = fechaMatricula;
    }

    public Integer getUserId() { return userId; }
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public String getEmail() { return email; }
    public Integer getCicloId() { return cicloId; }
    public String getCicloNombre() { return cicloNombre; }
    public Integer getCurso() { return curso; }
    public LocalDate getFechaMatricula() { return fechaMatricula; }
}
