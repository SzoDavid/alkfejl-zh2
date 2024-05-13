package hu.szte.inf.servlet;

import hu.szte.inf.core.data.Dao;
import hu.szte.inf.core.model.Dummy;
import hu.szte.inf.core.util.common.Instancer;

// Note: DO NOT USE THIS CLASS FOR ANY PURPOSE!
// Annotations and inheritance is required
public abstract class ExampleServlet {

    // Get my dao object
    // This example uses a hikari datasource,
    // but a simple sqlite datasource will do just as fine.
    // e.g. Instancer.defaultDaoWithSimpleDs()
    private final Dao<Long, Dummy> dao = Instancer.defaultDaoWithHikariDs();

    // These methods should be implemented only if required.
    // Method for GET
    abstract void doGet();

    // Method for POST
    abstract void doPost();

    // Method for PUT
    abstract void doPut();

    // Method for DELETE
    abstract void doDelete();
}
