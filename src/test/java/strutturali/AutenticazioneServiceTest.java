package strutturali;

import com.skywings.model.Utente;
import com.skywings.repository.interfaces.UtenteDAO;
import com.skywings.service.AutenticazioneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AutenticazioneServiceTest {

    @Mock // Simuliamo il database (DAO)
    private UtenteDAO utenteDAO;

    @InjectMocks // Inseriamo il finto database nel servizio da testare
    private AutenticazioneService autenticazioneService;

    @BeforeEach
    void setUp() {
        // Inizializza i mock di Mockito
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginUtenteNonTrovato() {
        // Diciamo al finto DB di non trovare l'utente
        when(utenteDAO.findByEmail("inesistente@email.com")).thenReturn(Optional.empty());

        // Verifichiamo che il login fallisca (o restituisca null in base alla tua implementazione)
        Utente risultato = autenticazioneService.login("inesistente@email.com", "password123");
        assertNull(risultato, "Il login di un utente non esistente deve fallire");
    }

    @Test
    void testLoginPasswordErrata() {
        Utente utenteVero = new Utente();
        utenteVero.setEmail("mario@email.com");
        utenteVero.setPassword("passwordCorretta"); // Assumendo testo in chiaro per ora o Hash

        // Diciamo al finto DB di restituire l'utente
        when(utenteDAO.findByEmail("mario@email.com")).thenReturn(Optional.of(utenteVero));

        // Proviamo ad accedere con password sbagliata
        Utente risultato = autenticazioneService.login("mario@email.com", "passwordSbagliata");
        assertNull(risultato, "Il login con password errata non deve restituire l'utente");
    }
}