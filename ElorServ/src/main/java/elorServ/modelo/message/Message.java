package elorServ.modelo.message;

import elorServ.modelo.entities.Users;

public class Message {
    private String tipo;
    private String usuario;
    private String password;
    private String estado;
    private String mensaje;
    private Users userData;

    public Message() {
    }

    // Constructor para login (cliente -> servidor)
    public static Message crearLogin(String usuario, String password) {
        Message msg = new Message();
        msg.tipo = "LOGIN";
        msg.usuario = usuario;
        msg.password = password;
        return msg;
    }

    // Constructor para respuestas simples (servidor -> cliente)
    public static Message crearRespuesta(String tipo, String estado, String mensaje) {
        Message msg = new Message();
        msg.tipo = tipo;
        msg.estado = estado;
        msg.mensaje = mensaje;
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

    @Override
    public String toString() {
        return "Message{" +
                "tipo='" + tipo + '\'' +
                ", usuario='" + usuario + '\'' +
                ", estado='" + estado + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", userData=" + userData +
                '}';
    }
}