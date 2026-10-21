package co.edu.uniquindio.rutaverde.modelo.voucher;

import co.edu.uniquindio.rutaverde.modelo.Reserva;
import co.edu.uniquindio.rutaverde.modelo.producto.Actividad;

/** Detalle para la pantalla del asesor: cada actividad, el guia y los servicios incluidos. */
public class VoucherDetallado implements Voucher {

    @Override
    public String generarDetalle(Reserva r) {
        StringBuilder sb = new StringBuilder();
        sb.append("Reserva No. ").append(r.getNumero()).append('\n');
        sb.append("Cliente: ").append(r.getCliente().getNombreCompleto())
          .append(" (").append(r.getCliente().getDocumento()).append(")\n");
        sb.append("Producto: ").append(r.getProducto().getNombre()).append('\n');
        sb.append("Fecha del viaje: ").append(r.getFechaViaje())
          .append("   Viajeros: ").append(r.getViajeros()).append('\n');
        sb.append("Actividades:\n");
        for (Actividad a : r.getProducto().obtenerActividades()) {
            sb.append(String.format("  - %s (%s, %.0f h)%n", a.getNombre(), a.getMunicipio(), a.getDuracionHoras()));
        }
        sb.append("Guia: ").append(r.getGuia() == null ? "sin guia" : r.getGuia().getNombre()).append('\n');
        sb.append("Servicios: ")
          .append(r.tieneTransporte() ? "transporte desde el hotel  " : "")
          .append(r.tieneSeguro() ? "seguro de viaje" : "")
          .append(!r.tieneTransporte() && !r.tieneSeguro() ? "ninguno" : "").append('\n');
        if (r.getObservaciones() != null && !r.getObservaciones().isBlank()) {
            sb.append("Observaciones: ").append(r.getObservaciones()).append('\n');
        }
        sb.append(String.format("Producto: $%,.0f | Transporte: $%,.0f | Seguro: $%,.0f | Guia: $%,.0f%n",
                r.calcularValorProducto(), r.calcularValorTransporte(), r.calcularValorSeguro(), r.calcularValorGuia()));
        sb.append(String.format("TOTAL: $%,.0f", r.calcularValor()));
        return sb.toString();
    }
}
