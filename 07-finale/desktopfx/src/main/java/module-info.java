module hu.inf.szte.adventure.destopfx {
    requires hu.inf.szte.adventure.core;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires org.apache.httpcomponents.httpcore;
    requires com.google.gson;
    requires static lombok;

    opens hu.inf.szte.adventure to javafx.fxml;
    opens hu.inf.szte.adventure.controller to javafx.fxml;

    exports hu.inf.szte.adventure;
}
