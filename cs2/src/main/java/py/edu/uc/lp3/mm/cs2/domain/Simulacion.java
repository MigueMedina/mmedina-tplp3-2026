package py.edu.uc.lp3.mm.cs2.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "simulaciones")
public class Simulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "jugador_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Jugador jugador;

    @ManyToOne(optional = false)
    @JoinColumn(name = "arma_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Arma arma;

    @Column(nullable = false)
    private int dano;

    @Column(nullable = false)
    private int vidaAntes;

    @Column(nullable = false)
    private int vidaDespues;

    @Column(nullable = false)
    private LocalDateTime fecha;

    protected Simulacion() {
    }

    public Simulacion(Jugador jugador, Arma arma, int dano, int vidaAntes, int vidaDespues) {
        this.jugador = jugador;
        this.arma = arma;
        this.dano = dano;
        this.vidaAntes = vidaAntes;
        this.vidaDespues = vidaDespues;
        this.fecha = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public Arma getArma() {
        return arma;
    }

    public int getDano() {
        return dano;
    }

    public int getVidaAntes() {
        return vidaAntes;
    }

    public int getVidaDespues() {
        return vidaDespues;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
