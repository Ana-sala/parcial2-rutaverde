package co.edu.uniquindio.rutaverde.modelo.voucher;

import co.edu.uniquindio.rutaverde.modelo.Reserva;

/** Producto del Factory Method: lo unico que cambia entre canales es el detalle. */
public interface Voucher {
    String generarDetalle(Reserva reserva);
}
