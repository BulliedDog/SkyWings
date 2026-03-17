package com.skywings.strategy;

import com.skywings.model.Volo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("tariffaStandard")
public class TariffaStandard implements TariffaStrategy {

    @Override
    public BigDecimal calcolaPrezzo(Volo volo, BigDecimal prezzoBase) {
        return prezzoBase;
    }
}