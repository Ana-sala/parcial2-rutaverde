package co.edu.uniquindio.rutaverde.controlador;

import co.edu.uniquindio.rutaverde.modelo.AgenciaRutaVerde;
import co.edu.uniquindio.rutaverde.modelo.Cliente;
import co.edu.uniquindio.rutaverde.modelo.Reserva;
import co.edu.uniquindio.rutaverde.modelo.fachada.FachadaReservas;
import co.edu.uniquindio.rutaverde.modelo.producto.ProductoTuristico;
import co.edu.uniquindio.rutaverde.modelo.voucher.GeneradorVoucher;
import co.edu.uniquindio.rutaverde.modelo.voucher.GeneradorVoucherKiosco;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.time.LocalDate;

/** Canal del kiosco: misma fachada, distinto generador de voucher. */
public class KioscoControlador {

    @FXML private ComboBox<Cliente> cmbCliente;
    @FXML private ComboBox<ProductoTuristico> cmbProducto;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtViajeros;
    @FXML private TextField txtTarjeta;
    @FXML private Label lblMensaje;
    @FXML private Label lblTiquete;

    private AgenciaRutaVerde agencia;
    private FachadaReservas fachada;
    private GeneradorVoucher generador;

    @FXML
    private void initialize() {
        dpFecha.setValue(LocalDate.now().plusDays(1));
    }

    public void inicializar(AgenciaRutaVerde agencia, FachadaReservas fachada) {
        this.agencia = agencia;
        this.fachada = fachada;
        this.generador = new GeneradorVoucherKiosco(agencia.getNombre(), agencia.getRnt());
        refrescar();
    }

    public void refrescar() {
        cmbCliente.setItems(FXCollections.observableArrayList(agencia.getClientes()));
        cmbProducto.setItems(FXCollections.observableArrayList(agencia.getProductos()));
    }

    @FXML
    private void pagar() {
        try {
            String viajeros = txtViajeros.getText().trim();
            Reserva r = new Reserva.Builder()
                    .conCliente(cmbCliente.getValue())
                    .conProducto(cmbProducto.getValue())
                    .conFechaViaje(dpFecha.getValue())
                    .conViajeros(viajeros.isEmpty() ? 0 : Formato.entero(viajeros, "Los viajeros"))
                    .build();
            String tiquete = fachada.confirmarReserva(r, txtTarjeta.getText(), generador);
            lblTiquete.setText(tiquete);
            lblTiquete.setVisible(true);
            lblTiquete.setManaged(true);
            Formato.mensaje(lblMensaje, "Pago aprobado. Presente este codigo en la entrada.", true);
        } catch (IllegalStateException e) {
            lblTiquete.setVisible(false);
            lblTiquete.setManaged(false);
            Formato.mensaje(lblMensaje, e.getMessage(), false);
        }
    }
}
