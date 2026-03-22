package DAL;

import com.skywings.SkyWingsApplication;
import com.skywings.model.Utente;
import com.skywings.repository.interfaces.UtenteDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SkyWingsApplication.class)
@Transactional // FONDAMENTALE: esegue il test sul DB vero e poi cancella tutto!
public class UtenteDAOTest {

    @Autowired
    private UtenteDAO utenteDAO;

    @Test
    void testSalvataggioERecuperoUtente_DatabaseReale() {
        // ARRANGE: Prepariamo un utente fittizio da inserire
        Utente nuovoUtente = new Utente();
        nuovoUtente.setNome("Mario");
        nuovoUtente.setCognome("Rossi");
        nuovoUtente.setEmail("mario.test@skywings.com");
        nuovoUtente.setUsername("mariorossi_test");
        nuovoUtente.setPassword("hash_finto_123");

        // Mettiamo il ruolo esatto che ha fatto passare il vincolo CHECK
        nuovoUtente.setRuolo("CLIENTE");

        // ACT 1: Eseguiamo l'INSERT sul database reale tramite il DAO
        utenteDAO.save(nuovoUtente);

        // ACT 2: Usiamo la SELECT per ripescare l'utente appena salvato.
        // Se l'inserimento ha funzionato, il database ce lo restituirà con l'ID valorizzato!
        Optional<Utente> utenteTrovato = utenteDAO.findByUsername("mariorossi_test");

        // ASSERT: Assicuriamoci che i dati estratti combacino
        assertTrue(utenteTrovato.isPresent(), "L'utente salvato deve essere ritrovato nel database");

        // Adesso che lo abbiamo letto dal database, l'ID ci deve essere per forza!
        assertNotNull(utenteTrovato.get().getId(), "Il database deve aver generato un ID per la riga salvata");
        assertEquals("mario.test@skywings.com", utenteTrovato.get().getEmail(), "La mail mappata deve coincidere");
    }
}