package py.edu.uc.lp3.mm.cs2.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import py.edu.uc.lp3.mm.cs2.client.RespuestaCompanero;

@Entity
@Table(name = "escenarios_coordinados")
public class EscenarioCoordinado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, unique = true)
    private String idempotencyKey;

    @ManyToOne(optional = false)
    @JoinColumn(name = "simulacion_id", nullable = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private Simulacion simulacion;

    @Column(nullable = false)
    private int a2Status;

    @Column(nullable = false)
    private int a3Status;

    @Column(length = 2000)
    private String a2Cuerpo;

    @Column(length = 2000)
    private String a3Cuerpo;

    @Column(nullable = false)
    private String a2Operacion;

    @Column(nullable = false)
    private String a3Operacion;

    @Column(nullable = false)
    private LocalDateTime fecha;

    protected EscenarioCoordinado() {
    }

    public EscenarioCoordinado(String idempotencyKey, Simulacion simulacion,
                               RespuestaCompanero a2, RespuestaCompanero a3) {
        this.idempotencyKey = idempotencyKey;
        this.simulacion = simulacion;
        this.a2Status = a2.status();
        this.a3Status = a3.status();
        this.a2Cuerpo = recortar(a2.cuerpo());
        this.a3Cuerpo = recortar(a3.cuerpo());
        this.a2Operacion = a2.operacion();
        this.a3Operacion = a3.operacion();
        this.fecha = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public Long getSimulacionId() {
        return simulacion == null ? null : simulacion.getId();
    }

    public Simulacion getSimulacion() {
        return simulacion;
    }

    public int getA2Status() {
        return a2Status;
    }

    public int getA3Status() {
        return a3Status;
    }

    public String getA2Cuerpo() {
        return a2Cuerpo;
    }

    public String getA3Cuerpo() {
        return a3Cuerpo;
    }

    public String getA2Operacion() {
        return a2Operacion;
    }

    public String getA3Operacion() {
        return a3Operacion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    private String recortar(String texto) {
        if (texto == null) {
            return null;
        }
        String limpio = texto.replaceAll("[\\p{Cntrl}]", " ").trim();
        return limpio.length() <= 1000 ? limpio : limpio.substring(0, 1000) + "...";
    }
}
