module hu.alkfejl.core {
    requires transitive java.sql;
    requires java.net.http;
    requires com.google.gson;

    exports hu.alkfejl.model;
    exports hu.alkfejl.controller;
    exports hu.alkfejl.utils;

    opens hu.alkfejl.model;
}
