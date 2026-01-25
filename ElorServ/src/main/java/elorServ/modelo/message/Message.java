package elorServ.modelo.message;

import java.util.List;

import elorServ.modelo.entities.Horarios;
import elorServ.modelo.entities.Users;


public class Message {
	private String tipo;
	private String usuario;
	private String password;
	private String estado;
	private String mensaje;
	private Users userData;
	private List<Users> usersList;
	private List<Horarios> horarioList;
	private Integer idProfesor;
	private Integer cicloId;
	private Integer curso;

	public Message() {
	}

	// Método estático para crear login
	public static Message crearLogin(String usuario, String password) {
		Message msg = new Message();
		msg.tipo = "LOGIN";
		msg.usuario = usuario;
		msg.password = password;
		return msg;
	}

	// NUEVO: Método para solicitar lista de alumnos por profesor
	public static Message createListStudentsById(int idProfesor) {
		Message msg = new Message();
		msg.tipo = "GET_ALUMNOS_BY_PROFESOR";
		msg.idProfesor = idProfesor;
		return msg;
	}

	public static Message createListStudentsByProfesorAndFilters(int idProfesor, Integer cicloId, Integer curso) {
		Message msg = new Message();
		msg.tipo = "GET_ALUMNOS_FILTRADOS";
		msg.idProfesor = idProfesor;
		msg.cicloId = cicloId;
		msg.curso = curso;
		return msg;
	}

	// Constructor para respuestas con objeto Users (servidor -> cliente)
	public static Message crearRespuestaConUsuario(String tipo, String estado, String mensaje, Users userData) {
		Message msg = new Message();
		msg.tipo = tipo;
		msg.estado = estado;
		msg.mensaje = mensaje;
		msg.userData = userData;
		return msg;
	}

	// Constructor para respuestas simples
	public static Message crearRespuesta(String tipo, String estado, String mensaje) {
		Message msg = new Message();
		msg.tipo = tipo;
		msg.estado = estado;
		msg.mensaje = mensaje;
		return msg;
	}

	// NUEVO: Constructor para respuestas con lista de usuarios
	public static Message crearRespuestaConLista(String tipo, String estado, String mensaje, List<Users> usersList) {
		Message msg = new Message();
		msg.tipo = tipo;
		msg.estado = estado;
		msg.mensaje = mensaje;
		msg.usersList = usersList;
		return msg;
	}
	
	public static Message crearRespuestaConListaHorarios(String tipo, String estado, String mensaje,
			List<Horarios> listHorarios) {
		Message msg = new Message();
		msg.tipo = tipo;
		msg.estado = estado;
		msg.mensaje = mensaje;
		msg.horarioList = listHorarios;
		return msg;
	}
	
	public static Message createHorario(Integer idProfesor) {
		Message msg = new Message();
		msg.tipo = "GET_HORARIO_PROFESOR";
		msg.idProfesor = idProfesor;
		return msg;
	}
	
	// Getters y Setters
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public Users getUserData() {
		return userData;
	}

	public void setUserData(Users userData) {
		this.userData = userData;
	}

	public List<Users> getUsersList() {
		return usersList;
	}

	public void setHorarioList(List<Horarios> horarioList) {
		this.horarioList = horarioList;
	}
	
	public List<Horarios> getHorarioList() {
		return horarioList;
	}
	
	public void setUsersList(List<Users> usersList) {
		this.usersList = usersList;
	}

	public Integer getIdProfesor() {
		return idProfesor;
	}

	public void setIdProfesor(Integer idProfesor) {
		this.idProfesor = idProfesor;
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
	public String toString() {
		return "Message{" + "tipo='" + tipo + '\'' + ", usuario='" + usuario + '\'' + ", estado='" + estado + '\''
				+ ", mensaje='" + mensaje + '\'' + ", userData=" + userData + ", usersList="
				+ (usersList != null ? usersList.size() + " usuarios" : "null") + ", idProfesor=" + idProfesor + '}';
	}
}