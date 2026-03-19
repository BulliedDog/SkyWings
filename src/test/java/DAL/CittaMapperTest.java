package DAL;

import com.skywings.mapper.CittaMapper;
import com.skywings.model.Citta;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CittaMapperTest {

    @Test
    void testMapRow() throws SQLException {
        // 1. Simuliamo la risposta del Database (ResultSet)
        ResultSet mockResultSet = Mockito.mock(ResultSet.class);
        Mockito.when(mockResultSet.getLong("id")).thenReturn(1L);
        Mockito.when(mockResultSet.getString("nome")).thenReturn("Roma");
        Mockito.when(mockResultSet.getString("nazione")).thenReturn("Italia");
        Mockito.when(mockResultSet.getString("codice_iata")).thenReturn("FCO");

        // 2. Chiamiamo il nostro Mapper
        CittaMapper mapper = new CittaMapper();
        Citta citta = mapper.mapRow(mockResultSet, 1);

        // 3. Verifichiamo che il mappaggio Oggetto-Relazionale sia perfetto
        assertEquals(1L, citta.getId());
        assertEquals("Roma", citta.getNome());
        assertEquals("Italia", citta.getNazione());
        assertEquals("FCO", citta.getCodiceIata());
    }
}