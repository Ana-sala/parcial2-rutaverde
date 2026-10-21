package co.edu.uniquindio.rutaverde.app;

import co.edu.uniquindio.rutaverde.controlador.PrincipalControlador;
import co.edu.uniquindio.rutaverde.modelo.AgenciaRutaVerde;
import co.edu.uniquindio.rutaverde.modelo.fachada.FachadaReservas;
import co.edu.uniquindio.rutaverde.modelo.fachada.ServicioCorreo;
import co.edu.uniquindio.rutaverde.modelo.pago.AdaptadorPagoAndino;
import co.edu.uniquindio.rutaverde.modelo.pago.PagoAndinoAPI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RutaVerdeApp extends Application {

    private static final String RUTA_VISTA = "/co/edu/uniquindio/rutaverde/vista/";

    @Override
    public void start(Stage stage) throws Exception {
        // Modelo
        AgenciaRutaVerde agencia = DatosIniciales.crearAgencia();
        ServicioCorreo correo = new ServicioCorreo();
        FachadaReservas fachada = new FachadaReservas(agencia, new AdaptadorPagoAndino(new PagoAndinoAPI()), correo);

        // Vista
        FXMLLoader loader = new FXMLLoader(RutaVerdeApp.class.getResource(RUTA_VISTA + "principal-view.fxml"));
        Parent root = loader.load();

        // Controlador
        PrincipalControlador controlador = loader.getController();
        controlador.inicializar(agencia, fachada, correo);

        Scene scene = new Scene(root, 1200, 760);
        scene.getStylesheets().add(RutaVerdeApp.class.getResource(RUTA_VISTA + "estilos.css").toExternalForm());
        stage.setTitle("RutaVerde Quindio - Gestion de reservas");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
