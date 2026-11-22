package py.edu.uc.lp3.mm.cs2.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "chalecos")
public class Chaleco {

    public static final int DURABILIDAD_MAXIMA = 100;
    public static final int PORCENTAJE_ABSORCION = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int durabilidad;

    protected Chaleco() {
        this(DURABILIDAD_MAXIMA);
    }

    public Chaleco(int durabilidad) {
        this.durabilidad = durabilidad;
    }

    public int absorber(int dano) {
        int danoSeguro = Math.max(0, dano);
        int absorbido = Math.min((danoSeguro * PORCENTAJE_ABSORCION) / 100, durabilidad);
        this.durabilidad -= absorbido;
        return danoSeguro - absorbido;
    }

    public Long getId() {
        return id;
    }

    public int getDurabilidad() {
        return durabilidad;
    }
}
