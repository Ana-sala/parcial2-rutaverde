package co.edu.uniquindio.rutaverde.modelo.fachada;

import co.edu.uniquindio.rutaverde.modelo.AgenciaRutaVerde;
import co.edu.uniquindio.rutaverde.modelo.Reserva;
import co.edu.uniquindio.rutaverde.modelo.pago.PasarelaPago;
import co.edu.uniquindio.rutaverde.modelo.pago.ResultadoPago;
import co.edu.uniquindio.rutaverde.modelo.pago.ValidadorLuhn;
import co.edu.uniquindio.rutaverde.modelo.producto.Actividad;
import co.edu.uniquindio.rutaverde.modelo.voucher.GeneradorVoucher;

/**
 * Facade: una sola puerta para confirmar reservas desde el asesor, el kiosco o la app movil.
 * Ninguna pantalla conoce ni repite los pasos.
 */
public class FachadaReservas {
    private final AgenciaRutaVerde agencia;
    private final PasarelaPago pasarela;
    private final ValidadorLuhn validador;
    private final ServicioCorreo correo;

    public FachadaReservas(AgenciaRutaVerde agencia, PasarelaPago pasarela, ServicioCorreo correo) {
        this.agencia = agencia;
        this.pasarela = pasarela;
        this.correo = correo;
        this.validador = new ValidadorLuhn();
    }

    /**
     * RN-13, en este orden exacto: cupo, tarjeta, cobro, descontar cupos, registrar, voucher, correo.
     * Si un paso falla, el cliente recibe un correo con el motivo y no se descuenta ningun cupo.
     */
    public String confirmarReserva(Reserva reserva, String numeroTarjeta, GeneradorVoucher generador) {
        if (reserva == null) {
            throw new IllegalStateException("No hay reserva para confirmar");
        }
        String destino = reserva.getCliente().getCorreo();
        try {
            verificarCupos(reserva);                                              // 1
            if (!validador.esValida(numeroTarjeta)) {                             // 2
                throw new IllegalStateException("La tarjeta no es valida (no supera la verificacion de Luhn)");
            }
            String referencia = "RV-" + reserva.getCliente().getDocumento() + "-" + reserva.getFechaViaje();
            ResultadoPago pago = pasarela.cobrar(numeroTarjeta, reserva.calcularValor(), referencia);   // 3
            if (!pago.isAprobado()) {
                throw new IllegalStateException("Pago rechazado: " + pago.getMotivo());
            }
            descontarCupos(reserva);                                              // 4
            int numero = agencia.registrarReserva(reserva);                      // 5
            String voucher = generador.emitir(reserva);                          // 6
            correo.enviar(destino, "Reserva No. " + numero + " confirmada", voucher
                    + "\nAutorizacion del pago: " + pago.getCodigoAutorizacion());  // 7
            return voucher;
        } catch (IllegalStateException e) {
            correo.enviar(destino, "No fue posible confirmar su reserva", "Motivo: " + e.getMessage());
            throw e;
        }
    }

    private void verificarCupos(Reserva reserva) {
        if (reserva.isConfirmada()) {
            throw new IllegalStateException("La reserva ya fue confirmada");
        }
        for (Actividad a : reserva.getProducto().obtenerActividades()) {
            if (!a.hayCupo(reserva.getFechaViaje(), reserva.getViajeros())) {
                throw new IllegalStateException("No hay cupo en " + a.getNombre() + " para el "
                        + reserva.getFechaViaje() + " (libres: " + a.cuposLibres(reserva.getFechaViaje()) + ")");
            }
        }
    }

    /** Solo se llama cuando todas las verificaciones y el cobro ya pasaron. */
    private void descontarCupos(Reserva reserva) {
        for (Actividad a : reserva.getProducto().obtenerActividades()) {
            a.ocuparCupos(reserva.getFechaViaje(), reserva.getViajeros());
        }
    }
}
