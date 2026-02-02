package elorServ.restApi.dto;

import java.time.LocalDateTime;

public class ReunionListaDto {

	private Integer idReunion;
    private String titulo;
    private LocalDateTime fecha;
    private String estado;
    private Integer profesorId;
    private Integer alumnoId;

    public ReunionListaDto(Integer idReunion, String titulo, LocalDateTime fecha, String estado, Integer profesorId, Integer alumnoId) {
        this.idReunion = idReunion;
        this.titulo = titulo;
        this.fecha = fecha;
        this.estado = estado;
        this.profesorId = profesorId;
        this.alumnoId = alumnoId;
    }

    public Integer getIdReunion() { return idReunion; }
    public String getTitulo() { return titulo; }
    public LocalDateTime getFecha() { return fecha; }
    public String getEstado() { return estado; }
    public Integer getProfesorId() { return profesorId; }
    public Integer getAlumnoId() { return alumnoId; }
}
