module com.example.javaassignment {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    // requires org.xerial.sqlitejdbc;

    opens com.example.javaassignment to javafx.fxml;
    exports com.example.javaassignment;
    exports com.example.javaassignment.DataAccess;
    opens com.example.javaassignment.DataAccess to javafx.fxml;
    exports com.example.javaassignment.Networking;
    opens com.example.javaassignment.Networking to javafx.fxml;
    exports com.example.javaassignment.DataModels;
    opens com.example.javaassignment.DataModels to javafx.fxml;
}