package com.skywings.observer;

import com.skywings.model.Volo;
import com.skywings.repository.interfaces.NotificaDAO;
import com.skywings.service.VoloService;
import org.springframework.stereotype.Component;

@Component
public class NotificaObserver implements Observer {

    private final NotificaDAO notificaDAO;

    public NotificaObserver(VoloService voloService, NotificaDAO notificaDAO) {
        this.notificaDAO = notificaDAO;
        voloService.addObserver(this); // Registrazione al subject
    }

    @Override
    public void update(Volo volo) {
        // messaggio notifica
        String messaggio = String.format(
                "Il volo %s è ora in stato: %s",
                volo.getCodiceVolo(),
                volo.getStato()
        );

        notificaDAO.creaNotifichePerVoloModificato(volo.getId(), messaggio);
    }
}