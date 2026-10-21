package co.edu.uniquindio.rutaverde.modelo;

public class Cliente {
    private final String documento;
    private final String nombreCompleto;
    private final String telefono;
    private final String correo;
    private final String paisOrigen;
    private final String idiomaPreferido;

    public Cliente(String documento, String nombreCompleto, String telefono, String correo,
                   String paisOrigen, String idiomaPreferido) {
        this.documento = documento;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.correo = correo;
        this.paisOrigen = paisOrigen;
        this.idiomaPreferido = idiomaPreferido;
    }

    public String getDocumento() { return documento; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    public String getPaisOrigen() { return paisOrigen; }
    public String getIdiomaPreferido() { return idiomaPreferido; }

    @Override
    public String toString() { return nombreCompleto + " (" + idiomaPreferido + ")"; }
}
