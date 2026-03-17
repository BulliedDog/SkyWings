package com.skywings.service;

import com.skywings.dto.VoloDTO;
import com.skywings.mapper.VoloMapper;
import com.skywings.model.Volo;
import com.skywings.observer.Observer;
import com.skywings.observer.Subject;
import com.skywings.repository.interfaces.VoloDAO;
import com.skywings.strategy.TariffaStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class VoloService implements Subject {

    private final List<Observer> observers = new ArrayList<>();

    @Autowired
    private VoloDAO voloDAO;

    @Autowired
    private VoloMapper voloMapper;

    public List<Volo> getAllVoli() {
        return voloDAO.findAll();
    }

    public Volo getVoloById(Long id) {
        return voloDAO.findById(id).orElse(null);
    }

    public List<Volo> getVoliFiltered(Long originId, Long destId, LocalDate date) {
        return voloDAO.findVoliFiltered(originId, destId, date);
    }

    @Transactional
    public void saveVolo(Volo voloAggiornato) {
        // 1. Verifichiamo se il volo esiste già (è un update)
        if (voloAggiornato.getId() != null) {
            Volo voloAttuale = voloDAO.findById(voloAggiornato.getId())
                    .orElseThrow(() -> new RuntimeException("Volo non trovato"));

            // 2. Controllo se lo stato è cambiato
            boolean statoCambiato = !voloAttuale.getStato().equals(voloAggiornato.getStato());

            // 3. Eseguo l'update/save
            voloDAO.save(voloAggiornato);

            // 4. Pubblico l'evento (tramite Pattern Observer) solo se lo stato è effettivamente cambiato
            if (statoCambiato) {
                notifyObservers(voloAggiornato);

                System.out.println("DEBUG: Stato cambiato in " + voloAggiornato.getStato() + ". Observer notificati.");
            }
        } else {
            // Se l'ID è null, è un nuovo inserimento (Create)
            voloDAO.save(voloAggiornato);
            System.out.println("DEBUG: Nuovo volo creato, nessuna notifica di cambio stato necessaria.");
        }
    }



    public void deleteVolo(Long id) {
        voloDAO.deleteById(id);
    }

    public List<VoloDTO> getAllVoliConPrezzo() {

    }

    public VoloDTO getVoloByIdConPrezzo(Long id) {

    }

    public List<VoloDTO> getVoliFilteredConPrezzo(Long originId, Long destId, LocalDate date) {

    }

    @Override
    public void addObserver(Observer o) {
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(Volo volo) {
        for (Observer o : observers) {
            o.update(volo);
        }
    }

    public void aggiornaStatoVolo(Long idVolo, Volo.StatoVolo nuovoStato) {
        Volo v = voloDAO.findById(idVolo)
                .orElseThrow(() -> new RuntimeException("Volo non trovato con ID: " + idVolo));

        if (!v.getStato().equals(nuovoStato)) {
            System.out.println("DEBUG: Aggiornamento stato e notifica per volo " + idVolo);
            v.setStato(nuovoStato);
            voloDAO.save(v);

            notifyObservers(v);
        }
    }
}