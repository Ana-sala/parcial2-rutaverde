package co.edu.uniquindio.rutaverde.modelo.voucher;

import co.edu.uniquindio.rutaverde.modelo.Reserva;

/** Detalle para el kiosco: una sola linea con el codigo para presentar en la entrada. */
public class TiqueteKiosco implements Voucher {

    @Override
    public String generarDetalle(Reserva r) {
        String codigo = String.format("RV-%04d-%02d%02d", r.getNumero(),
                r.getFechaViaje().getDayOfMonth(), r.getFechaViaje().getMonthValue());
        return String.format("%s | %s | %s | %d viajeros | $%,.0f", codigo, r.getProducto().getNombre(),
                r.getFechaViaje(), r.getViajeros(), r.calcularValor());
    }
}
