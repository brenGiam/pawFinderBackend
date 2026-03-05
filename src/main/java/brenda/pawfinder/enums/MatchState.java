package brenda.pawfinder.enums;

public enum MatchState {
    PENDIENTE("Pendiente de revisión"),
    DESCARTADO("Descartado"),
    CONFIRMADO("Confirmado como coincidencia");

    private final String descripcion;

    MatchState(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
