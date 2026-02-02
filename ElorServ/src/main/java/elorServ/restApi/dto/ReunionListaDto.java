package elorServ.restApi.dto;

import java.time.LocalDateTime;

public class ReunionListaDto {

    private Integer id;
    private String titulo;
    private LocalDateTime fecha;
    private String estado;
    private String profesorNombre;
    private String alumnoNombre;

    public ReunionListaDto(Integer id, String titulo, LocalDateTime fecha,
                           String estado, String profesorNombre, String alumnoNombre) {
        this.id = id;
        this.titulo = titulo;
        this.fecha = fecha;
        this.estado = estado;
        this.profesorNombre = profesorNombre;
        this.alumnoNombre = alumnoNombre;
    }

    public Integer getId() { return id; }
    public String getTitulo() { return titulo; }
    public LocalDateTime getFecha() { return fecha; }
    public String getEstado() { return estado; }
    public String getProfesorNombre() { return profesorNombre; }
    public String getAlumnoNombre() { return alumnoNombre; }
}
