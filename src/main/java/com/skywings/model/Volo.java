package com.skywings.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.Duration;
import java.time.LocalDateTime;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Volo {
    private Long id;
    private String codiceVolo;
    private Long idCittaPartenza;
    private Long idCittaArrivo;
    private Long idAereo;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime orarioPartenza;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime orarioArrivo;

    private BigDecimal prezzoBase;
    private StatoVolo stato;

    public enum StatoVolo {
        PROGRAMMATO, IN_VOLO, ATTERRATO, CANCELLATO, IN_RITARDO
    }

    public Volo() {}

    public Volo(Long id, String codiceVolo, Long idCittaPartenza, Long idCittaArrivo, Long idAereo,
                LocalDateTime orarioPartenza, LocalDateTime orarioArrivo, BigDecimal prezzoBase, StatoVolo stato) {
        this.id = id;
        this.codiceVolo = codiceVolo;
        this.idCittaPartenza = idCittaPartenza;
        this.idCittaArrivo = idCittaArrivo;
        this.idAereo = idAereo;
        this.orarioPartenza = orarioPartenza;
        this.orarioArrivo = orarioArrivo;
        this.prezzoBase = prezzoBase;
        this.stato = stato;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodiceVolo() { return codiceVolo; }
    public void setCodiceVolo(String codiceVolo) { this.codiceVolo = codiceVolo; }

    public Long getIdCittaPartenza() { return idCittaPartenza; }
    public void setIdCittaPartenza(Long idCittaPartenza) { this.idCittaPartenza = idCittaPartenza; }

    public Long getIdCittaArrivo() { return idCittaArrivo; }
    public void setIdCittaArrivo(Long idCittaArrivo) { this.idCittaArrivo = idCittaArrivo; }

    public Long getIdAereo() { return idAereo; }
    public void setIdAereo(Long idAereo) { this.idAereo = idAereo; }

    public LocalDateTime getOrarioPartenza() { return orarioPartenza; }
    public void setOrarioPartenza(LocalDateTime orarioPartenza) { this.orarioPartenza = orarioPartenza; }

    public LocalDateTime getOrarioArrivo() { return orarioArrivo; }
    public void setOrarioArrivo(LocalDateTime orarioArrivo) { this.orarioArrivo = orarioArrivo; }

    public BigDecimal getPrezzoBase() { return prezzoBase; }
    public void setPrezzoBase(BigDecimal prezzoBase) { this.prezzoBase = prezzoBase; }

    public StatoVolo getStato() { return stato; }
    public void setStato(StatoVolo stato) { this.stato = stato; }

    public String getDurataFormattata() {
        if (orarioPartenza == null || orarioArrivo == null) return "N/A";

        if (orarioArrivo.isBefore(orarioPartenza)) {
            return "N/A";
        }

        Duration duration = Duration.between(orarioPartenza, orarioArrivo);
        long ore = duration.toHours();
        long minuti = duration.toMinutesPart();

        return ore + "h " + minuti + "m";
    }
}
