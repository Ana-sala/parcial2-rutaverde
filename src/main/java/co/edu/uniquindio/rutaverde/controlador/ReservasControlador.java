package co.edu.uniquindio.rutaverde.controlador;

import co.edu.uniquindio.rutaverde.modelo.AgenciaRutaVerde;
import co.edu.uniquindio.rutaverde.modelo.Cliente;
import co.edu.uniquindio.rutaverde.modelo.Guia;
import co.edu.uniquindio.rutaverde.modelo.Reserva;
import co.edu.uniquindio.rutaverde.modelo.fachada.FachadaReservas;
import co.edu.uniquindio.rutaverde.modelo.producto.ProductoTuristico;
import co.edu.uniquindio.rutaverde.modelo.voucher.GeneradorVoucher;
import co.edu.uniquindio.rutaverde.modelo.voucher.GeneradorVoucherAsesor;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;

/** Canal del asesor: arma la reserva con el Builder y confirma con la fachada. */
public class ReservasControlador {

    @FXML private ComboBox<Cliente> cmbCliente;
    @FXML private ComboBox<ProductoTuristico> cmbProducto;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtViajeros;
    @FXML private ComboBox<Guia> cmbGuia;
    @FXML private CheckBox chkTransporte;
    @FXML private CheckBox chkSeguro;
    @FXML private TextArea txtObservaciones;
    @FXML private Label lblValor;
    @FXML private TextField txtTarjeta;
    @FXML private Label lblMensaje;
    @FXML private TextArea txtVoucher;
    @FXML private TableView<Reserva> tblReservas;
    @FXML private TableColumn<Reserva, String> colNumero;
    @FXML private TableColumn<Reserva, String> colCliente;
    @FXML private TableColumn<Reserva, String> colProducto;
    @FXML private TableColumn<Reserva, String> colFecha;
    @FXML private TableColumn<Reserva, String> colViajeros;
    @FXML private TableColumn<Reserva, String> colTotal;

    private AgenciaRutaVerde agencia;
    private FachadaReservas fachada;
    private GeneradorVoucher generador;

    @FXML
    private void initialize() {
        dpFecha.setValue(LocalDate.now().plusDays(7));
        colNumero.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getNumero())));
        colCliente.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCliente().getNombreCompleto()));
        colProducto.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getProducto().getNombre()));
        colFecha.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFechaViaje().toString()));
        colViajeros.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getViajeros())));
        colTotal.setCellValueFactory(c -> new SimpleStringProperty(Formato.dinero(c.getValue().calcularValor())));
    }

    public void inicializar(AgenciaRutaVerde agencia, FachadaReservas fachada) {
        this.agencia = agencia;
        this.fachada = fachada;
        // Este canal elige el voucher detallado (Factory Method)
        this.generador = new GeneradorVoucherAsesor(agencia.getNombre(), agencia.getRnt());
        refrescar();
    }

    public void refrescar() {
        cmbCliente.setItems(FXCollections.observableArrayList(agencia.getClientes()));
        cmbProducto.setItems(FXCollections.observableArrayList(agencia.getProductos()));
        cmbGuia.setItems(FXCollections.observableArrayList(agencia.getGuias()));
        tblReservas.setItems(FXCollections.observableArrayList(agencia.getReservas()));
    }

    /** Traduce el formulario al Builder; las reglas las valida build(). */
    private Reserva construirReserva() {
        String viajeros = txtViajeros.getText().trim();
        Reserva.Builder builder = new Reserva.Builder()
                .conCliente(cmbCliente.getValue())
                .conProducto(cmbProducto.getValue())
                .conFechaViaje(dpFecha.getValue())
                .conViajeros(viajeros.isEmpty() ? 0 : Formato.entero(viajeros, "Los viajeros"))
                .conGuia(cmbGuia.getValue())
                .conObservaciones(txtObservaciones.getText());
        if (chkTransporte.isSelected()) {
            builder.conTransporte();
        }
        if (chkSeguro.isSelected()) {
            builder.conSeguro();
        }
        return builder.build();
    }

    @FXML
    private void calcularValor() {
        try {
            Reserva r = construirReserva();
            lblValor.setText("Producto: " + Formato.dinero(r.calcularValorProducto())
                    + "\nTransporte: " + Formato.dinero(r.calcularValorTransporte())
                    + "\nSeguro: " + Formato.dinero(r.calcularValorSeguro())
                    + "\nGuia: " + Formato.dinero(r.calcularValorGuia())
                    + "\nTOTAL: " + Formato.dinero(r.calcularValor()));
            Formato.mensaje(lblMensaje, "Reserva valida. Ingrese la tarjeta para confirmar.", true);
        } catch (IllegalStateException e) {
            lblValor.setText("");
            Formato.mensaje(lblMensaje, e.getMessage(), false);
        }
    }

    @FXML
    private void confirmarReserva() {
        try {
            Reserva r = construirReserva();
            String voucher = fachada.confirmarReserva(r, txtTarjeta.getText(), generador);
            txtVoucher.setText(voucher);
            Formato.mensaje(lblMensaje, "Reserva No. " + r.getNumero() + " confirmada. Voucher enviado a "
                    + r.getCliente().getCorreo(), true);
            refrescar();
        } catch (IllegalStateException e) {
            Formato.mensaje(lblMensaje, e.getMessage(), false);
        }
    }

    @FXML
    private void quitarGuia() {
        cmbGuia.setValue(null);
    }
}
