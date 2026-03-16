package com.skywings.repository.repositories;

import com.skywings.repository.interfaces.NotificaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class NotificaRepository implements NotificaDAO {
    private final JdbcTemplate jdbcTemplate;

    public NotificaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Map<String, Object>> getNotificheNonLette(Long utenteId) {
        String sql = "SELECT * FROM notifiche WHERE utente_id = ? AND letta = FALSE ORDER BY data_creazione DESC";
        return jdbcTemplate.queryForList(sql, utenteId);
    }

    @Override
    public void segnaTutteComeLette(Long utenteId) {
        String sql = "UPDATE notifiche SET letta = TRUE WHERE utente_id = ?";
        jdbcTemplate.update(sql, utenteId);
    }

    @Override
    public void creaNotifichePerVoloModificato(Long idVolo, String messaggio) {
        // La query estrae in automatico gli id degli utenti dalla tabella prenotazioni
        // DISTINCT evita di inviare 2 notifiche se un utente ha prenotato 2 posti separati per lo stesso volo
        String sql = "INSERT INTO notifiche (utente_id, messaggio, letta, data_creazione) " +
                "SELECT DISTINCT utente_id, ?, FALSE, CURRENT_TIMESTAMP " +
                "FROM prenotazioni " +
                "WHERE volo_id = ?";

        // Eseguiamo l'update passando i parametri nell'ordine corretto (prima il messaggio per il SELECT, poi l'idVolo per la WHERE)
        int notificheCreate = jdbcTemplate.update(sql, messaggio, idVolo);

        System.out.println(">>> [DB] Create " + notificheCreate + " notifiche per il volo ID: " + idVolo);
    }
}