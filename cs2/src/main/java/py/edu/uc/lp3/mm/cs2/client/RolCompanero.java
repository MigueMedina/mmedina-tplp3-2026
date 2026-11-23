package py.edu.uc.lp3.mm.cs2.client;

public enum RolCompanero {
    A2("A2"),
    A3("A3");

    private final String etiqueta;

    RolCompanero(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
