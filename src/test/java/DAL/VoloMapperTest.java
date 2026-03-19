package DAL;

import com.skywings.dto.VoloDTO;
import com.skywings.mapper.VoloMapper;
import com.skywings.model.Volo;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class VoloMapperTest {

    private final VoloMapper mapper = new VoloMapper();

    @Test
    void testMappingCompleto() {
        Volo v = new Volo();
        v.setId(1L);
        v.setCodiceVolo("SK-123");
        v.setPrezzoBase(new BigDecimal("150.00"));
        v.setStato(Volo.StatoVolo.PROGRAMMATO);

        VoloDTO dto = mapper.toDto(v);

        assertNotNull(dto);
        assertEquals(v.getId(), dto.getId());
        assertEquals(v.getCodiceVolo(), dto.getCodiceVolo());
        assertEquals(0, v.getPrezzoBase().compareTo(dto.getPrezzoBase()));
        assertEquals(v.getStato(), dto.getStato());
    }

    @Test
    void testMappingOggettoNullo() {
        // Test di robustezza: cosa succede se il mapper riceve null?
        VoloDTO dto = mapper.toDto(null);
        assertNull(dto, "Il mapper deve restituire null se l'input è null invece di lanciare eccezioni");
    }
}