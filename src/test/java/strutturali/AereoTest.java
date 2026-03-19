package strutturali;

import com.skywings.model.Aereo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AereoTest {

    @Test
    void testCapacitaAereoValida() {
        Aereo aereo = new Aereo();

        // Verifichiamo che venga lanciata un'eccezione (IllegalArgumentException)
        assertThrows(IllegalArgumentException.class, () -> {
            aereo.setCapacitaEconomy(-50);
        }, "Il sistema deve rifiutare capacità negative lanciando un'eccezione");
    }

    @Test
    void testCapacitaTotale() {
        Aereo aereo = new Aereo();
        aereo.setCapacitaEconomy(150);
        aereo.setCapacitaBusiness(20);

        // Se hai un metodo getCapacitaTotale(), testalo così:
        // assertEquals(170, aereo.getCapacitaTotale(), "La capacità totale deve essere la somma di economy e business");
    }
}