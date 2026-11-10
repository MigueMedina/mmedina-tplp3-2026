package py.edu.uc.lp3.mm.cs2.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import py.edu.uc.lp3.mm.cs2.exception.DatosInvalidosException;

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
        if (durabilidad < 0 || durabilidad > DURABILIDAD_MAXIMA) {
            throw new DatosInvalidosException(
                    "La durabilidad del chaleco debe estar entre 0 y " + DURABILIDAD_MAXIMA);
        }
        this.durabilidad = durabilidad;
    }

    public int absorber(int dano) {
        if (dano < 0) {
            throw new DatosInvalidosException("El daño no puede ser negativo");
        }
        int absorbido = Math.min((dano * PORCENTAJE_ABSORCION) / 100, durabilidad);
        this.durabilidad -= absorbido;
        return dano - absorbido;
    }

    public Long getId() {
        return id;
    }

    public int getDurabilidad() {
        return durabilidad;
    }
}
