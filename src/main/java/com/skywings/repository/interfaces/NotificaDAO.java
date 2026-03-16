package com.skywings.repository.interfaces;

import java.util.List;
import java.util.Map;

public interface NotificaDAO {
    List<Map<String, Object>> getNotificheNonLette(Long utenteId);
    void segnaTutteComeLette(Long utenteId);
    void creaNotifichePerVoloModificato(Long idVolo, String messaggio);
}
