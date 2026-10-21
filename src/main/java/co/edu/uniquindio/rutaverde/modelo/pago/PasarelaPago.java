package co.edu.uniquindio.rutaverde.modelo.pago;

/** Objetivo del Adapter: la interfaz que la agencia entiende (pesos y resultado propio). */
public interface PasarelaPago {
    ResultadoPago cobrar(String numeroTarjeta, double valorPesos, String referencia);
}
