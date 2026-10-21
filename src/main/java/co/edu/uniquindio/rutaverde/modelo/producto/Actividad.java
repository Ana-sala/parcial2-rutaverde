package co.edu.uniquindio.rutaverde.modelo.producto;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Hoja del Composite. */
public class Actividad implements ProductoTuristico {
    private final String codigo;
    private final String nombre;
    private final String municipio;
    private final double duracionHoras;
    private final double precioPorPersona;
    private final int cupoMaximoDia;
    private final Dificultad dificultad;
    private EstadoActividad estado;
    private final Map<LocalDate, Integer> cuposOcupados = new HashMap<>();

    public Actividad(String codigo, String nombre, String municipio, double duracionHoras,
                     double precioPorPersona, int cupoMaximoDia, Dificultad dificultad,
                     EstadoActividad estado) {
        if (duracionHoras <= 0 || precioPorPersona < 0 || cupoMaximoDia <= 0) {
            throw new IllegalArgumentException("Datos invalidos para la actividad " + nombre);
        }
        this.codigo = codigo;
        this.nombre = nombre;
        this.municipio = municipio;
        this.duracionHoras = duracionHoras;
        this.precioPorPersona = precioPorPersona;
        this.cupoMaximoDia = cupoMaximoDia;
        this.dificultad = dificultad;
        this.estado = estado;
    }

    @Override
    public double calcularPrecioPorPersona() { return precioPorPersona; }

    @Override
    public double calcularDuracionHoras() { return duracionHoras; }

    /** Una actividad suelta se devuelve a si misma. */
    @Override
    public List<Actividad> obtenerActividades() { return List.of(this); }

    /** Una actividad solo "contiene" a si misma. */
    @Override
    public boolean contiene(ProductoTuristico producto) { return this == producto; }

    public boolean estaDisponible() { return estado == EstadoActividad.DISPONIBLE; }

    public int cuposLibres(LocalDate fecha) {
        return cupoMaximoDia - cuposOcupados.getOrDefault(fecha, 0);
    }

    public boolean hayCupo(LocalDate fecha, int viajeros) {
        return cuposLibres(fecha) >= viajeros;
    }

    /** Evita la sobreventa (RN-14). */
    public void ocuparCupos(LocalDate fecha, int viajeros) {
        if (!hayCupo(fecha, viajeros)) {
            throw new IllegalStateException("No hay cupo en " + nombre + " para el " + fecha
                    + " (libres: " + cuposLibres(fecha) + ")");
        }
        cuposOcupados.merge(fecha, viajeros, Integer::sum);
    }

    @Override public String getCodigo() { return codigo; }
    @Override public String getNombre() { return nombre; }
    public String getMunicipio() { return municipio; }
    public double getDuracionHoras() { return duracionHoras; }
    public double getPrecioPorPersona() { return precioPorPersona; }
    public int getCupoMaximoDia() { return cupoMaximoDia; }
    public Dificultad getDificultad() { return dificultad; }
    public EstadoActividad getEstado() { return estado; }
    public void setEstado(EstadoActividad estado) { this.estado = estado; }

    @Override
    public String toString() { return codigo + " - " + nombre; }
}
