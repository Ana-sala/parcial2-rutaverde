package co.edu.uniquindio.rutaverde.modelo.fachada;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Envio de correo simulado: se imprime en consola y se guarda en una bandeja. */
public class ServicioCorreo {
    private final List<String> enviados = new ArrayList<>();

    public void enviar(String destino, String asunto, String cuerpo) {
        String correo = "Para: " + destino + "\nAsunto: " + asunto + "\n" + cuerpo;
        enviados.add(correo);
        System.out.println("[Correo] " + destino + " - " + asunto);
    }

    public List<String> getEnviados() { return Collections.unmodifiableList(enviados); }
}
