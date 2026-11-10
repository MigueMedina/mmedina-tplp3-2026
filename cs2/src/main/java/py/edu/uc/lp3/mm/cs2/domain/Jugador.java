package py.edu.uc.lp3.mm.cs2.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import py.edu.uc.lp3.mm.cs2.exception.DatosInvalidosException;
import py.edu.uc.lp3.mm.cs2.exception.JugadorEliminadoException;

@Entity
@Table(name = "jugadores")
public class Jugador {

    public static final int VIDA_INICIAL = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false)
    private int vida;

    @Column(nullable = false)
    private boolean vivo;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "chaleco_id")
    private Chaleco chaleco;

    protected Jugador() {
    }

    public Jugador(String nombre) {
        this(nombre, VIDA_INICIAL, null);
    }

    public Jugador(String nombre, int vida) {
        this(nombre, vida, null);
    }

    public Jugador(String nombre, int vida, Chaleco chaleco) {
        setNombre(nombre);
        setVida(vida);
        this.chaleco = chaleco;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new DatosInvalidosException("El nombre del jugador no puede estar vacío");
        }
        this.nombre = nombre;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        if (vida < 0 || vida > VIDA_INICIAL) {
            throw new DatosInvalidosException("La vida del jugador debe estar entre 0 y " + VIDA_INICIAL);
        }
        this.vida = vida;
        this.vivo = vida > 0;
    }

    public boolean isVivo() {
        return vivo;
    }

    /**
     * Regla de negocio: un jugador recibe el daño de un arma, su vida nunca
     * baja de cero y queda marcado como muerto al llegar a cero.
     *
     * @return el daño efectivamente aplicado.
     */
    public int recibirDisparo(int dano) {
        if (!vivo) {
            throw new JugadorEliminadoException(
                    "El jugador " + nombre + " ya está muerto y no puede recibir más disparos");
        }
        if (dano <= 0) {
            throw new DatosInvalidosException("El arma debe infligir un daño mayor que cero");
        }
        int danoALaVida = chaleco == null ? dano : chaleco.absorber(dano);
        int vidaAnterior = this.vida;
        this.vida = Math.max(0, this.vida - danoALaVida);
        this.vivo = this.vida > 0;
        return vidaAnterior - this.vida;
    }

    public void equiparChaleco(Chaleco chaleco) {
        this.chaleco = chaleco;
    }

    public int getBlindaje() {
        return chaleco == null ? 0 : chaleco.getDurabilidad();
    }

    public void revivir() {
        this.vida = VIDA_INICIAL;
        this.vivo = true;
    }
}
