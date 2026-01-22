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

import elorServ.modelo.dao.UsersDao;
import elorServ.modelo.entities.Users;
import elorServ.modelo.message.Message;

@SpringBootApplication
public class Servidor {

	private ServerSocket serverSocket;
	private List<ManejadorCliente> clientesConectados;
	private int puerto;
	private boolean ejecutando;
	private UsersDao usuarioDAO;
	private Gson gson;

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
		 * Procesa los mensajes JSON recibidos del cliente
		 */
		private void procesarMensajeJson(String mensajeJson) {

			Message mensaje = gson.fromJson(mensajeJson, Message.class);

			switch (mensaje.getTipo()) {
			case "LOGIN":
				procesarLogin(mensaje);
				break;

			case "GET_ALUMNOS_BY_PROFESOR":
				procesarGetAllStudents(mensaje);
				break;

			case "GET_ALUMNOS_FILTRADOS":
				procesarGetFilterStudents(mensaje);
				break;

			default:
				System.err.println("[ERROR] Error al procesar JSON");
				enviarRespuestaError("Error al procesar mensaje");
			}
		}

		/**
		 * Procesa el login del usuario
		 */
		private void procesarLogin(Message mensaje) {
			String usuario = mensaje.getUsuario();
			String password = mensaje.getPassword();

			System.out.println("[LOGIN] Intentando login para: " + usuario);

			try {
				List<Users> usuarios = usuarioDAO.selectAll();
				boolean encontrado = false;
				Users usuarioEncontrado = null;

				for (Users user : usuarios) {
					if (user.getUsername().equals(usuario) && user.getPassword().equals(password)) {
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
		 * Procesa el mensaje para obtener y enviar la lista de alumnos
		 * 
		 * @param mensaje
		 */
		public void procesarGetAllStudents(Message mensaje) {
			int idProfesor = mensaje.getIdProfesor();

			System.out.println("[GET_ALUMNOS_BY_PROFESOR] Intentando obtener alumnos del profesor: " + idProfesor);

			try {
				List<Users> listStudents = usuarioDAO.selectAll();
				List<Users> usuariosEncontrados = new ArrayList<>();

				for (Users alumno : listStudents) {
					if (alumno.getTipos().getId() == 4) {
						usuariosEncontrados.add(alumno);
					}
				}

				if (usuariosEncontrados != null && !usuariosEncontrados.isEmpty()) {
					respuesta = Message.crearRespuestaConLista("GET_ALUMNOS_BY_PROFESOR_RESPONSE", "OK",
							"Se encontraron " + usuariosEncontrados.size() + " alumnos", usuariosEncontrados);
					System.out.println("[GET ALUMNOS EXITOSO] Profesor ID: " + idProfesor + ", Alumnos encontrados: "
							+ usuariosEncontrados.size());
				} else {
					respuesta = Message.crearRespuesta("GET_ALUMNOS_BY_PROFESOR_RESPONSE", "ERROR",
							"No se encontraron alumnos");
					System.out
							.println("[GET ALUMNOS FALLIDO] Profesor ID: " + idProfesor + "No se encontraron alumnos");
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
		 * @return
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