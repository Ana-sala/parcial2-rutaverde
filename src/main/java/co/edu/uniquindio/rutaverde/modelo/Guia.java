package co.edu.uniquindio.rutaverde.modelo;

import java.util.List;

public class Guia {
    private static final int HORAS_POR_DIA = 8;

    private final String identificacion;
    private final String nombre;
    private final List<String> idiomas;
    private final String telefono;
    private final double tarifaPorDia;

    public Guia(String identificacion, String nombre, List<String> idiomas, String telefono,
                double tarifaPorDia) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.idiomas = List.copyOf(idiomas);
        this.telefono = telefono;
        this.tarifaPorDia = tarifaPorDia;
    }

    /** RN-08: el guia debe hablar el idioma del cliente (sin importar mayusculas). */
    public boolean hablaIdioma(String idioma) {
        if (idioma == null) {
            return false;
        }
        for (String i : idiomas) {
            if (i.trim().equalsIgnoreCase(idioma.trim())) {
                return true;
            }
        }
        return false;
    }

    /** RN-11: un dia cubre hasta 8 horas; si el producto dura mas, se cobran dias adicionales. */
    public double calcularCosto(double horas) {
        int dias = (int) Math.ceil(horas / HORAS_POR_DIA);
        return Math.max(1, dias) * tarifaPorDia;
    }

    public String getIdentificacion() { return identificacion; }
    public String getNombre() { return nombre; }
    public List<String> getIdiomas() { return idiomas; }
    public String getTelefono() { return telefono; }
    public double getTarifaPorDia() { return tarifaPorDia; }

    @Override
    public String toString() { return nombre + " " + idiomas; }
}
