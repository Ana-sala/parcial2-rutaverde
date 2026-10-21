package co.edu.uniquindio.rutaverde.controlador;

import co.edu.uniquindio.rutaverde.modelo.AgenciaRutaVerde;
import co.edu.uniquindio.rutaverde.modelo.fachada.ServicioCorreo;
import co.edu.uniquindio.rutaverde.modelo.producto.ProductoTuristico;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import java.time.LocalDate;
import java.util.Map;

public class ConsultasControlador {

    @FXML private DatePicker dpDesde;
    @FXML private DatePicker dpHasta;
    @FXML private Label lblIngresos;
    @FXML private Label lblMensaje;
    @FXML private Label lblTop;
    @FXML private TableView<Map.Entry<ProductoTuristico, Integer>> tblViajeros;
    @FXML private TableColumn<Map.Entry<ProductoTuristico, Integer>, String> colProducto;
    @FXML private TableColumn<Map.Entry<ProductoTuristico, Integer>, String> colViajeros;
    @FXML private TextArea txtCorreos;

    private AgenciaRutaVerde agencia;
    private ServicioCorreo correo;

    @FXML
    private void initialize() {
        dpDesde.setValue(LocalDate.now());
        dpHasta.setValue(LocalDate.now().plusMonths(3));
        colProducto.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getKey().getNombre()));
        colViajeros.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getValue())));
    }

    public void inicializar(AgenciaRutaVerde agencia, ServicioCorreo correo) {
        this.agencia = agencia;
        this.correo = correo;
        refrescar();
    }

    public void refrescar() {
        Map<ProductoTuristico, Integer> viajeros = agencia.calcularViajerosPorProducto();
        tblViajeros.setItems(FXCollections.observableArrayList(viajeros.entrySet()));
        try {
            ProductoTuristico top = agencia.productoConMasViajeros();
            lblTop.setText(top.getNombre() + " con " + viajeros.get(top) + " viajeros");
        } catch (IllegalStateException e) {
            lblTop.setText(e.getMessage());
        }
        txtCorreos.setText(correo.getEnviados().isEmpty() ? "Todavia no se han enviado correos."
                : String.join("\n\n----------------------------------------\n\n", correo.getEnviados().reversed()));
    }

    @FXML
    private void calcularIngresos() {
        try {
            double total = agencia.calcularIngresosPeriodo(dpDesde.getValue(), dpHasta.getValue());
            lblIngresos.setText(Formato.dinero(total));
            Formato.mensaje(lblMensaje, "Del " + dpDesde.getValue() + " al " + dpHasta.getValue() + " (incluidas)", true);
        } catch (IllegalStateException e) {
            lblIngresos.setText("");
            Formato.mensaje(lblMensaje, e.getMessage(), false);
        }
    }
}
