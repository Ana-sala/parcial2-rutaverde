package co.edu.uniquindio.rutaverde.modelo.pago;

/** Respuesta de un cobro en los terminos de la agencia. */
public class ResultadoPago {
    private final boolean aprobado;
    private final String motivo;
    private final String codigoAutorizacion;

    private ResultadoPago(boolean aprobado, String motivo, String codigoAutorizacion) {
        this.aprobado = aprobado;
        this.motivo = motivo;
        this.codigoAutorizacion = codigoAutorizacion;
    }

    public static ResultadoPago aprobado(String codigoAutorizacion) {
        return new ResultadoPago(true, "Aprobado", codigoAutorizacion);
    }

    public static ResultadoPago rechazado(String motivo) {
        return new ResultadoPago(false, motivo, null);
    }

    public boolean isAprobado() { return aprobado; }
    public String getMotivo() { return motivo; }
    public String getCodigoAutorizacion() { return codigoAutorizacion; }

    @Override
    public String toString() {
        return aprobado ? "Aprobado (" + codigoAutorizacion + ")" : "Rechazado: " + motivo;
    }
}
