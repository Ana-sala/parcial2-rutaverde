package co.edu.uniquindio.rutaverde.modelo.pago;

/** Valida un numero de tarjeta con el algoritmo de Luhn (RN-12). */
public class ValidadorLuhn {

    public boolean esValida(String numeroTarjeta) {
        if (numeroTarjeta == null) {
            return false;
        }
        String digitos = numeroTarjeta.replace(" ", "");
        if (digitos.length() < 13 || digitos.length() > 19 || !digitos.matches("\\d+")) {
            return false;
        }
        int suma = 0;
        boolean duplicar = false;   // el ultimo digito no se duplica; el penultimo si
        for (int i = digitos.length() - 1; i >= 0; i--) {
            int d = digitos.charAt(i) - '0';
            if (duplicar) {
                d *= 2;
                if (d > 9) {
                    d -= 9;
                }
            }
            suma += d;
            duplicar = !duplicar;
        }
        return suma % 10 == 0;
    }
}
