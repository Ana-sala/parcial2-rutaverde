package co.edu.uniquindio.rutaverde.modelo.pago;

/** Adapter: traduce la interfaz de PagoAndino (centavos y codigos) a PasarelaPago (pesos y resultado). */
public class AdaptadorPagoAndino implements PasarelaPago {
    private final PagoAndinoAPI api;

    public AdaptadorPagoAndino(PagoAndinoAPI api) {
        this.api = api;
    }

    @Override
    public ResultadoPago cobrar(String numeroTarjeta, double valorPesos, String referencia) {
        long centavos = Math.round(valorPesos * 100);
        String tarjeta = numeroTarjeta.replace(" ", "");
        int codigo = api.procesarCobro(tarjeta, centavos, referencia);
        if (codigo == 0) {
            return ResultadoPago.aprobado(api.ultimaAutorizacion());
        }
        return ResultadoPago.rechazado(traducirCodigo(codigo));
    }

    private String traducirCodigo(int codigo) {
        switch (codigo) {
            case 51: return "Fondos insuficientes";
            case 14: return "Tarjeta invalida";
            case 91: return "Pasarela no disponible";
            default: return "Codigo de respuesta desconocido: " + codigo;
        }
    }
}
