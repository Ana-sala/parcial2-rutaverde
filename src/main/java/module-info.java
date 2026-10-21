module co.edu.uniquindio.rutaverde {
    requires javafx.controls;
    requires javafx.fxml;


    opens co.edu.uniquindio.rutaverde to javafx.fxml;
    exports co.edu.uniquindio.rutaverde;
}