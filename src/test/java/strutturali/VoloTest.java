package strutturali;

import com.skywings.model.Volo;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VoloTest{

    @Test
    void testCalcoloDurataVolo() {
        Volo v = new Volo();
        v.setOrarioPartenza(LocalDateTime.of(2026, 3, 5, 10, 0));
        v.setOrarioArrivo(LocalDateTime.of(2026, 3, 5, 12, 30));

        assertEquals("2h 30m", v.getDurataFormattata());
    }

    @Test
    void testDurataVoloNegativa() {
        Volo v = new Volo();
        // Partenza alle 10:00, Arrivo alle 08:00 (Impossibile)
        v.setOrarioPartenza(LocalDateTime.of(2026, 3, 5, 10, 0));
        v.setOrarioArrivo(LocalDateTime.of(2026, 3, 5, 8, 0));

        // Se il tuo metodo getDurataFormattata() non gestisce l'errore,
        // questo test serve a decidere come deve comportarsi il sistema.
        // Esempio: deve restituire "N/A" o un valore di errore.
        assertEquals("N/A", v.getDurataFormattata(),
                "La durata deve essere N/A se l'arrivo precede la partenza");
    }

    @Test
    void testVoloStessaOra() {
        Volo v = new Volo();
        LocalDateTime adesso = LocalDateTime.now();
        v.setOrarioPartenza(adesso);
        v.setOrarioArrivo(adesso);

        assertEquals("0h 0m", v.getDurataFormattata(),
                "Un volo con partenza e arrivo identici deve avere durata zero");
    }
}