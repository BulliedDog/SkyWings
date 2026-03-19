package com.skywings.strategy;

import com.skywings.model.Volo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.DayOfWeek;

@Component("tariffaWeekend")
public class TariffaWeekend implements TariffaStrategy {

    @Override
    public BigDecimal calcolaPrezzo(Volo volo, BigDecimal prezzoBase) {
        // Se il volo è di Sabato o Domenica, il prezzo aumenta del 30%
        DayOfWeek giorno = volo.getOrarioPartenza().getDayOfWeek();
        if (giorno == DayOfWeek.SATURDAY || giorno == DayOfWeek.SUNDAY) {
            return prezzoBase.multiply(new BigDecimal("1.30"));
        }
        return prezzoBase;
    }
}