package elorServ;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
   
    private ServerSocket serverSocket;
    private List<ManejadorCliente> clientesConectados;
    private int puerto;
    private boolean ejecutando;
   
    public Servidor(int puerto) {
        this.puerto = puerto;
        this.clientesConectados = new ArrayList<>();
        this.ejecutando = false;
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
           
            // Bucle principal para aceptar clientes
            while (ejecutando) {
                try {
                    Socket clienteSocket = serverSocket.accept();
                    System.out.println("\n[NUEVA CONEXIÓN] Cliente conectado desde: "
                        + clienteSocket.getInetAddress().getHostAddress());
                   
                    // Crear un manejador para este cliente
                    ManejadorCliente manejador = new ManejadorCliente(clienteSocket, this);
                    clientesConectados.add(manejador);
                   
                    // Iniciar el hilo del cliente
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
                // Configurar streams de entrada/salida
                entrada = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
                );
                salida = new PrintWriter(socket.getOutputStream(), true);
               
                // Enviar mensaje de bienvenida
                salida.println("Bienvenido al servidor ELORRIETA");
                salida.flush();
               
                // Leer mensajes del cliente
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
         */
        private void procesarMensaje(String mensaje) {
            if (mensaje.startsWith("LOGIN:")) {
                procesarLogin(mensaje);
            } else if (mensaje.startsWith("REGISTRO:")) {
                procesarRegistro(mensaje);
            } else if (mensaje.startsWith("CONSULTA:")) {
                procesarConsulta(mensaje);
            } else {
                // Mensaje genérico
                System.out.println("Mensaje del cliente: " + mensaje);
                salida.println("Servidor recibió: " + mensaje);
                salida.flush();
            }
        }
       
        /**
         * Procesa el login del usuario
         */
        private void procesarLogin(String mensaje) {
            // Formato esperado: LOGIN:usuario:password
            String[] partes = mensaje.split(":");
           
            if (partes.length == 3) {
                String usuario = partes[1];
                String password = partes[2];
               
                System.out.println("[LOGIN] Usuario: " + usuario);
               
                // Aquí validarías contra una base de datos
                // Por ahora, ejemplo simple
                if (validarCredenciales(usuario, password)) {
                    nombreUsuario = usuario;
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
 * Valida las credenciales
 */
private boolean validarCredenciales(String usuario, String password) {
    // Usuario: profesor1, Contraseña: 123456
    if (usuario.equals("profesor1") && password.equals("123456")) {
        return true;
    }
    return false;
}
       
        /**
         * Procesa el registro de un nuevo usuario
         */
        private void procesarRegistro(String mensaje) {
            // Formato: REGISTRO:usuario:password:email
            String[] partes = mensaje.split(":");
           
            if (partes.length == 4) {
                String usuario = partes[1];
                String password = partes[2];
                String email = partes[3];
               
                System.out.println("[REGISTRO] Usuario: " + usuario + ", Email: " + email);
               
                // Aquí guardarías en la base de datos
                salida.println("REGISTRO_OK");
                salida.flush();
            } else {
                salida.println("ERROR:FORMATO_INVALIDO");
                salida.flush();
            }
        }
       
        /**
         * Procesa consultas generales
         */
        private void procesarConsulta(String mensaje) {
            // Formato: CONSULTA:tipo:parametros
            System.out.println("[CONSULTA] " + mensaje);
           
            // Aquí procesarías la consulta según tu lógica de negocio
            salida.println("DATOS_CONSULTA:ejemplo1,ejemplo2,ejemplo3");
            salida.flush();
        }
       
        /**
         * Cierra la conexión con el cliente
         */
        private void cerrarConexion() {
            try {
                if (entrada != null) entrada.close();
                if (salida != null) salida.close();
                if (socket != null) socket.close();
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
        int puerto = 8080;
       
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║   SERVIDOR EDUCATIVO ELORRIETA    ║");
        System.out.println("╚════════════════════════════════════╝");
       
        Servidor servidor = new Servidor(puerto);
       
        // Agregar hook para cerrar el servidor correctamente
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nCerrando servidor...");
            servidor.detener();
        }));
       
        // Iniciar el servidor
        servidor.iniciar();
    }
}