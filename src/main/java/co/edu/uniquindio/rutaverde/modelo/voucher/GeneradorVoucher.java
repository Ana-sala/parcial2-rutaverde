package co.edu.uniquindio.rutaverde.modelo.voucher;

import co.edu.uniquindio.rutaverde.modelo.Reserva;

/** Creador del Factory Method: el proceso de emision es siempre el mismo. */
public abstract class GeneradorVoucher {
    private final String nombreAgencia;
    private final String rnt;

    protected GeneradorVoucher(String nombreAgencia, String rnt) {
        this.nombreAgencia = nombreAgencia;
        this.rnt = rnt;
    }

    /** Metodo fabrica: cada canal decide que voucher crear. */
    protected abstract Voucher crearVoucher();

    public final String emitir(Reserva reserva) {
        Voucher voucher = crearVoucher();
        return encabezado() + "\n" + voucher.generarDetalle(reserva) + "\n" + pie();
    }

    private String encabezado() {
        return "=== " + nombreAgencia + " - RNT " + rnt + " ===";
    }

    private String pie() {
        return "Cancelacion sin costo hasta 48 horas antes del viaje.";
    }
}
