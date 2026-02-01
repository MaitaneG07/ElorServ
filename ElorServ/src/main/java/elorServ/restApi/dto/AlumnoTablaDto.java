package elorServ.restApi.dto;

public class AlumnoTablaDto {
	private String nombre;
    private String apellidos;
    private String ciclo;
    private Integer curso;

    public AlumnoTablaDto(String nombre, String apellidos, String ciclo, Integer curso) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.ciclo = ciclo;
        this.curso = curso;
    }

    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public String getCiclo() { return ciclo; }
    public Integer getCurso() { return curso; }
}
