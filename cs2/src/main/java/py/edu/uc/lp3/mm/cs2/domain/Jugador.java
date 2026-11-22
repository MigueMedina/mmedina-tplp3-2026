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

@Entity
@Table(name = "jugadores")
public class Jugador implements UICapable {

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
        this.nombre = nombre;
        asignarVida(vida);
        this.chaleco = chaleco;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getVida() {
        return vida;
    }

    private void asignarVida(int vida) {
        this.vida = vida;
        this.vivo = vida > 0;
    }

    public boolean isVivo() {
        return vivo;
    }

    public int recibirDisparo(int dano) {
        if (!vivo) {
            return 0;
        }
        int danoAplicado = Math.max(0, dano);
        int danoALaVida = chaleco == null ? danoAplicado : chaleco.absorber(danoAplicado);
        int vidaAnterior = this.vida;
        asignarVida(Math.max(0, this.vida - danoALaVida));
        return vidaAnterior - this.vida;
    }

    public void equiparChaleco(Chaleco chaleco) {
        this.chaleco = chaleco;
    }

    public int getBlindaje() {
        return chaleco == null ? 0 : chaleco.getDurabilidad();
    }

    public void revivir() {
        asignarVida(VIDA_INICIAL);
    }

    @Override
    public String getImagen() {
        return "jugador_" + nombre.toLowerCase().replace(" ", "_") + ".png";
    }

    @Override
    public String getSonido() {
        return "jugador_impacto.ogg";
    }

    @Override
    public double getVelocidadDisparo() {
        return 0;
    }

    @Override
    public double getRango() {
        return 0;
    }

    @Override
    public String mostrarEnUI() {
        return "Jugador " + nombre + " con vida " + vida + "/" + VIDA_INICIAL
                + (vivo ? " (vivo)" : " (eliminado)");
    }
}
