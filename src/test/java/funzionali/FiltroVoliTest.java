package funzionali;

import com.skywings.model.Volo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class FiltroVoliTest {

    @Test
    void testFiltraggioVoliPerDestinazione() {
        // Simuliamo una lista dal DB
        List<Volo> voliDB = new ArrayList<>();

        Volo v1 = new Volo(); v1.setIdCittaPartenza(1L); v1.setIdCittaArrivo(2L);
        Volo v2 = new Volo(); v2.setIdCittaPartenza(1L); v2.setIdCittaArrivo(3L);

        voliDB.add(v1);
        voliDB.add(v2);

        // Simuliamo la logica di ricerca
        Long idDestinazioneCercata = 2L;
        List<Volo> risultati = voliDB.stream()
                .filter(v -> v.getIdCittaArrivo().equals(idDestinazioneCercata))
                .collect(Collectors.toList());

        assertEquals(1, risultati.size(), "Deve trovare un solo volo per la destinazione richiesta");
        assertEquals(2L, risultati.get(0).getIdCittaArrivo(), "L'id destinazione deve combaciare");
    }
}