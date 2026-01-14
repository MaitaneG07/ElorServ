package server;

import java.io.BufferedReader;
 import java.io.IOException;

import java.io.InputStreamReader;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Cliente de prueba
 */
public class Cliente {

	private Scanner scanner = null;

	public Cliente() {
		scanner = new Scanner(System.in);
	}

	public void doSend() {

		int puerto = 8080;
		String ipServidor = "10.5.104.31";

		try (Socket socket = new Socket(ipServidor, puerto)) {
            System.out.println("Conectado al servidor");

            // Streams para comunicación

            PrintWriter salida = new PrintWriter(
                socket.getOutputStream(), true

            );

            BufferedReader entrada = new BufferedReader(
                new InputStreamReader(socket.getInputStream())

            );

            // Enviar mensaje al servidor
            salida.println("Hola desde el cliente");
            
            // Recibir respuesta

            String respuesta = entrada.readLine();
            System.out.println("Servidor respondió: " + respuesta);

            

        } catch (IOException e) {

            System.out.println("Error de conexión: " + e.getMessage());
            e.printStackTrace();

        }

    }

	public static void main(String[] args) {

		Cliente cliente = new Cliente();
		cliente.doSend();

	} 

}
