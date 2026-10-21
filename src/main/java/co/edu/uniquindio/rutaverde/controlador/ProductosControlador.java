package co.edu.uniquindio.rutaverde.controlador;

import co.edu.uniquindio.rutaverde.modelo.AgenciaRutaVerde;
import co.edu.uniquindio.rutaverde.modelo.producto.Actividad;
import co.edu.uniquindio.rutaverde.modelo.producto.Dificultad;
import co.edu.uniquindio.rutaverde.modelo.producto.EstadoActividad;
import co.edu.uniquindio.rutaverde.modelo.producto.Paquete;
import co.edu.uniquindio.rutaverde.modelo.producto.ProductoTuristico;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import java.util.ArrayList;
import java.util.List;

public class ProductosControlador {

    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtMunicipio;
    @FXML private TextField txtHoras;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtCupo;
    @FXML private ComboBox<Dificultad> cmbDificultad;
    @FXML private ComboBox<EstadoActividad> cmbEstado;
    @FXML private TextField txtCodigoPaquete;
    @FXML private TextField txtNombrePaquete;
    @FXML private TextField txtDescuento;
    @FXML private ComboBox<Paquete> cmbPaquete;
    @FXML private ComboBox<ProductoTuristico> cmbProducto;
    @FXML private Label lblMensaje;
    @FXML private TreeTableView<ProductoTuristico> tblProductos;
    @FXML private TreeTableColumn<ProductoTuristico, String> colNombre;
    @FXML private TreeTableColumn<ProductoTuristico, String> colTipo;
    @FXML private TreeTableColumn<ProductoTuristico, String> colPrecio;
    @FXML private TreeTableColumn<ProductoTuristico, String> colDuracion;
    @FXML private TreeTableColumn<ProductoTuristico, String> colActividades;
    @FXML private TreeTableColumn<ProductoTuristico, String> colDetalle;

    private AgenciaRutaVerde agencia;

    @FXML
    private void initialize() {
        cmbDificultad.setItems(FXCollections.observableArrayList(Dificultad.values()));
        cmbDificultad.setValue(Dificultad.BAJA);
        cmbEstado.setItems(FXCollections.observableArrayList(EstadoActividad.values()));
        cmbEstado.setValue(EstadoActividad.DISPONIBLE);

        // Todas las columnas usan la interfaz ProductoTuristico: no importa si es hoja o compuesto
        colNombre.setCellValueFactory(c -> texto(c.getValue().getValue().getNombre()));
        colTipo.setCellValueFactory(c -> texto(c.getValue().getValue() instanceof Paquete ? "Paquete" : "Actividad"));
        colPrecio.setCellValueFactory(c -> texto(Formato.dinero(c.getValue().getValue().calcularPrecioPorPersona())));
        colDuracion.setCellValueFactory(c -> texto(String.format("%.0f h", c.getValue().getValue().calcularDuracionHoras())));
        colActividades.setCellValueFactory(c -> texto(String.valueOf(c.getValue().getValue().obtenerActividades().size())));
        colDetalle.setCellValueFactory(c -> texto(detalle(c.getValue().getValue())));
    }

    public void inicializar(AgenciaRutaVerde agencia) {
        this.agencia = agencia;
        refrescar();
    }

    public void refrescar() {
        TreeItem<ProductoTuristico> raiz = new TreeItem<>();
        for (ProductoTuristico p : agencia.getProductos()) {
            if (!estaDentroDeOtroPaquete(p)) {
                raiz.getChildren().add(crearNodo(p));
            }
        }
        tblProductos.setRoot(raiz);

        List<Paquete> paquetes = new ArrayList<>();
        for (ProductoTuristico p : agencia.getProductos()) {
            if (p instanceof Paquete paquete) {
                paquetes.add(paquete);
            }
        }
        cmbPaquete.setItems(FXCollections.observableArrayList(paquetes));
        cmbProducto.setItems(FXCollections.observableArrayList(agencia.getProductos()));
    }

