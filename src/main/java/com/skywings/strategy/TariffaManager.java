package com.skywings.strategy;

import com.skywings.model.Volo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Component
public class TariffaManager {

    private final TariffaStrategy tariffaStandard;
    private final TariffaStrategy tariffaLastMinute;
    private final TariffaStrategy tariffaWeekend;

    @Autowired
    public TariffaManager(
            @Qualifier("tariffaStandard") TariffaStrategy tariffaStandard,
            @Qualifier("tariffaLastMinute") TariffaStrategy tariffaLastMinute,
            @Qualifier("tariffaWeekend") TariffaStrategy tariffaWeekend) {

        this.tariffaStandard = tariffaStandard;
        this.tariffaLastMinute = tariffaLastMinute;
        this.tariffaWeekend = tariffaWeekend;
    }

    public TariffaContext getContextConfigurato(Volo volo) {
        // 1. Partiamo dalla base
        TariffaContext context = new TariffaContext(tariffaStandard);

        // 2. Logica di selezione
        if (isLastMinute(volo)) {
            // Se mancano meno di 3 giorni, applichiamo lo sconto 20%
            context.setStrategy(tariffaLastMinute);
        }
        else if (isWeekend(volo)) {
            // Se è Sabato o Domenica, applichiamo il rincaro 30%
            context.setStrategy(tariffaWeekend);
        }

        return context;
    }

    private boolean isLastMinute(Volo volo) {
        if (volo.getOrarioPartenza() == null) return false;
        // 3 giorni prima della partenza
        return volo.getOrarioPartenza().isBefore(LocalDateTime.now().plusDays(3))
                && volo.getOrarioPartenza().isAfter(LocalDateTime.now());
    }

    private boolean isWeekend(Volo volo) {
        if (volo.getOrarioPartenza() == null) return false;
        DayOfWeek giorno = volo.getOrarioPartenza().getDayOfWeek();
        return giorno == DayOfWeek.SATURDAY || giorno == DayOfWeek.SUNDAY;
    }
}