package funzionali;

import com.skywings.SkyWingsApplication;
import com.skywings.dto.VoloDTO;
import com.skywings.model.Volo;
import com.skywings.repository.interfaces.VoloDAO;
import com.skywings.service.VoloService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = SkyWingsApplication.class)
class VoloServiceFunctionalTest {

    @Autowired
    private VoloService voloService;

    // Usiamo MockBean per isolare il database reale ed evitare che il test
    // fallisca se il DB di produzione è vuoto
    @MockitoBean
    private VoloDAO voloDAO;

    @Test
    void testFlussoBase_RicercaVoloConRisultati() {
        // ARRANGE: Simuliamo il comportamento del DAO
        List<Volo> voliTrovati = new ArrayList<>();
        Volo v = new Volo();
        v.setCodiceVolo("SW-101");
        voliTrovati.add(v);

        when(voloDAO.findVoliFiltered(any(Long.class), any(Long.class), any(LocalDate.class)))
                .thenReturn(voliTrovati);

        // ACT: L'utente (tramite il controller/service) effettua la ricerca
        List<Volo> risultato = voloService.getVoliFiltered(1L, 2L, LocalDate.now());

        // ASSERT: Il sistema restituisce la lista corretta
        assertEquals(1, risultato.size(), "Il Flusso Base deve restituire i voli corrispondenti ai filtri");
        assertEquals("SW-101", risultato.get(0).getCodiceVolo());
    }

    @Test
    void testFlussoAlternativo_RicercaVoloSenzaRisultati() {
        // ARRANGE: Simuliamo che non ci siano voli per quella tratta/data
        when(voloDAO.findVoliFiltered(any(Long.class), any(Long.class), any(LocalDate.class)))
                .thenReturn(new ArrayList<>());

        // ACT: L'utente effettua la ricerca
        List<Volo> risultato = voloService.getVoliFiltered(99L, 100L, LocalDate.now());

        // ASSERT: Il sistema gestisce elegantemente la mancanza di risultati
        assertTrue(risultato.isEmpty(), "Il Flusso Alternativo deve restituire una lista vuota senza andare in errore");
    }
}