    @FXML
    private void registrarActividad() {
        try {
            Actividad a = new Actividad(txtCodigo.getText().trim(), txtNombre.getText().trim(),
                    txtMunicipio.getText().trim(), Formato.decimal(txtHoras.getText(), "Las horas"),
                    Formato.decimal(txtPrecio.getText(), "El precio"), Formato.entero(txtCupo.getText(), "El cupo"),
                    cmbDificultad.getValue(), cmbEstado.getValue());
            agencia.registrarProducto(a);
            Formato.mensaje(lblMensaje, "Actividad registrada: " + a.getNombre(), true);
            txtCodigo.clear(); txtNombre.clear(); txtMunicipio.clear();
            txtHoras.clear(); txtPrecio.clear(); txtCupo.clear();
            refrescar();
        } catch (IllegalStateException | IllegalArgumentException e) {
            Formato.mensaje(lblMensaje, e.getMessage(), false);
        }
    }

    @FXML
    private void registrarPaquete() {
        try {
            String descuento = txtDescuento.getText().trim();
            Paquete p = new Paquete(txtCodigoPaquete.getText().trim(), txtNombrePaquete.getText().trim(),
                    descuento.isEmpty() ? 0 : Formato.decimal(descuento, "El descuento"));
            agencia.registrarProducto(p);
            Formato.mensaje(lblMensaje, "Paquete registrado: " + p.getNombre() + ". Ahora agreguele productos.", true);
            txtCodigoPaquete.clear(); txtNombrePaquete.clear(); txtDescuento.clear();
            refrescar();
            cmbPaquete.setValue(p);
        } catch (IllegalStateException | IllegalArgumentException e) {
            Formato.mensaje(lblMensaje, e.getMessage(), false);
        }
    }

    @FXML
    private void agregarAlPaquete() {
        Paquete paquete = cmbPaquete.getValue();
        ProductoTuristico producto = cmbProducto.getValue();
        if (paquete == null || producto == null) {
            Formato.mensaje(lblMensaje, "Seleccione el paquete y el producto", false);
            return;
        }
        try {
            paquete.agregar(producto);   // el modelo valida ciclos y actividades repetidas
            Formato.mensaje(lblMensaje, producto.getNombre() + " agregado a " + paquete.getNombre(), true);
            refrescar();
        } catch (IllegalArgumentException e) {
            Formato.mensaje(lblMensaje, e.getMessage(), false);
        }
    }

    @FXML
    private void cambiarEstado() {
        TreeItem<ProductoTuristico> item = tblProductos.getSelectionModel().getSelectedItem();
        if (item == null || !(item.getValue() instanceof Actividad actividad)) {
            Formato.mensaje(lblMensaje, "Seleccione una actividad en el catalogo", false);
            return;
        }
        actividad.setEstado(actividad.estaDisponible() ? EstadoActividad.SUSPENDIDA : EstadoActividad.DISPONIBLE);
        Formato.mensaje(lblMensaje, actividad.getNombre() + " ahora esta " + actividad.getEstado(), true);
        refrescar();
    }

    private TreeItem<ProductoTuristico> crearNodo(ProductoTuristico p) {
        TreeItem<ProductoTuristico> nodo = new TreeItem<>(p);
        if (p instanceof Paquete paquete) {
            for (ProductoTuristico hijo : paquete.getProductos()) {
                nodo.getChildren().add(crearNodo(hijo));
            }
            nodo.setExpanded(true);
        }
        return nodo;
    }

    private boolean estaDentroDeOtroPaquete(ProductoTuristico p) {
        for (ProductoTuristico otro : agencia.getProductos()) {
            if (otro != p && otro instanceof Paquete && otro.contiene(p)) {
                return true;
            }
        }
        return false;
    }

    private static String detalle(ProductoTuristico p) {
        if (p instanceof Actividad a) {
            return a.getEstado() + " · " + a.getDificultad();
        }
        return String.format("Descuento %.0f%%", ((Paquete) p).getDescuento());
    }

    private static SimpleStringProperty texto(String s) {
        return new SimpleStringProperty(s);
    }
}
