package co.edu.uniquindio.rutaverde.modelo;

import co.edu.uniquindio.rutaverde.modelo.producto.ProductoTuristico;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AgenciaRutaVerde {
    private final String nombre;
    private final String nit;
    private final String rnt;
    private final String direccion;
    private final String telefono;
    private final String correo;
    private final String web;
    private int consecutivoReservas = 0;
    private final List<Cliente> clientes = new ArrayList<>();
    private final List<Guia> guias = new ArrayList<>();
    private final List<ProductoTuristico> productos = new ArrayList<>();
    private final List<Reserva> reservas = new ArrayList<>();

    public AgenciaRutaVerde(String nombre, String nit, String rnt, String direccion,
                            String telefono, String correo, String web) {
        this.nombre = nombre;
        this.nit = nit;
        this.rnt = rnt;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.web = web;
    }

    public void registrarCliente(Cliente c) {
        requerido(c == null || vacio(c.getDocumento()) || vacio(c.getNombreCompleto()),
                "El cliente requiere documento y nombre");
        requerido(vacio(c.getIdiomaPreferido()), "El cliente requiere un idioma preferido");
        for (Cliente e : clientes) {
            requerido(e.getDocumento().equalsIgnoreCase(c.getDocumento()),
                    "Ya existe un cliente con documento " + c.getDocumento());
        }
        clientes.add(c);
    }

    public void registrarGuia(Guia g) {
        requerido(g == null || vacio(g.getIdentificacion()) || vacio(g.getNombre()),
                "El guia requiere identificacion y nombre");
        requerido(g.getIdiomas().isEmpty(), "El guia debe hablar al menos un idioma");
        for (Guia e : guias) {
            requerido(e.getIdentificacion().equalsIgnoreCase(g.getIdentificacion()),
                    "Ya existe un guia con identificacion " + g.getIdentificacion());
        }
        guias.add(g);
    }

    public void registrarProducto(ProductoTuristico p) {
        requerido(p == null || vacio(p.getCodigo()) || vacio(p.getNombre()),
                "El producto requiere codigo y nombre");
        for (ProductoTuristico e : productos) {
            requerido(e.getCodigo().equalsIgnoreCase(p.getCodigo()),
                    "Ya existe un producto con codigo " + p.getCodigo());
        }
        productos.add(p);
    }

    /** Paso 5 de la confirmacion: asigna el numero consecutivo y guarda la reserva. */
    public int registrarReserva(Reserva r) {
        requerido(r == null, "No hay reserva para registrar");
        requerido(r.isConfirmada(), "La reserva ya fue registrada");
        consecutivoReservas++;
        r.confirmar(consecutivoReservas);
        reservas.add(r);
        return consecutivoReservas;
    }

    /** Reservas confirmadas con viaje entre inicio y fin, ambas fechas incluidas (RN-15). */
    public double calcularIngresosPeriodo(LocalDate inicio, LocalDate fin) {
        requerido(inicio == null || fin == null, "Seleccione las dos fechas del periodo");
        requerido(inicio.isAfter(fin), "La fecha inicial no puede ser posterior a la final");
        double total = 0;
        for (Reserva r : reservas) {
            LocalDate f = r.getFechaViaje();
            if (r.isConfirmada() && !f.isBefore(inicio) && !f.isAfter(fin)) {
                total += r.calcularValor();
            }
        }
        return total;
    }

    /** Acumula los viajeros de las reservas confirmadas por producto. */
    public Map<ProductoTuristico, Integer> calcularViajerosPorProducto() {
        Map<ProductoTuristico, Integer> viajeros = new LinkedHashMap<>();
        for (Reserva r : reservas) {
            if (r.isConfirmada()) {
                viajeros.merge(r.getProducto(), r.getViajeros(), Integer::sum);
            }
        }
        return viajeros;
    }

    /** Recorre los acumulados e identifica el mayor. */
    public ProductoTuristico productoConMasViajeros() {
        ProductoTuristico mayor = null;
        int maximo = 0;
        for (Map.Entry<ProductoTuristico, Integer> e : calcularViajerosPorProducto().entrySet()) {
            if (e.getValue() > maximo) {
                maximo = e.getValue();
                mayor = e.getKey();
            }
        }
        requerido(mayor == null, "Todavia no hay reservas confirmadas");
        return mayor;
    }

    private static boolean vacio(String s) { return s == null || s.isBlank(); }

    private static void requerido(boolean falla, String mensaje) {
        if (falla) {
            throw new IllegalStateException(mensaje);
        }
    }

    public List<Cliente> getClientes() { return Collections.unmodifiableList(clientes); }
    public List<Guia> getGuias() { return Collections.unmodifiableList(guias); }
    public List<ProductoTuristico> getProductos() { return Collections.unmodifiableList(productos); }
    public List<Reserva> getReservas() { return Collections.unmodifiableList(reservas); }
    public String getNombre() { return nombre; }
    public String getNit() { return nit; }
    public String getRnt() { return rnt; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    public String getWeb() { return web; }
}
