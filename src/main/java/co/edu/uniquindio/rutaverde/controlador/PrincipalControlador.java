package co.edu.uniquindio.rutaverde.controlador;

import co.edu.uniquindio.rutaverde.modelo.AgenciaRutaVerde;
import co.edu.uniquindio.rutaverde.modelo.fachada.FachadaReservas;
import co.edu.uniquindio.rutaverde.modelo.fachada.ServicioCorreo;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;

public class PrincipalControlador {

    @FXML private Label lblTitulo;
    @FXML private Label lblAgencia;
    @FXML private TabPane tabPane;
    @FXML private ProductosControlador productosController;
    @FXML private ClientesControlador clientesController;
    @FXML private ReservasControlador reservasController;
    @FXML private KioscoControlador kioscoController;
    @FXML private ConsultasControlador consultasController;

    public void inicializar(AgenciaRutaVerde agencia, FachadaReservas fachada, ServicioCorreo correo) {
        lblTitulo.setText(agencia.getNombre());
        lblAgencia.setText("NIT " + agencia.getNit() + "  ·  RNT " + agencia.getRnt() + "  ·  "
                + agencia.getDireccion() + "  ·  " + agencia.getWeb());
        productosController.inicializar(agencia);
        clientesController.inicializar(agencia);
        reservasController.inicializar(agencia, fachada);
        kioscoController.inicializar(agencia, fachada);
        consultasController.inicializar(agencia, correo);
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, antes, ahora) -> refrescar());
    }

    /** Al cambiar de pestana, cada una vuelve a leer el modelo. */
    private void refrescar() {
        productosController.refrescar();
        clientesController.refrescar();
        reservasController.refrescar();
        kioscoController.refrescar();
        consultasController.refrescar();
    }
}
