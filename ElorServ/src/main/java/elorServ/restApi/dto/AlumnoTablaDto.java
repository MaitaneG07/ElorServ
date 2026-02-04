package elorServ.restApi.dto;

public class AlumnoTablaDto {

    private Integer id;
    private String nombre;
    private String apellidos;
    private String ciclo;
    private Integer curso;
    private String argazkiaUrl;

    public AlumnoTablaDto(
            Integer id,
            String nombre,
            String apellidos,
            String ciclo,
            Integer curso,
            String argazkiaUrl
    ) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.ciclo = ciclo;
        this.curso = curso;
        this.argazkiaUrl = argazkiaUrl;
    }

    public Integer getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public String getCiclo() { return ciclo; }
    public Integer getCurso() { return curso; }
    public String getArgazkiaUrl() { return argazkiaUrl; }
}
