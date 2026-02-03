package elorServ;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

import elorServ.modelo.dao.HorariosDao;
import elorServ.modelo.dao.ReunionesDao;
import elorServ.modelo.dao.UsersDao;
import elorServ.modelo.email.EmailES;
import elorServ.modelo.entities.Horarios;
import elorServ.modelo.entities.Reuniones;
import elorServ.modelo.entities.Users;
import elorServ.modelo.exception.ElorException;
import elorServ.modelo.message.Message;
import elorServ.modelo.util.CryptoUtil;
import jakarta.mail.MessagingException;

@SpringBootApplication
public class Servidor {

	private ServerSocket serverSocket;
	private List<ManejadorCliente> clientesConectados;
	private int puerto;
	private boolean ejecutando;
	private UsersDao usuarioDAO;
	private Gson gson;
	private EmailES emailES;

	public Servidor() {

	}

	public Servidor(int puerto) {
		this.puerto = puerto;
		this.clientesConectados = new ArrayList<>();
		this.ejecutando = false;
		this.usuarioDAO = new UsersDao();

		// Configurar Gson con adaptadores para LocalDateTime
		this.gson = new GsonBuilder()
				.registerTypeAdapter(LocalDateTime.class,
						(JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> context.serialize(src.toString()))
				.registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, typeOfT,
						context) -> LocalDateTime.parse(json.getAsString()))
				.create();

