package co.edu.uniquindio.rutaverde.modelo;

import co.edu.uniquindio.rutaverde.modelo.producto.Actividad;
import co.edu.uniquindio.rutaverde.modelo.producto.ProductoTuristico;
import java.time.LocalDate;

public class Reserva {
    private static final double TRANSPORTE_POR_VIAJERO = 35_000;
    private static final double SEGURO_POR_VIAJERO = 15_000;
    private static final int MAX_VIAJEROS = 15;

    private final Cliente cliente;
    private final ProductoTuristico producto;
    private final LocalDate fechaViaje;
    private final int viajeros;
    private final Guia guia;
    private final boolean transporte;
    private final boolean seguro;
    private final String observaciones;
    private int numero;            // se asigna al confirmar
    private boolean confirmada;

    private Reserva(Builder b) {
        this.cliente = b.cliente;
        this.producto = b.producto;
        this.fechaViaje = b.fechaViaje;
        this.viajeros = b.viajeros;
        this.guia = b.guia;
        this.transporte = b.transporte;
        this.seguro = b.seguro;
        this.observaciones = b.observaciones;
    }

    public double calcularValorProducto() {
        return producto.calcularPrecioPorPersona() * viajeros;
    }

    public double calcularValorTransporte() {
        return transporte ? TRANSPORTE_POR_VIAJERO * viajeros : 0;
    }

    public double calcularValorSeguro() {
        return seguro ? SEGURO_POR_VIAJERO * viajeros : 0;
    }

    /** El guia se cobra por grupo segun la duracion del producto (RN-11). */
    public double calcularValorGuia() {
        return guia == null ? 0 : guia.calcularCosto(producto.calcularDuracionHoras());
    }

    /** RN-10: precio por persona x viajeros + opcionales. */
    public double calcularValor() {
        return calcularValorProducto() + calcularValorTransporte() + calcularValorSeguro() + calcularValorGuia();
    }

    /** Solo la agencia la confirma al registrarla con su numero consecutivo. */
    public void confirmar(int numero) {
        if (confirmada) {
            throw new IllegalStateException("La reserva ya fue confirmada con el numero " + this.numero);
        }
        this.numero = numero;
        this.confirmada = true;
    }

    public boolean isConfirmada() { return confirmada; }
    public int getNumero() { return numero; }
    public Cliente getCliente() { return cliente; }
    public ProductoTuristico getProducto() { return producto; }
    public LocalDate getFechaViaje() { return fechaViaje; }
    public int getViajeros() { return viajeros; }
    public Guia getGuia() { return guia; }
    public boolean tieneTransporte() { return transporte; }
    public boolean tieneSeguro() { return seguro; }
    public String getObservaciones() { return observaciones; }

    @Override
    public String toString() {
        return (confirmada ? "Reserva #" + numero : "Reserva sin confirmar") + " | " + cliente.getNombreCompleto()
                + " | " + producto.getNombre() + " | " + fechaViaje + " | " + viajeros + " viajeros"
                + String.format(" | $%,.0f", calcularValor());
    }

    public static class Builder {
        private Cliente cliente;
        private ProductoTuristico producto;
        private LocalDate fechaViaje;
        private int viajeros;
        private Guia guia;
        private boolean transporte;
        private boolean seguro;
        private String observaciones;

        public Builder conCliente(Cliente c) { this.cliente = c; return this; }
        public Builder conProducto(ProductoTuristico p) { this.producto = p; return this; }
        public Builder conFechaViaje(LocalDate f) { this.fechaViaje = f; return this; }
        public Builder conViajeros(int n) { this.viajeros = n; return this; }
        public Builder conGuia(Guia g) { this.guia = g; return this; }
        public Builder conTransporte() { this.transporte = true; return this; }
        public Builder conSeguro() { this.seguro = true; return this; }
        public Builder conObservaciones(String o) { this.observaciones = o; return this; }

        /** Valida todas las reglas antes de que la reserva exista. */
        public Reserva build() {
            if (cliente == null) {
                throw new IllegalStateException("La reserva requiere un cliente");
            }
            if (producto == null) {
                throw new IllegalStateException("La reserva requiere un producto");
            }
            if (fechaViaje == null) {
                throw new IllegalStateException("La reserva requiere la fecha del viaje");
            }
            if (fechaViaje.isBefore(LocalDate.now())) {
                throw new IllegalStateException("La fecha del viaje no puede estar en el pasado");
            }
            if (viajeros < 1 || viajeros > MAX_VIAJEROS) {
                throw new IllegalStateException("El grupo debe tener entre 1 y " + MAX_VIAJEROS + " viajeros");
            }
            if (guia != null && !guia.hablaIdioma(cliente.getIdiomaPreferido())) {
                throw new IllegalStateException("El guia " + guia.getNombre() + " no habla "
                        + cliente.getIdiomaPreferido() + ", el idioma del cliente");
            }
            if (producto.obtenerActividades().isEmpty()) {
                throw new IllegalStateException("El producto " + producto.getNombre() + " no tiene actividades");
            }
            for (Actividad a : producto.obtenerActividades()) {
                if (!a.estaDisponible()) {
                    throw new IllegalStateException("No se puede reservar: la actividad " + a.getNombre()
                            + " esta suspendida");
                }
            }
            return new Reserva(this);
        }
    }
}
