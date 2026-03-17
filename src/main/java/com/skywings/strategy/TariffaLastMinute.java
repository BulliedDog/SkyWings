package com.skywings.strategy;

import com.skywings.model.Volo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component("tariffaLastMinute")
public class TariffaLastMinute implements TariffaStrategy {

    @Override
    public BigDecimal calcolaPrezzo(Volo volo, BigDecimal prezzoBase) {
        // Se il volo parte entro 3 giorni, applichiamo uno sconto del 30% (moltiplichiamo per 0.70)
        long giorniAllaPartenza = ChronoUnit.DAYS.between(LocalDate.now(), volo.getOrarioPartenza().toLocalDate());

        if (giorniAllaPartenza >= 0 && giorniAllaPartenza <= 3) {
            return prezzoBase.multiply(new BigDecimal("0.70"));
        }
        return prezzoBase;
    }
}