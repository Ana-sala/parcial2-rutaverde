package co.edu.uniquindio.rutaverde.modelo.producto;

import java.util.List;

/** Componente del Composite: una actividad suelta y un paquete se consultan igual. */
public interface ProductoTuristico {
    String getCodigo();
    String getNombre();
    double calcularPrecioPorPersona();
    double calcularDuracionHoras();
    List<Actividad> obtenerActividades();
    boolean contiene(ProductoTuristico producto);
}
