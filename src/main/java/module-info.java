module com.example.sistemedistribuite {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sistemedistribuite to javafx.fxml;
    exports com.example.sistemedistribuite;
}