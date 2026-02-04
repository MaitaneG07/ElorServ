package elorServ.restApi.dto;

public class ProfesorTablaDto {
	private Integer id;
    private String nombre;
    private String apellidos;

    public ProfesorTablaDto(Integer id, String nombre, String apellidos) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }
}
