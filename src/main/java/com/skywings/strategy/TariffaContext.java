package com.skywings.strategy;

import com.skywings.model.Volo;
import java.math.BigDecimal;

public class TariffaContext {
    private TariffaStrategy strategy;

    public TariffaContext(TariffaStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(TariffaStrategy strategy) {
        this.strategy = strategy;
    }

    public BigDecimal eseguiCalcolo(Volo volo, BigDecimal prezzoBase) {
        if (this.strategy == null) {
            throw new IllegalStateException("Strategia non impostata!");
        }
        return strategy.calcolaPrezzo(volo, prezzoBase);
    }
}