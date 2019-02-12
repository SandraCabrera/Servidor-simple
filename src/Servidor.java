import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Servidor {
	private ServerSocket serverSocket;
	private Socket socket;
	private BufferedReader bufferEntrada;
	private OutputStream bufferSortida;
	
	public void connectar(int port) {
		
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Esperando conexión entrante...");
			socket = serverSocket.accept();
			System.out.println("Conexión establecida con: "+socket.getInetAddress());		
		}catch (Exception e){
			System.out.println("Error a conectar: "+e.getMessage());
		}
	}
	
	public void canals() {
		try {
			bufferEntrada = new BufferedReader (new InputStreamReader(socket.getInputStream()));
			bufferSortida = socket.getOutputStream();
			bufferSortida.flush();
		}catch (Exception e){
			System.out.println("Error en los canales: "+e.getMessage());
		}
	}
	
	public void rebre() {
		String input;
		try {
			do {
				input = bufferEntrada.readLine();
				System.out.println(input);
			}while (!input.isEmpty());
		}catch(Exception e) {
			System.out.println("Error a recibir: "+e.getMessage());
		}
	}
	
	public void enviar() {
		String web = "HTTP/1.1 200 OK\\nContent-Type; text/html; charset=UTF-8\\n\\nhtml>Hola " + socket.getInetAddress().getHostName() + "</html>";
		try {
			bufferSortida.write(web.getBytes());
			System.out.println("Enviando: /n/n" +web);
			bufferSortida.flush();
		}catch(Exception e){
			System.out.println("Error a enviar: "+e.getMessage());
		}
	}
	
	public void tancar() {
		try {
			serverSocket.close();
			socket.close();
			bufferEntrada.close();
			bufferSortida.close();
		}catch(Exception e) {
			System.out.println("Error a cerrar: "+e.getMessage());
		}
	}
	
	public void runServidor(int port) {
		while(true) {
			try {
				connectar(port);
				canals();
				rebre();
				enviar();
			}catch (Exception e) {
				System.out.println("Error a runServer: "+e.getMessage());
			}finally {
				tancar();
			}
		}
	}
	
	public static void main (String[] arg) {
		Scanner teclat = new Scanner (System.in);
		Servidor s = new Servidor();
		System.out.println("Introduce el puerto: ");
		int port = teclat.nextInt();
		teclat.close();
		while (true) {
			s.runServidor(port);
		}
	}
}