		CryptoUtil.inicializar();
	}

	/**
	 * Inicia el servidor
	 */
	public void iniciar() {
		ejecutando = true;

		try {
			serverSocket = new ServerSocket(puerto);
			System.out.println("====================================");
			System.out.println("Servidor iniciado en puerto " + puerto);
			System.out.println("Esperando conexiones de clientes...");
			System.out.println("====================================");

			while (ejecutando) {
				Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
				try {
					Socket clienteSocket = serverSocket.accept();
					System.out.println("\n[NUEVA CONEXIÓN] Cliente conectado desde: "
							+ clienteSocket.getInetAddress().getHostAddress());

					// Crear un manejador para este cliente
					ManejadorCliente manejador = new ManejadorCliente(clienteSocket, this);
					clientesConectados.add(manejador);

					new Thread(manejador).start();

					System.out.println("Total de clientes conectados: " + clientesConectados.size());

				} catch (IOException e) {
					if (ejecutando) {
						System.err.println("Error al aceptar cliente: " + e.getMessage());
					}
				}
			}

		} catch (IOException e) {
			System.err.println("Error al iniciar el servidor: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Detiene el servidor
	 */
	public void detener() {
		ejecutando = false;
		try {
			if (serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
			System.out.println("\nServidor detenido");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Remueve un cliente de la lista
	 */
	public void removerCliente(ManejadorCliente cliente) {
		clientesConectados.remove(cliente);
		System.out.println("Cliente desconectado. Clientes restantes: " + clientesConectados.size());
	}

	/**
	 * Clase interna para manejar cada cliente
	 */
	private class ManejadorCliente implements Runnable {
		private Socket socket;
		private BufferedReader entrada;
		private PrintWriter salida;
		private Servidor servidor;
		private String nombreUsuario;
		private Message respuesta;

		public ManejadorCliente(Socket socket, Servidor servidor) {
			this.socket = socket;
			this.servidor = servidor;
		}

		@Override
		public void run() {
			try {
				entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				salida = new PrintWriter(socket.getOutputStream(), true);

				System.out.println("Bienvenido al servidor ELORRIETA");

				enviarClavePublica();

				String mensajeJson;
				while ((mensajeJson = entrada.readLine()) != null) {
					System.out.println("[RECIBIDO] " + mensajeJson);
					procesarMensajeJson(mensajeJson);
				}

			} catch (IOException e) {
				System.err.println("Error en la comunicación con el cliente: " + e.getMessage());
			} finally {
				cerrarConexion();
			}
		}

		/**
		 * Envía la clave pública RSA al cliente al inicio de la conexión
		 */
		private void enviarClavePublica() {
			try {
				String publicKeyBase64 = CryptoUtil.getPublicKeyBase64();

				Message mensaje = new Message();
				mensaje.setTipo("PUBLIC_KEY");
				mensaje.setEstado("OK");
				mensaje.setMensaje(publicKeyBase64);

				String json = gson.toJson(mensaje);
				salida.println(json);
				salida.flush();

				System.out.println("[SERVER] Clave pública enviada al cliente");
			} catch (Exception e) {
				System.err.println("[SERVER ERROR] Error al enviar clave pública: " + e.getMessage());
				e.printStackTrace();
			}
		}

		/**
		 * Procesa los mensajes JSON recibidos del cliente
		 */
		private void procesarMensajeJson(String mensajeJson) {
			try {
				Message mensaje = gson.fromJson(mensajeJson, Message.class);

				System.out.println("[DEBUG] Tipo recibido: '" + mensaje.getTipo() + "'");

				if (mensaje.getTipo() == null) {
					System.err.println("[ERROR] Tipo de mensaje es null");
					enviarRespuestaError("Tipo de mensaje inválido");
					return;
				}

				String tipo = mensaje.getTipo().trim();

				switch (tipo) {
				case "LOGIN":
					procesarLogin(mensaje);
					break;

				case "GET_ALUMNOS_BY_PROFESOR":
//		            	procesarGetAllStudentsById(mensaje);
					break;

				case "GET_ALUMNOS_FILTRADOS":
					procesarGetFilterStudents(mensaje);
					break;

				case "GET_HORARIO_PROFESOR":
					procesarHorario(mensaje);
					break;

				case "GET_PROFESORES":
					procesarGetAllTeachers(mensaje);
					break;

				case "GET_REUNIONES_PROFESOR":
					procesarGetReuniones(mensaje);
					break;

				case "GET_PROFESORES_FILTRADOS":
					procesarFilterTeachers(mensaje);
					break;

				case "ACTUALIZAR_REUNION":
					procesarActualizarReunion(mensaje);
					break;

				case "GET_STUDENTS":
					procesarGetAllStudents(mensaje);
					break;

				case "CREAR_REUNION":
					procesarCreateReunion(mensaje);
					break;

				default:
					System.err.println("[ERROR] Tipo no reconocido: '" + tipo + "'");
					enviarRespuestaError("Tipo de mensaje desconocido: " + tipo);
				}

			} catch (Exception e) {
				System.err.println("[ERROR] Excepción al procesar JSON: " + e.getMessage());
				e.printStackTrace();
				enviarRespuestaError("Error al procesar mensaje");
			}
		}

		/**
		 * Procesa el login del usuario
		 */
		private void procesarLogin(Message mensaje) {
			String usuario = mensaje.getUsuario();
			String passwordEncriptada = mensaje.getPassword();

			System.out.println("[LOGIN] Intentando login para: " + usuario);

			try {
//		        String password = CryptoUtil.desencriptar(passwordEncriptada);

				if (passwordEncriptada == null) {
					respuesta = Message.crearRespuesta("LOGIN_RESPONSE", "ERROR", "Error al procesar credenciales");
					String respuestaJson = gson.toJson(respuesta);
					salida.println(respuestaJson);
					salida.flush();
					System.out.println("[LOGIN ERROR] No se pudo encontrar la contraseña");
					return;
				}

				System.out.println("[LOGIN] Contraseña encontrada correctamente");

				List<Users> usuarios = usuarioDAO.selectAll();
				boolean encontrado = false;
				Users usuarioEncontrado = null;

				for (Users user : usuarios) {
					if (user.getUsername().equals(usuario) && user.getPassword().equals(passwordEncriptada)) {
						if ((user).getTipos().getId() == 3) {
							encontrado = true;
							usuarioEncontrado = user;
						}
						break;
					}
				}

				if (encontrado) {
					respuesta = Message.crearRespuestaConUsuario("LOGIN_RESPONSE", "OK", "Login exitoso",
							usuarioEncontrado);
					nombreUsuario = usuario;
					System.out.println("[LOGIN EXITOSO] Profesor: " + usuario);

					final Users userParaCorreo = usuarioEncontrado;

					// Lanzamos un hilo paralelo para no frenar la respuesta al cliente
					new Thread(() -> {
						try {
							String emailDestino = userParaCorreo.getEmail();
							String nombreUser = userParaCorreo.getUsername();

							if (emailDestino != null && !emailDestino.isEmpty()) {
								emailES = new EmailES();
								emailES.enviarCorreoLogin(emailDestino, nombreUser);
							} else {
								System.err.println(
										"[EMAIL WARN] El usuario " + nombreUser + " no tiene email configurado.");
							}
						} catch (Exception e) {
							System.err.println("[EMAIL ERROR] No se pudo enviar el correo: " + e.getMessage());
						}
					}).start();
				} else {
					respuesta = Message.crearRespuesta("LOGIN_RESPONSE", "ERROR",
							"Credenciales incorrectas o no eres profesor");
					System.out.println("[LOGIN FALLIDO] Usuario: " + usuario);
				}

				String respuestaJson = gson.toJson(respuesta);
				salida.println(respuestaJson);
				salida.flush();
				System.out.println("[ENVIADO JSON] " + respuestaJson);

			} catch (Exception e) {
				System.err.println("[ERROR] Error en login: " + e.getMessage());
				enviarRespuestaError("Error interno del servidor");
			}
		}

		/**
		 * Procesa el mensaje para obtener y enviar la lista de profesores
		 * 
		 * @param mensaje
		 */
		public void procesarGetAllTeachers(Message mensaje) {
			System.out.println("[GET_PROFESORES] Intentando obtener profesores");

			try {
				List<Users> listTeachers = usuarioDAO.selectAll();
				List<Users> profesoresEncontrados = new ArrayList<>();

				for (Users teacher : listTeachers) {
					if (teacher.getTipos().getId() == 3) {
						profesoresEncontrados.add(teacher);
					}
				}

				if (profesoresEncontrados != null && !profesoresEncontrados.isEmpty()) {
					respuesta = Message.crearRespuestaConLista("GET_PROFESORES_RESPONSE", "OK",
							"Se encontraron " + profesoresEncontrados.size() + " profesores", profesoresEncontrados);
					System.out.println(
							"[GET PROFESORES EXITOSO] Profesores encontrados: " + profesoresEncontrados.size());
				} else {
					respuesta = Message.crearRespuesta("GET_PROFESORES_RESPONSE", "ERROR", "No se encontraron alumnos");
					System.out.println("[GET PROFESORES FALLIDO] No se encontraron profesores");
				}

				String respuestaJson = gson.toJson(respuesta);
				salida.println(respuestaJson);
				salida.flush();
				System.out.println("[ENVIADO JSON] " + respuestaJson);

			} catch (Exception e) {
				System.err.println("[ERROR] Error en la búsqueda de profesores: " + e.getMessage());
				e.printStackTrace();
				enviarRespuestaError("Error interno del servidor");
			}
		}

		/**
		 * Procesa el mensaje para obtener y enviar la lista de profesores
		 * 
		 * @param mensaje
		 */
		public void procesarGetAllStudents(Message mensaje) {
			System.out.println("[GET_STUDENTS] Intentando obtener alumnos");

			try {
				List<Users> listStudents = usuarioDAO.selectAll();
				List<Users> alumnosEncontrados = new ArrayList<>();

				for (Users student : listStudents) {
					if (student.getTipos().getId() == 4) {
						alumnosEncontrados.add(student);
					}
				}

				if (alumnosEncontrados != null && !alumnosEncontrados.isEmpty()) {
					respuesta = Message.crearRespuestaConLista("GET_STUDENTS_RESPONSE", "OK",
							"Se encontraron " + alumnosEncontrados.size() + " alumnos", alumnosEncontrados);
					System.out.println("[GET STUDENTS EXITOSO] Alumnos encontrados: " + alumnosEncontrados.size());
				} else {
					respuesta = Message.crearRespuesta("GET_STUDENTS_RESPONSE", "ERROR", "No se encontraron alumnos");
					System.out.println("[GET STUDENTS FALLIDO] No se encontraron alumnos");
				}

				String respuestaJson = gson.toJson(respuesta);
				salida.println(respuestaJson);
				salida.flush();
				System.out.println("[ENVIADO JSON] " + respuestaJson);

			} catch (Exception e) {
				System.err.println("[ERROR] Error en la búsqueda de alumnos: " + e.getMessage());
				e.printStackTrace();
				enviarRespuestaError("Error interno del servidor");
			}
		}

		/**
		 * Procesa el mensaje para filtrar los alumnos por ciclo y curso
		 * 
		 * @param mensaje
		 * @return lista de estudiantes
		 */
		public void procesarGetFilterStudents(Message mensaje) {
			try {
				Integer profesorId = mensaje.getIdProfesor();
				Integer cicloId = mensaje.getCicloId();
				Integer curso = mensaje.getCurso();

				if (profesorId == null) {
					respuesta = Message.crearRespuesta("GET_ALUMNOS_FILTRADOS", "ERROR",
							"ID de profesor no proporcionado");
				}

				UsersDao usersDao = new UsersDao();
				List<Users> alumnos = usersDao.getAlumnosByProfesorAndFilters(profesorId, cicloId, curso);

				if (alumnos != null && !alumnos.isEmpty()) {
					respuesta = Message.crearRespuestaConLista("GET_ALUMNOS_FILTRADOS", "OK",
							"Se encontraron " + alumnos.size() + " alumnos", alumnos);
				} else {
					respuesta = Message.crearRespuestaConLista("GET_ALUMNOS_FILTRADOS", "OK",
							"No se encontraron alumnos con esos filtros", new ArrayList<>());
				}

				String respuestaJson = gson.toJson(respuesta);
				salida.println(respuestaJson);
				salida.flush();
				System.out.println("[ENVIADO JSON] " + respuestaJson);

			} catch (Exception e) {
				System.err.println("Error procesando GET_ALUMNOS_FILTRADOS: " + e.getMessage());
				e.printStackTrace();
				respuesta = Message.crearRespuesta("GET_ALUMNOS_FILTRADOS", "ERROR",
						"Error al obtener alumnos: " + e.getMessage());
			}
		}

		/**
		 * Procesa el mensaje para filtrar los profesores por ciclo y curso
		 * 
		 * @param mensaje
		 * @return
		 */
		public void procesarFilterTeachers(Message mensaje) {
			try {
				Integer cicloId = mensaje.getCicloId();
				Integer curso = mensaje.getCurso();

				UsersDao usersDao = new UsersDao();
				List<Users> profesores = usersDao.getProfesoresByFilters(cicloId, curso);

				if (profesores != null && !profesores.isEmpty()) {
					respuesta = Message.crearRespuestaConLista("GET_PROFESORES_FILTRADOS", "OK",
							"Se encontraron " + profesores.size() + " profesores", profesores);
				} else {
					respuesta = Message.crearRespuestaConLista("GET_PROFESORES_FILTRADOS", "OK",
							"No se encontraron profesores con esos filtros", new ArrayList<>());
				}

				String respuestaJson = gson.toJson(respuesta);
				salida.println(respuestaJson);
				salida.flush();
				System.out.println("[ENVIADO JSON] " + respuestaJson);

			} catch (Exception e) {
				System.err.println("Error procesando GET_PROFESORES_FILTRADOS: " + e.getMessage());
				e.printStackTrace();
				respuesta = Message.crearRespuesta("GET_PROFESORES_FILTRADOS", "ERROR",
						"Error al obtener profesores: " + e.getMessage());
			}
		}

		/**
		 * Procesa el mensaje para obtener el horario de un profesor
		 * 
		 * @param mensaje
		 */
		public void procesarHorario(Message mensaje) {
			try {
				System.out.println("[SERVER] INICIO procesarHorario");

				int idProfesor = mensaje.getIdProfesor();
				System.out.println("[SERVER] ID Profesor: " + idProfesor);

				HorariosDao horarioDao = new HorariosDao();
				List<Horarios> listHorarios = horarioDao.selectHorarioByProfesorId(idProfesor);

				System.out.println(
						"[SERVER] Lista recibida del DAO: " + (listHorarios != null ? listHorarios.size() : "null"));

				if (listHorarios != null && !listHorarios.isEmpty()) {
					System.out.println("[SERVER] Creando respuesta con " + listHorarios.size() + " horarios");
					respuesta = Message.crearRespuestaConListaHorarios("GET_HORARIOS_PROFESOR_RESPONSE", "OK",
							"Horario obtenido", listHorarios);
				} else {
					System.out.println("[SERVER] Sin horarios, enviando lista vacía");
					respuesta = Message.crearRespuestaConListaHorarios("GET_HORARIOS_PROFESOR_RESPONSE", "OK",
							"Sin horarios", new ArrayList<>());
				}

				String respuestaJson = gson.toJson(respuesta);
				System.out.println("[SERVER] JSON a enviar (primeros 200 chars): "
						+ respuestaJson.substring(0, Math.min(200, respuestaJson.length())));

				salida.println(respuestaJson);
				salida.flush();
				System.out.println("[SERVER] Respuesta enviada");

			} catch (Exception e) {
				System.err.println("[SERVER ERROR] " + e.getMessage());
				e.printStackTrace();
				enviarRespuestaError("Error al obtener horarios");
			}
		}

		/**
		 * Procesa el mensaje para obtener las reuniones de un profesor
		 * 
		 * @param mensaje
		 */
		public void procesarGetReuniones(Message mensaje) {
			try {
				System.out.println("[PROCESANDO REUNIONES] Inicio");

				int idProfesor = mensaje.getIdProfesor();
				System.out.println("[GET_REUNIONES] ID del profesor: " + idProfesor);

				ReunionesDao reunionesDao = new ReunionesDao();
				List<Reuniones> listReuniones = reunionesDao.selectReunionesByProfesorId(idProfesor);

				System.out.println(
						"[DEBUG] Reuniones obtenidas: " + (listReuniones != null ? listReuniones.size() : "null"));

				if (listReuniones != null && !listReuniones.isEmpty()) {
					respuesta = Message.crearRespuestaConListaReuniones("GET_REUNIONES_PROFESOR_RESPONSE", "OK",
							"Reuniones obtenidas", listReuniones);
					System.out.println("[ÉXITO] " + listReuniones.size() + " reuniones encontradas");
				} else {
					respuesta = Message.crearRespuestaConListaReuniones("GET_REUNIONES_PROFESOR_RESPONSE", "OK",
							"Sin reuniones", new ArrayList<>());
					System.out.println("[INFO] Sin reuniones");
				}

				String respuestaJson = gson.toJson(respuesta);
				salida.println(respuestaJson);
				salida.flush();
				System.out.println("[ENVIADO] Respuesta de reuniones");

			} catch (Exception e) {
				System.err.println("[ERROR] En procesarReuniones: " + e.getMessage());
				e.printStackTrace();
				enviarRespuestaError("Error al obtener reuniones");
			}
		}

		/**
		 * Procesa la actualización del estado de una reunión
		 * 
		 * @param mensaje
		 */
		private void procesarActualizarReunion(Message mensaje) {
			try {
				System.out.println("[ACTUALIZAR_REUNION] Inicio del proceso");

				Reuniones reunionEntrante = mensaje.getReunion();

				if (reunionEntrante == null) {
					System.err.println("[ERROR] La reunión recibida es null");
					// ... (código de error igual que antes)
					return;
				}

				// 1. Actualizamos el estado en la BD
				ReunionesDao reunionesDao = new ReunionesDao();
				boolean actualizado = reunionesDao.actualizarEstadoReunion(reunionEntrante.getIdReunion(),
						reunionEntrante.getEstado());

				if (actualizado) {
					respuesta = Message.crearRespuesta("ACTUALIZAR_REUNION_RESPONSE", "OK",
							"Reunión actualizada correctamente");

					// Envio de email a profesor y alumno
					Reuniones reunionCompleta = reunionesDao.selectById(reunionEntrante.getIdReunion());

					if (reunionCompleta != null) {
						final String estadoNuevo = reunionEntrante.getEstado();
						final String emailProfesor = (reunionCompleta.getProfesores() != null)
								? reunionCompleta.getProfesores().getEmail()
								: null;
						final String emailAlumno = (reunionCompleta.getAlumnos() != null)
								? reunionCompleta.getAlumnos().getEmail()
								: null;

						new Thread(() -> {
							EmailES emailService = new EmailES();

							// Enviar al profesor
							if (emailProfesor != null && !emailProfesor.isEmpty()) {
								emailService.enviarActualizacionReunion(emailProfesor, estadoNuevo);
								System.out.println("[EMAIL] Enviado al profesor (" + emailProfesor + ")");
							}

							// Enviar al alumno
							if (emailAlumno != null && !emailAlumno.isEmpty()) {
								emailService.enviarActualizacionReunion(emailAlumno, estadoNuevo);
								System.out.println("[EMAIL] Enviado al alumno (" + emailAlumno + ")");
							}
						}).start();
					} else {
						System.err.println(
								"[EMAIL WARNING] No se pudo recuperar la reunión completa para enviar correos.");
					}

				} else {
					respuesta = Message.crearRespuesta("ACTUALIZAR_REUNION_RESPONSE", "ERROR",
							"No se pudo actualizar la reunión");
					System.err.println("[ERROR] Fallo al actualizar la reunión en la BD");
				}

				String respuestaJson = gson.toJson(respuesta);
				salida.println(respuestaJson);
				salida.flush();
				System.out.println("[ENVIADO] Respuesta de actualización");

			} catch (Exception e) {
				System.err.println("[ERROR] Excepción al actualizar reunión: " + e.getMessage());
				e.printStackTrace();
				enviarRespuestaError("Error interno del servidor al actualizar reunión");
			}
		}

		/**
		 * Procesa el mensaje para crear una reunion
		 * 
		 * @param mensaje
		 */
		public void procesarCreateReunion(Message mensaje) throws ElorException {
			System.out.println("[CREAR_REUNION] Inicio del proceso");

			String estado = mensaje.getEstado();
			String titulo = mensaje.getTitulo();
			String asunto = mensaje.getAsunto();
			String aula = mensaje.getAula();
			Integer idAlumno = mensaje.getIdAlumnoSeleccionado();
			Integer idProfesor = mensaje.getIdProfesor();
			LocalDateTime fechaHora = mensaje.getFechaHora();
			Integer idCentro = 15112;

			if (estado == null || titulo == null || aula == null || idAlumno == null || idProfesor == null
					|| fechaHora == null) {

				System.err.println("[CREAR_REUNION ERROR] Datos incompletos");
				respuesta = Message.crearRespuesta("CREAR_REUNION_RESPONSE", "ERROR",
						"Datos incompletos para crear la reunión");
				salida.println(gson.toJson(respuesta));
				salida.flush();
				return;
			}

			if (fechaHora.isBefore(LocalDateTime.now())) {
				System.err.println("[CREAR_REUNION ERROR] Fecha en el pasado: " + fechaHora);
				respuesta = Message.crearRespuesta("CREAR_REUNION_RESPONSE", "ERROR",
						"La fecha y hora de la reunión no pueden ser en el pasado");
				salida.println(gson.toJson(respuesta));
				salida.flush();
				return;
			}

			Reuniones reunion = new Reuniones(estado, titulo, asunto, aula, idAlumno, idProfesor, fechaHora, idCentro);

			ReunionesDao reunionesDao = new ReunionesDao();
			boolean insertado = reunionesDao.insertarReunion(reunion);

			if (insertado) {
				System.out.println("[CREAR_REUNION OK] Reunión creada correctamente");

				respuesta = Message.crearRespuesta("CREAR_REUNION_RESPONSE", "OK", "Reunión creada correctamente");

				// Envio de email a profesor y alumno
				String emailAlumno = usuarioDAO.selectById(idAlumno).getEmail();
				String emailProfesor = usuarioDAO.selectById(idProfesor).getEmail();

				if (emailProfesor != null && !emailProfesor.isEmpty()) {

					new Thread(() -> {
						EmailES emailService = new EmailES(); // Esto carga la config encriptada automáticamente
						try {
							emailService.enviarConfirmacionReunion(emailProfesor, titulo, aula, fechaHora, estado);
						} catch (ElorException | MessagingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						try {
							emailService.enviarConfirmacionReunion(emailAlumno, titulo, aula, fechaHora, estado);
						} catch (ElorException | MessagingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}).start();
				}

			} else {
				System.err.println("[CREAR_REUNION ERROR] Error al insertar en BD");

				respuesta = Message.crearRespuesta("CREAR_REUNION_RESPONSE", "ERROR", "No se pudo crear la reunión");
			}

			String respuestaJson = gson.toJson(respuesta);
			salida.println(respuestaJson);
			salida.flush();

		}

		/**
		 * Envía una respuesta de error al cliente
		 */
		private void enviarRespuestaError(String mensajeError) {
			Message respuesta = Message.crearRespuesta("ERROR_RESPONSE", "ERROR", mensajeError);
			String respuestaJson = gson.toJson(respuesta);
			salida.println(respuestaJson);
			salida.flush();
			System.out.println("[ENVIADO ERROR] " + respuestaJson);
		}

		/**
		 * Cierra la conexión con el cliente
		 */
		private void cerrarConexion() {
			try {
				if (entrada != null)
					entrada.close();
				if (salida != null)
					salida.close();
				if (socket != null)
					socket.close();
				servidor.removerCliente(this);

				if (nombreUsuario != null) {
					System.out.println("[DESCONEXIÓN] Usuario: " + nombreUsuario);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Punto de entrada del servidor
	 */
	public static void main(String[] args) {

		SpringApplication.run(Servidor.class, args);
		int puerto = 8080;

		System.out.println("╔═══════════════════════════════════╗");
		System.out.println("║   SERVIDOR EDUCATIVO ELORRIETA    ║");
		System.out.println("╚═══════════════════════════════════╝");

		Servidor servidor = new Servidor(puerto);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("\nCerrando servidor...");
			servidor.detener();
		}));

		servidor.iniciar();

	}
}