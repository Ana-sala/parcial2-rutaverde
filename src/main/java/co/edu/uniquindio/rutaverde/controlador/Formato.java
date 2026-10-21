package co.edu.uniquindio.rutaverde.controlador;

import javafx.scene.control.Label;

/** Utilidades de presentacion compartidas por los controladores (no contiene reglas de negocio). */
final class Formato {

    private Formato() { }

    static String dinero(double valor) {
        return String.format("$%,.0f", valor);
    }

    static void mensaje(Label label, String texto, boolean exito) {
        label.setText(texto);
        label.getStyleClass().setAll(exito ? "mensaje-exito" : "mensaje-error");
    }

    static int entero(String texto, String campo) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalStateException(campo + " debe ser un numero entero");
        }
    }

    static double decimal(String texto, String campo) {
        try {
            return Double.parseDouble(texto.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalStateException(campo + " debe ser un numero");
        }
    }
}
