package strutturali;

import com.skywings.model.Volo;
import com.skywings.observer.Observer;
import com.skywings.service.VoloService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ObserverPatternTest {

    // Creiamo un Observer di test (Mock)
    class TestObserver implements Observer {
        boolean eStatoNotificato = false;

        @Override
        public void update(Volo volo) {
            this.eStatoNotificato = true;
        }
    }

    @Test
    void testAggiuntaENotificaObserver() {
        // Arrange
        VoloService voloService = new VoloService();
        TestObserver observerSpy = new TestObserver();

        voloService.addObserver(observerSpy);

        Volo voloFittizio = new Volo();
        voloFittizio.setCodiceVolo("SW123");
        voloFittizio.setStato(Volo.StatoVolo.IN_VOLO);

        // Act
        voloService.notifyObservers(voloFittizio);

        // Assert
        assertTrue(observerSpy.eStatoNotificato, "L'Observer registrato non ha ricevuto la notifica di update");
    }
}