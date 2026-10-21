module co.edu.uniquindio.rutaverde {
    requires javafx.controls;
    requires javafx.fxml;

    opens co.edu.uniquindio.rutaverde.controlador to javafx.fxml;
    exports co.edu.uniquindio.rutaverde.app;
}
