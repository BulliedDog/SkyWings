package strutturali; // O com.skywings.strutturali se hai sistemato i package

import com.skywings.model.Volo;
import com.skywings.strategy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TariffaManagerTest {

    private TariffaManager tariffaManager;

    @BeforeEach
    void setUp() {
        // Inizializziamo il manager con le strategie concrete reali
        tariffaManager = new TariffaManager(
                new TariffaStandard(),
                new TariffaLastMinute(),
                new TariffaWeekend()
        );
    }

    @Test
    void testTariffaLastMinute() {
        Volo volo = new Volo();
        volo.setPrezzoBase(new BigDecimal("100.00"));
        // Simuliamo una partenza tra 1 giorno (< 3 giorni = Last Minute)
        volo.setOrarioPartenza(LocalDateTime.now().plusDays(1));

        TariffaContext context = tariffaManager.getContextConfigurato(volo);
        BigDecimal prezzoFinale = context.eseguiCalcolo(volo, volo.getPrezzoBase());

        // Confrontiamo direttamente i valori arrotondati a due decimali
        assertEquals(new BigDecimal("80.00"), prezzoFinale.setScale(2, RoundingMode.HALF_UP),
                "La tariffa Last Minute deve applicare uno sconto del 20%");
    }

    @Test
    void testTariffaWeekend() {
        Volo volo = new Volo();
        volo.setPrezzoBase(new BigDecimal("100.00"));

        // Cerchiamo un sabato che sia distante più di 3 giorni
        LocalDateTime dataPartenza = LocalDateTime.now().plusDays(5);
        while (dataPartenza.getDayOfWeek() != DayOfWeek.SATURDAY) {
            dataPartenza = dataPartenza.plusDays(1);
        }
        volo.setOrarioPartenza(dataPartenza);

        TariffaContext context = tariffaManager.getContextConfigurato(volo);
        BigDecimal prezzoFinale = context.eseguiCalcolo(volo, volo.getPrezzoBase());

        // Confrontiamo direttamente i valori arrotondati a due decimali
        assertEquals(new BigDecimal("130.00"), prezzoFinale.setScale(2, RoundingMode.HALF_UP),
                "La tariffa Weekend deve applicare un rincaro del 30%");
    }

    @Test
    void testTariffaStandard() {
        Volo volo = new Volo();
        volo.setPrezzoBase(new BigDecimal("100.00"));

        // Cerchiamo un mercoledì lontano
        LocalDateTime dataPartenza = LocalDateTime.now().plusDays(10);
        while (dataPartenza.getDayOfWeek() == DayOfWeek.SATURDAY || dataPartenza.getDayOfWeek() == DayOfWeek.SUNDAY) {
            dataPartenza = dataPartenza.plusDays(1);
        }
        volo.setOrarioPartenza(dataPartenza);

        TariffaContext context = tariffaManager.getContextConfigurato(volo);
        BigDecimal prezzoFinale = context.eseguiCalcolo(volo, volo.getPrezzoBase());

        assertEquals(new BigDecimal("100.00"), prezzoFinale.setScale(2, RoundingMode.HALF_UP),
                "La tariffa Standard deve restituire il prezzo base");
    }
}