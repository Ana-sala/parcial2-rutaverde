package co.edu.uniquindio.rutaverde.controlador;

import co.edu.uniquindio.rutaverde.modelo.AgenciaRutaVerde;
import co.edu.uniquindio.rutaverde.modelo.Cliente;
import co.edu.uniquindio.rutaverde.modelo.Guia;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.List;

public class ClientesControlador {

    @FXML private TextField txtDocumento;
    @FXML private TextField txtNombre;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtPais;
    @FXML private TextField txtIdioma;
    @FXML private TextField txtIdGuia;
    @FXML private TextField txtNombreGuia;
    @FXML private TextField txtIdiomasGuia;
    @FXML private TextField txtTelefonoGuia;
    @FXML private TextField txtTarifaGuia;
    @FXML private Label lblMensaje;
    @FXML private TableView<Cliente> tblClientes;
    @FXML private TableColumn<Cliente, String> colDocumento;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colCorreo;
    @FXML private TableColumn<Cliente, String> colPais;
    @FXML private TableColumn<Cliente, String> colIdioma;
    @FXML private TableView<Guia> tblGuias;
    @FXML private TableColumn<Guia, String> colIdGuia;
    @FXML private TableColumn<Guia, String> colNombreGuia;
    @FXML private TableColumn<Guia, String> colIdiomasGuia;
    @FXML private TableColumn<Guia, String> colTelefonoGuia;
    @FXML private TableColumn<Guia, String> colTarifaGuia;

    private AgenciaRutaVerde agencia;

    @FXML
    private void initialize() {
        colDocumento.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDocumento()));
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombreCompleto()));
        colTelefono.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTelefono()));
        colCorreo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCorreo()));
        colPais.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPaisOrigen()));
        colIdioma.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getIdiomaPreferido()));
        colIdGuia.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getIdentificacion()));
        colNombreGuia.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colIdiomasGuia.setCellValueFactory(c -> new SimpleStringProperty(String.join(", ", c.getValue().getIdiomas())));
        colTelefonoGuia.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTelefono()));
        colTarifaGuia.setCellValueFactory(c -> new SimpleStringProperty(Formato.dinero(c.getValue().getTarifaPorDia())));
    }

    public void inicializar(AgenciaRutaVerde agencia) {
        this.agencia = agencia;
        refrescar();
    }

    public void refrescar() {
        tblClientes.setItems(FXCollections.observableArrayList(agencia.getClientes()));
        tblGuias.setItems(FXCollections.observableArrayList(agencia.getGuias()));
    }

    @FXML
    private void registrarCliente() {
        try {
            Cliente c = new Cliente(txtDocumento.getText().trim(), txtNombre.getText().trim(),
                    txtTelefono.getText().trim(), txtCorreo.getText().trim(), txtPais.getText().trim(),
                    txtIdioma.getText().trim());
            agencia.registrarCliente(c);
            Formato.mensaje(lblMensaje, "Cliente registrado: " + c.getNombreCompleto(), true);
            txtDocumento.clear(); txtNombre.clear(); txtTelefono.clear();
            txtCorreo.clear(); txtPais.clear(); txtIdioma.clear();
            refrescar();
        } catch (IllegalStateException e) {
            Formato.mensaje(lblMensaje, e.getMessage(), false);
        }
    }

    @FXML
    private void registrarGuia() {
        try {
            List<String> idiomas = new ArrayList<>();
            for (String i : txtIdiomasGuia.getText().split(",")) {
                if (!i.isBlank()) {
                    idiomas.add(i.trim());
                }
            }
            Guia g = new Guia(txtIdGuia.getText().trim(), txtNombreGuia.getText().trim(), idiomas,
                    txtTelefonoGuia.getText().trim(), Formato.decimal(txtTarifaGuia.getText(), "La tarifa"));
            agencia.registrarGuia(g);
            Formato.mensaje(lblMensaje, "Guia registrado: " + g.getNombre(), true);
            txtIdGuia.clear(); txtNombreGuia.clear(); txtIdiomasGuia.clear();
            txtTelefonoGuia.clear(); txtTarifaGuia.clear();
            refrescar();
        } catch (IllegalStateException e) {
            Formato.mensaje(lblMensaje, e.getMessage(), false);
        }
    }
}
