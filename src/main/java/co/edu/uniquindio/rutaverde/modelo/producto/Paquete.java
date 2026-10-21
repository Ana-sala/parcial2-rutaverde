package co.edu.uniquindio.rutaverde.modelo.producto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Compuesto del Composite: contiene actividades y otros paquetes. */
public class Paquete implements ProductoTuristico {
    private static final double DESCUENTO_MAXIMO = 20.0;

    private final String codigo;
    private final String nombre;
    private final double descuento;   // porcentaje de 0 a 20
    private final List<ProductoTuristico> productos = new ArrayList<>();

    public Paquete(String codigo, String nombre, double descuento) {
        if (descuento < 0 || descuento > DESCUENTO_MAXIMO) {
            throw new IllegalArgumentException("El descuento del paquete debe estar entre 0% y 20%");
        }
        this.codigo = codigo;
        this.nombre = nombre;
        this.descuento = descuento;
    }

    public void agregar(ProductoTuristico producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        // RN-02: si el producto ya me contiene (o soy yo mismo), se formaria un ciclo
        if (producto.contiene(this)) {
            throw new IllegalArgumentException("Un paquete no puede contenerse a si mismo: "
                    + producto.getNombre() + " ya contiene a " + nombre);
        }
        // RN-03: ninguna actividad del producto puede estar ya en este plan
        for (Actividad a : producto.obtenerActividades()) {
            if (this.contiene(a)) {
                throw new IllegalArgumentException("La actividad " + a.getNombre()
                        + " ya esta incluida en " + nombre);
            }
        }
        productos.add(producto);
    }

    public void quitar(ProductoTuristico producto) {
        productos.remove(producto);
    }

    /** RN-04: suma de lo que contiene con el descuento propio aplicado. */
    @Override
    public double calcularPrecioPorPersona() {
        double suma = 0;
        for (ProductoTuristico p : productos) {
            suma += p.calcularPrecioPorPersona();
        }
        return suma * (1 - descuento / 100);
    }

    /** RN-04: suma de las duraciones. */
    @Override
    public double calcularDuracionHoras() {
        double suma = 0;
        for (ProductoTuristico p : productos) {
            suma += p.calcularDuracionHoras();
        }
        return suma;
    }

    @Override
    public List<Actividad> obtenerActividades() {
        List<Actividad> lista = new ArrayList<>();
        for (ProductoTuristico p : productos) {
            lista.addAll(p.obtenerActividades());
        }
        return lista;
    }

    @Override
    public boolean contiene(ProductoTuristico producto) {
        if (this == producto) {
            return true;
        }
        for (ProductoTuristico p : productos) {
            if (p.contiene(producto)) {
                return true;
            }
        }
        return false;
    }

    @Override public String getCodigo() { return codigo; }
    @Override public String getNombre() { return nombre; }
    public double getDescuento() { return descuento; }
    public List<ProductoTuristico> getProductos() { return Collections.unmodifiableList(productos); }

    @Override
    public String toString() { return codigo + " - " + nombre; }
}
