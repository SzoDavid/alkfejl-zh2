package hu.szte.inf.helper;

import hu.szte.inf.core.util.db.SqlDbSupport;
import org.junit.jupiter.api.extension.*;

public class ReInitDbExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        SqlDbSupport.dropDb();
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        SqlDbSupport.initDb();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        afterEach(extensionContext);
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        beforeEach(extensionContext);
    }
}
