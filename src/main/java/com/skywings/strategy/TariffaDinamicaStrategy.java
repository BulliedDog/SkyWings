package com.skywings.strategy;

import com.skywings.model.Volo;
import com.skywings.strategy.TariffaStrategy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component("tariffaDinamicaStrategy")
@Primary // Questo dice a Spring: "Se non specifico nulla, usa questa"
public class TariffaDinamicaStrategy implements TariffaStrategy {

    @Override
    public BigDecimal calcolaPrezzo(Volo volo, BigDecimal prezzoBase) {
        DayOfWeek giorno = volo.getOrarioPartenza().getDayOfWeek();
        long giorniAllaPartenza = ChronoUnit.DAYS.between(LocalDate.now(), volo.getOrarioPartenza().toLocalDate());

        // Logica dinamica:
        // 1. Se è weekend, applichiamo il rincaro
        if (giorno == DayOfWeek.SATURDAY || giorno == DayOfWeek.SUNDAY) {
            return prezzoBase.multiply(new BigDecimal("1.20"));
        }

        // 2. Se è un Last Minute (meno di 3 giorni alla partenza)
        if (giorniAllaPartenza >= 0 && giorniAllaPartenza <= 3) {
            return prezzoBase.multiply(new BigDecimal("0.70"));
        }

        // 3. Altrimenti tariffa standard
        return prezzoBase;
    }
}