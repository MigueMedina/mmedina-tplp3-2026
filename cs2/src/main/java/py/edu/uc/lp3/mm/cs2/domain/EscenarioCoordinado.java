package py.edu.uc.lp3.mm.cs2.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import py.edu.uc.lp3.mm.cs2.client.RespuestaCompanero;

@Entity
@Table(name = "escenarios_coordinados")
public class EscenarioCoordinado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long simulacionId;

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

    public EscenarioCoordinado(Long simulacionId, RespuestaCompanero a2, RespuestaCompanero a3) {
        this.simulacionId = simulacionId;
        this.a2Status = a2.status();
        this.a3Status = a3.status();
        this.a2Cuerpo = a2.cuerpo();
        this.a3Cuerpo = a3.cuerpo();
        this.a2Operacion = a2.operacion();
        this.a3Operacion = a3.operacion();
        this.fecha = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getSimulacionId() {
        return simulacionId;
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
}
