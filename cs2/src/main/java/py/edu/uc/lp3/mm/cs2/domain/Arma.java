package py.edu.uc.lp3.mm.cs2.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name = "armas")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_arma", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipoArma")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Fusil.class, name = "fusil"),
        @JsonSubTypes.Type(value = Pistola.class, name = "pistola"),
        @JsonSubTypes.Type(value = Escopeta.class, name = "escopeta"),
        @JsonSubTypes.Type(value = AWP.class, name = "awp"),
        @JsonSubTypes.Type(value = Flash.class, name = "flash"),
        @JsonSubTypes.Type(value = Humo.class, name = "humo"),
        @JsonSubTypes.Type(value = Granada.class, name = "granada")
})
public abstract class Arma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private double precio;

    protected Arma() {
    }

    public Arma(String nombre, int id, double precio) {
        setNombre(nombre);
        setId(id);
        setPrecio(precio);
    }

    // Getters y Setters (con invariantes)
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        this.nombre = nombre;
    }

    public int getId() { return id; }
    public void setId(int id) {
        if (id < 0)
            throw new IllegalArgumentException("El id debe ser no negativo");
        this.id = id;
    }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) {
        if (precio < 0)
            throw new IllegalArgumentException("El precio debe ser no negativo");
        this.precio = precio;
    }

    // Métodos abstractos
    public abstract String disparar();
    public abstract String recargar();
    public abstract String obtenerDetalleTienda();
}
