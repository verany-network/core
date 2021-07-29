package net.verany.api.module;

public interface VeranyProject {

    VeranyModule getModule();

    void setModule(VeranyModule module);

    VeranyModule.DatabaseConnection getConnection();

    void setConnection(VeranyModule.DatabaseConnection connection);

}
