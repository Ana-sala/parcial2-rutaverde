package co.edu.uniquindio.rutaverde.app;

import co.edu.uniquindio.rutaverde.modelo.AgenciaRutaVerde;
import co.edu.uniquindio.rutaverde.modelo.Cliente;
import co.edu.uniquindio.rutaverde.modelo.Guia;
import co.edu.uniquindio.rutaverde.modelo.Reserva;
import co.edu.uniquindio.rutaverde.modelo.fachada.FachadaReservas;
import co.edu.uniquindio.rutaverde.modelo.fachada.ServicioCorreo;
import co.edu.uniquindio.rutaverde.modelo.pago.AdaptadorPagoAndino;
import co.edu.uniquindio.rutaverde.modelo.pago.PagoAndinoAPI;
import co.edu.uniquindio.rutaverde.modelo.pago.PasarelaPago;
import co.edu.uniquindio.rutaverde.modelo.pago.ValidadorLuhn;
import co.edu.uniquindio.rutaverde.modelo.producto.Actividad;
import co.edu.uniquindio.rutaverde.modelo.producto.Paquete;
import co.edu.uniquindio.rutaverde.modelo.producto.ProductoTuristico;
import co.edu.uniquindio.rutaverde.modelo.voucher.GeneradorVoucher;
import co.edu.uniquindio.rutaverde.modelo.voucher.GeneradorVoucherAsesor;
import co.edu.uniquindio.rutaverde.modelo.voucher.GeneradorVoucherKiosco;
import java.time.LocalDate;
import java.util.List;

/** Pruebas del modelo en consola, sin interfaz grafica. */
public class MainConsola {
    private static final String TARJETA_VALIDA = "4539 1488 0343 6467";
    private static final String TARJETA_INVALIDA = "4539 1488 0343 6468";

