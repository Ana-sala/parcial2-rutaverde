package co.edu.uniquindio.rutaverde.app;

import co.edu.uniquindio.rutaverde.modelo.AgenciaRutaVerde;
import co.edu.uniquindio.rutaverde.modelo.Cliente;
import co.edu.uniquindio.rutaverde.modelo.Guia;
import co.edu.uniquindio.rutaverde.modelo.producto.Actividad;
import co.edu.uniquindio.rutaverde.modelo.producto.Dificultad;
import co.edu.uniquindio.rutaverde.modelo.producto.EstadoActividad;
import co.edu.uniquindio.rutaverde.modelo.producto.Paquete;
import java.util.List;

/** Datos de ejemplo para que la aplicacion y la consola no arranquen vacias. */
public final class DatosIniciales {

    private DatosIniciales() { }

    public static AgenciaRutaVerde crearAgencia() {
        AgenciaRutaVerde agencia = new AgenciaRutaVerde("RutaVerde Quindio", "901234567-8", "98765",
                "Carrera 14 # 20-35, Armenia", "6067412233", "reservas@rutaverde.co", "www.rutaverde.co");

        Actividad cocora = new Actividad("A01", "Caminata Valle de Cocora", "Salento", 6, 80_000, 15,
                Dificultad.MEDIA, EstadoActividad.DISPONIBLE);
        Actividad finca = new Actividad("A02", "Recorrido finca cafetera", "Salento", 4, 60_000, 20,
                Dificultad.BAJA, EstadoActividad.DISPONIBLE);
        Actividad cafe = new Actividad("A03", "Parque del Cafe", "Montenegro", 8, 120_000, 30,
                Dificultad.BAJA, EstadoActividad.DISPONIBLE);
        Actividad panaca = new Actividad("A04", "Panaca", "Quimbaya", 8, 100_000, 30,
                Dificultad.BAJA, EstadoActividad.DISPONIBLE);
        Actividad aves = new Actividad("A05", "Avistamiento de aves", "Filandia", 4, 90_000, 8,
                Dificultad.MEDIA, EstadoActividad.DISPONIBLE);
        Actividad rafting = new Actividad("A06", "Rafting rio Barragan", "Genova", 5, 150_000, 10,
                Dificultad.ALTA, EstadoActividad.SUSPENDIDA);

        Paquete salento = new Paquete("P01", "Paquete Salento", 10);
        salento.agregar(cocora);
        salento.agregar(finca);
        Paquete parques = new Paquete("P02", "Paquete Parques", 5);
        parques.agregar(cafe);
        parques.agregar(panaca);
        Paquete total = new Paquete("P03", "Quindio Total", 15);
        total.agregar(salento);
        total.agregar(parques);
        total.agregar(aves);
        Paquete aventura = new Paquete("P04", "Aventura Extrema", 10);
        aventura.agregar(rafting);
        aventura.agregar(cocora);

        for (var p : List.of(cocora, finca, cafe, panaca, aves, rafting, salento, parques, total, aventura)) {
            agencia.registrarProducto(p);
        }

        agencia.registrarCliente(new Cliente("1094000111", "Laura Gomez", "3101234567",
                "laura@mail.com", "Colombia", "Espanol"));
        agencia.registrarCliente(new Cliente("P-55667788", "John Smith", "+1 202 555 0147",
                "john@mail.com", "Estados Unidos", "Ingles"));
        agencia.registrarCliente(new Cliente("P-11223344", "Claire Martin", "+33 6 12 34 56 78",
                "claire@mail.com", "Francia", "Frances"));

        agencia.registrarGuia(new Guia("G01", "Carlos Rios", List.of("Espanol", "Ingles"), "3159876543", 200_000));
        agencia.registrarGuia(new Guia("G02", "Marta Lopez", List.of("Espanol"), "3201112233", 150_000));
        agencia.registrarGuia(new Guia("G03", "Pierre Dubois", List.of("Frances", "Espanol"), "3004445566", 220_000));
        return agencia;
    }
}
