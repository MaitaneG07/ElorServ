package elorServ.modelo.message;

import java.time.LocalDateTime;
import java.util.List;

import elorServ.modelo.entities.Horarios;
import elorServ.modelo.entities.Reuniones;
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
	private List<Reuniones> reunionesList;
	private Reuniones reunion;
	private Integer idProfesor;
	private Integer cicloId;
	private Integer curso;
	private String titulo;
	private String asunto;
	private String aula;
	private Integer idAlumnoSeleccionado;
	private LocalDateTime fechaHora;
	private String email;

	public Message() {
	}

	/**
	 * Método estático para crear login
	 * @param usuario
	 * @param password
	 * @return mensaje con los datos recibidos para el login
	 */
	public static Message crearLogin(String usuario, String password) {
		Message msg = new Message();
		msg.tipo = "LOGIN";
		msg.usuario = usuario;
		msg.password = password;
		return msg;
	}

	/**
	 * Método para solicitar lista de alumnos por profesor
	 * @param idProfesor
	 * @return mensaje
	 */
	public static Message createListStudentsById(int idProfesor) {
		Message msg = new Message();
		msg.tipo = "GET_ALUMNOS_BY_PROFESOR";
		msg.idProfesor = idProfesor;
		return msg;
	}

	/**
	 * Método para solicitar lista de profesores
	 * @return mensaje
	 */
	public static Message createListTeachers() {
		Message msg = new Message();
		msg.tipo = "GET_PROFESORES";
		return msg;
	}
	
	/**
	 * Método para solicitar lista de alumnos
	 * @return mensaje
	 */
	public static Message createListStudents() {
		Message msg = new Message();
		msg.tipo = "GET_STUDENTS";
		return msg;
	}

	/**
	 * Método para solicitar lista de profesores filtrados
	 * @param cicloId
	 * @param curso
	 * @return mensaje
	 */
	public static Message createListTeachersByFilters(Integer cicloId, Integer curso) {
		Message msg = new Message();
		msg.tipo = "GET_PROFESORES_FILTRADOS";
		msg.cicloId = cicloId;
		msg.curso = curso;
		return msg;
	}

	/**
	 * Metodo para solicitar lista de horarios de un profesor
	 * @param idProfesor
	 * @return mensaje
	 */
	public static Message createHorario(Integer idProfesor) {
		Message msg = new Message();
		msg.tipo = "GET_HORARIO_PROFESOR";
		msg.idProfesor = idProfesor;
		return msg;
	}

	/**
	 * Metodo para solicitar lista de reuniones de un profesor
	 * @param idProfesor
	 * @return mensaje
	 */
	public static Message createGetReunionesProfesor(int idProfesor) {
		Message msg = new Message();
		msg.setTipo("GET_REUNIONES_PROFESOR");
		msg.setIdProfesor(idProfesor);
		return msg;
	}

	/**
	 * Metodo para solicitar lista de alumnos filtrados por profesor, ciclo y curso
	 * @param idProfesor
	 * @param cicloId
	 * @param curso
	 * @return mensaje
	 */
	public static Message createListStudentsByProfesorAndFilters(int idProfesor, Integer cicloId, Integer curso) {
		Message msg = new Message();
		msg.tipo = "GET_ALUMNOS_FILTRADOS";
		msg.idProfesor = idProfesor;
		msg.cicloId = cicloId;
		msg.curso = curso;
		return msg;
	}

	// Método para actualizar una reunión
	/**
	 * Método para actualizar una reunión
	 * @param reunion
	 * @return mensaje
	 */
	public static Message createActualizarReunion(Reuniones reunion) {
		Message msg = new Message();
		msg.tipo = "ACTUALIZAR_REUNION";
		msg.reunion = reunion;
		return msg;
	}
	
	// Método para actualizar una reunión
	/**
	 * Método para actualizar una reunión
	 * @param reunion
	 * @return mensaje
	 */
	public static Message createReunion(String estado, String titulo, String asunto, String aula, Integer idAlumnoSeleccionado, LocalDateTime fechaHora, Integer idProfesor) {
		Message msg = new Message();
		msg.tipo = "CREAR_REUNION";
		msg.estado = estado;
		msg.titulo = titulo;
		msg.asunto = asunto;
		msg.aula = aula;
		msg.idAlumnoSeleccionado = idAlumnoSeleccionado;
		msg.fechaHora = fechaHora;
		msg.idProfesor = idProfesor;
		return msg;
	}
	

	/**
	 * Constructor para respuestas con objeto Users
	 * @param tipo
	 * @param estado
	 * @param mensaje
	 * @param userData
	 * @return mensaje para una respuesta con un objeto user
	 */
	public static Message crearRespuestaConUsuario(String tipo, String estado, String mensaje, Users userData) {
		Message msg = new Message();
		msg.tipo = tipo;
		msg.estado = estado;
		msg.mensaje = mensaje;
		msg.userData = userData;
		return msg;
	}

	/**
	 * Constructor para respuestas simples
	 * @param tipo
	 * @param estado
	 * @param mensaje
	 * @return mensaje para una respuesta simple
	 */
	public static Message crearRespuesta(String tipo, String estado, String mensaje) {
		Message msg = new Message();
		msg.tipo = tipo;
		msg.estado = estado;
		msg.mensaje = mensaje;
		return msg;
	}

	/**
	 * Constructor para respuestas con lista de usuarios
	 * @param tipo
	 * @param estado
	 * @param mensaje
	 * @param usersList
	 * @return mensaje con lista de usuarios
	 */
	public static Message crearRespuestaConLista(String tipo, String estado, String mensaje, List<Users> usersList) {
		Message msg = new Message();
		msg.tipo = tipo;
		msg.estado = estado;
		msg.mensaje = mensaje;
		msg.usersList = usersList;
		return msg;
	}

	/**
	 * Constructor para respuesta con lista de horarios
	 * @param tipo
	 * @param estado
	 * @param mensaje
	 * @param listHorarios
	 * @return mensaje con lista de horarios
	 */
	public static Message crearRespuestaConListaHorarios(String tipo, String estado, String mensaje,
			List<Horarios> listHorarios) {
		Message msg = new Message();
		msg.tipo = tipo;
		msg.estado = estado;
		msg.mensaje = mensaje;
		msg.horarioList = listHorarios;
		return msg;
	}
	
	/**
	 * Constructor para respuesta con lista de reuniones
	 * @param tipo
	 * @param estado
	 * @param mensaje
	 * @param listReuniones
	 * @return mensaje con lista de reuniones 
	 */
	public static Message crearRespuestaConListaReuniones(String tipo, String estado, String mensaje,
			List<Reuniones> listReuniones) {
		Message msg = new Message();
		msg.tipo = tipo;
		msg.estado = estado;
		msg.mensaje = mensaje;
		msg.reunionesList = listReuniones;
		return msg;
	}

	/**
	 * Constructor para respuestas con una reunión
	 * @param tipo
	 * @param estado
	 * @param mensaje
	 * @param reunion
	 * @return mensaje con una reunion
	 */
	public static Message crearRespuestaConReunion(String tipo, String estado, String mensaje, Reuniones reunion) {
		Message msg = new Message();
		msg.tipo = tipo;
		msg.estado = estado;
		msg.mensaje = mensaje;
		msg.reunion = reunion;
		return msg;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public List<Reuniones> getReunionesList() {
	    return reunionesList;
	}

	public void setReunionesList(List<Reuniones> reunionesList) {
	    this.reunionesList = reunionesList;
	}

	public Reuniones getReunion() {
		return reunion;
	}

	public void setReunion(Reuniones reunion) {
		this.reunion = reunion;
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

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getAula() {
		return aula;
	}

	public void setAula(String aula) {
		this.aula = aula;
	}

	public LocalDateTime getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(LocalDateTime fechaHora) {
		this.fechaHora = fechaHora;
	}

	public Integer getIdAlumnoSeleccionado() {
		return idAlumnoSeleccionado;
	}

	public void setIdAlumnoSeleccionado(Integer idAlumnoSeleccionado) {
		this.idAlumnoSeleccionado = idAlumnoSeleccionado;
	}

	@Override
	public String toString() {
		return "Message{" + "tipo='" + tipo + '\'' + ", usuario='" + usuario + '\'' + ", estado='" + estado + '\''
				+ ", mensaje='" + mensaje + '\'' + ", userData=" + userData + ", usersList="
				+ (usersList != null ? usersList.size() + " usuarios" : "null") + ", idProfesor=" + idProfesor + '}';
	}
}