package elorServ;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import elorServ.modelo.dao.UsersDao;
import elorServ.modelo.entities.Users;

@SpringBootApplication
public class Servidor {

	private ServerSocket serverSocket;
	private List<ManejadorCliente> clientesConectados;
	private int puerto;
	private boolean ejecutando;
	private UsersDao usuarioDAO;

	public Servidor() {
		
	}
	
	public Servidor(int puerto) {
		this.puerto = puerto;
		this.clientesConectados = new ArrayList<>();
		this.ejecutando = false;
		this.usuarioDAO = new UsersDao();
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

		public ManejadorCliente(Socket socket, Servidor servidor) {
			this.socket = socket;
			this.servidor = servidor;
		}

		@Override
		public void run() {
			try {
				entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				salida = new PrintWriter(socket.getOutputStream(), true);

				salida.println("Bienvenido al servidor ELORRIETA");
				salida.flush();

				String mensaje;
				while ((mensaje = entrada.readLine()) != null) {
					System.out.println("[RECIBIDO] " + mensaje);
					procesarMensaje(mensaje);
				}

			} catch (IOException e) {
				System.err.println("Error en la comunicación con el cliente: " + e.getMessage());
			} finally {
				cerrarConexion();
			}
		}

		/**
		 * Procesa los mensajes recibidos del cliente
		 * @throws IOException 
		 */
		private void procesarMensaje(String mensaje) throws IOException {
			if (mensaje.startsWith("LOGIN:")) {
				procesarLogin(mensaje);
			} else if (mensaje.startsWith("REGISTRO:")) {
//                procesarRegistro(mensaje);
			} else {
				System.out.println("Mensaje del cliente: " + mensaje);
				salida.println("Servidor recibió: " + mensaje);
				salida.flush();
			}
		}

		/**
		 * Procesa el login del usuario
		 * @throws IOException 
		 */
		private void procesarLogin(String mensaje) throws IOException {
			String[] partes = mensaje.split(":");

			if (partes.length == 3) {
				String usuario = partes[1];
				String password = partes[2];

				System.out.println("[LOGIN] Usuario: " + usuario);

				List<Users> usuarios = usuarioDAO.selectAll();
	            
				boolean encontrado = false;

				for (Users user : usuarios) {
				    if (user.getUsername().equals(usuario) && user.getPassword().equals(password)) {
				        encontrado = true;
				        break;
				    }
				}

				if (encontrado) {
				    salida.println("OK");
				    salida.flush();
				    System.out.println("[LOGIN EXITOSO] Usuario: " + usuario);
				} else {
				    salida.println("ERROR");
				    salida.flush();
				    System.out.println("[LOGIN FALLIDO] Usuario: " + usuario);
				}


			} else {
				salida.println("ERROR:FORMATO_INVALIDO");
				salida.flush();
			}
		}

		/**
		 * Procesa el registro de un nuevo usuario
		 */
//        private void procesarRegistro(String mensaje) {
//            // Formato: REGISTRO:usuario:password:email
//            String[] partes = mensaje.split(":");
//           
//            if (partes.length == 4) {
//                String usuario = partes[1];
//                String password = partes[2];
//                String email = partes[3];
//               
//                System.out.println("[REGISTRO] Usuario: " + usuario + ", Email: " + email);
//               
//                // Aquí guardarías en la base de datos
//                salida.println("REGISTRO_OK");
//                salida.flush();
//            } else {
//                salida.println("ERROR:FORMATO_INVALIDO");
//                salida.flush();
//            }
//        }

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
		/*int puerto = 8080;

		System.out.println("╔════════════════════════════════════╗");
		System.out.println("║   SERVIDOR EDUCATIVO ELORRIETA    ║");
		System.out.println("╚════════════════════════════════════╝");

		Servidor servidor = new Servidor(puerto);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("\nCerrando servidor...");
			servidor.detener();
		}));

		servidor.iniciar();*/
		
		
	}
}