    public static void main(String[] args) {
        AgenciaRutaVerde agencia = DatosIniciales.crearAgencia();
        ServicioCorreo correo = new ServicioCorreo();
        PasarelaPago pasarela = new AdaptadorPagoAndino(new PagoAndinoAPI());
        FachadaReservas fachada = new FachadaReservas(agencia, pasarela, correo);
        GeneradorVoucher asesor = new GeneradorVoucherAsesor(agencia.getNombre(), agencia.getRnt());
        GeneradorVoucher kiosco = new GeneradorVoucherKiosco(agencia.getNombre(), agencia.getRnt());

        Paquete salento = (Paquete) buscar(agencia, "P01");
        Paquete total = (Paquete) buscar(agencia, "P03");
        Actividad cocora = (Actividad) buscar(agencia, "A01");
        Actividad aves = (Actividad) buscar(agencia, "A05");
        Cliente laura = agencia.getClientes().get(0);
        Cliente john = agencia.getClientes().get(1);
        Guia carlos = agencia.getGuias().get(0);
        Guia marta = agencia.getGuias().get(1);
        LocalDate viaje = LocalDate.now().plusDays(30);

        // ---------- 1. Composite ----------
        titulo("1. Composite: se consulta igual una actividad o un paquete de varios niveles");
        for (String codigo : List.of("A05", "P01", "P02", "P03")) {
            ProductoTuristico p = buscar(agencia, codigo);
            System.out.printf("%-26s $%,10.0f  %4.0f h  %d actividades%n", p.getNombre(),
                    p.calcularPrecioPorPersona(), p.calcularDuracionHoras(), p.obtenerActividades().size());
        }
        System.out.println("Esperado: aves 90.000/4h - Salento 126.000/10h - Parques 209.000/16h - Quindio Total 361.250/30h/5");
        probar("Meter Quindio Total dentro de Paquete Salento", () -> salento.agregar(total));
        probar("Agregar otra vez Valle de Cocora a Quindio Total", () -> total.agregar(cocora));
        probar("Paquete con 25% de descuento", () -> new Paquete("X", "X", 25));

        // ---------- 2. Builder ----------
        titulo("2. Builder: la reserva valida sus reglas antes de existir");
        Reserva reserva = new Reserva.Builder()
                .conCliente(john).conProducto(total).conFechaViaje(viaje).conViajeros(2)
                .conGuia(carlos).conTransporte().conSeguro().conObservaciones("Vegetariano")
                .build();
        System.out.println(reserva);
        System.out.println("Esperado: 361.250 x 2 + 70.000 + 30.000 + 4 dias de guia x 200.000 = $1.622.500");
        probar("16 viajeros", () -> new Reserva.Builder().conCliente(laura).conProducto(salento)
                .conFechaViaje(viaje).conViajeros(16).build());
        probar("Fecha en el pasado", () -> new Reserva.Builder().conCliente(laura).conProducto(salento)
                .conFechaViaje(LocalDate.now().minusDays(1)).conViajeros(2).build());
        probar("Sin cliente", () -> new Reserva.Builder().conProducto(salento)
                .conFechaViaje(viaje).conViajeros(2).build());
        probar("Guia que no habla ingles", () -> new Reserva.Builder().conCliente(john).conProducto(salento)
                .conFechaViaje(viaje).conViajeros(2).conGuia(marta).build());
        probar("Paquete con actividad suspendida", () -> new Reserva.Builder().conCliente(laura)
                .conProducto(buscar(agencia, "P04")).conFechaViaje(viaje).conViajeros(2).build());

        // ---------- 3. Luhn + Adapter ----------
        titulo("3. Luhn y Adapter de PagoAndino");
        ValidadorLuhn luhn = new ValidadorLuhn();
        System.out.println(TARJETA_VALIDA + " -> " + luhn.esValida(TARJETA_VALIDA) + " (esperado true)");
        System.out.println(TARJETA_INVALIDA + " -> " + luhn.esValida(TARJETA_INVALIDA) + " (esperado false)");
        System.out.println("123 -> " + luhn.esValida("123") + " (esperado false)");
        System.out.println("Cobro de $3.251.250 -> " + pasarela.cobrar(TARJETA_VALIDA, 3_251_250, "PRUEBA")
                + " (esperado: Fondos insuficientes)");

        // ---------- 4. Facade ----------
        titulo("4. Facade: confirmacion en orden exacto");
        probar("Confirmar con tarjeta invalida", () -> fachada.confirmarReserva(reserva, TARJETA_INVALIDA, asesor));
        System.out.println("Cupos de aves despues del fallo: " + aves.cuposLibres(viaje) + " (esperado 8, intactos)");
        Reserva grande = new Reserva.Builder().conCliente(laura).conProducto(total)
                .conFechaViaje(viaje).conViajeros(9).build();
        probar("Grupo de 9 en Quindio Total (aves solo tiene 8 cupos)",
                () -> fachada.confirmarReserva(grande, TARJETA_VALIDA, asesor));
        Reserva cara = new Reserva.Builder().conCliente(laura).conProducto(total)
                .conFechaViaje(viaje).conViajeros(8).conGuia(carlos).conTransporte().conSeguro().build();
        probar("Cobro mayor a $3.000.000", () -> fachada.confirmarReserva(cara, TARJETA_VALIDA, asesor));
        System.out.println("Cupos de aves: " + aves.cuposLibres(viaje) + " (esperado 8, intactos)");

        // ---------- 5. Factory Method ----------
        titulo("5. Factory Method: el mismo proceso, voucher segun el canal");
        System.out.println("-- Asesor --");
        System.out.println(fachada.confirmarReserva(reserva, TARJETA_VALIDA, asesor));
        System.out.println("Cupos de aves: " + aves.cuposLibres(viaje) + " (esperado 6)");
        System.out.println("-- Kiosco --");
        Reserva kioscoRes = new Reserva.Builder().conCliente(laura).conProducto(salento)
                .conFechaViaje(viaje.plusDays(15)).conViajeros(4).build();
        System.out.println(fachada.confirmarReserva(kioscoRes, TARJETA_VALIDA, kiosco));

        // ---------- 6. Consultas ----------
        titulo("6. Consultas");
        System.out.printf("Ingresos del %s al %s (incluidas): $%,.0f%n", viaje, viaje.plusDays(15),
                agencia.calcularIngresosPeriodo(viaje, viaje.plusDays(15)));
        System.out.printf("Ingresos solo del %s: $%,.0f%n", viaje, agencia.calcularIngresosPeriodo(viaje, viaje));
        System.out.println("Producto con mas viajeros: " + agencia.productoConMasViajeros().getNombre()
                + " " + agencia.calcularViajerosPorProducto().values());
        System.out.println("Correos enviados: " + correo.getEnviados().size());
    }

    private static ProductoTuristico buscar(AgenciaRutaVerde agencia, String codigo) {
        for (ProductoTuristico p : agencia.getProductos()) {
            if (p.getCodigo().equals(codigo)) {
                return p;
            }
        }
        throw new IllegalArgumentException("No existe el producto " + codigo);
    }

    private static void titulo(String texto) {
        System.out.println("\n=== " + texto + " ===");
    }

    private static void probar(String caso, Runnable accion) {
        try {
            accion.run();
            System.out.println(caso + " -> ERROR: se acepto");
        } catch (RuntimeException e) {
            System.out.println(caso + " -> rechazado: " + e.getMessage());
        }
    }
}
