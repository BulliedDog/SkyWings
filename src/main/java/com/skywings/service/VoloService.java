package com.skywings.service;

import com.skywings.dto.VoloDTO;
import com.skywings.mapper.VoloMapper;
import com.skywings.model.Volo;
import com.skywings.observer.Observer;
import com.skywings.observer.Subject;
import com.skywings.repository.interfaces.VoloDAO;
import com.skywings.strategy.TariffaContext;
import com.skywings.strategy.TariffaManager;
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

    @Autowired
    private TariffaManager tariffaManager;

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
        List<Volo> voliDalDb = getAllVoli();
        List<VoloDTO> voliDaMostrare = new ArrayList<>();

        for (Volo volo : voliDalDb) {
            VoloDTO dto = voloMapper.toDto(volo);

            // Richiediamo il Context dinamicamente configurato per QUESTO volo
            TariffaContext context = tariffaManager.getContextConfigurato(volo);

            // Eseguiamo il calcolo tramite il context
            dto.setPrezzoCalcolato(context.eseguiCalcolo(volo, volo.getPrezzoBase()));

            voliDaMostrare.add(dto);
        }
        return voliDaMostrare;
    }

    public VoloDTO getVoloByIdConPrezzo(Long id) {
        Volo volo = getVoloById(id);
        if (volo == null) {
            return null;
        }
        VoloDTO dto = voloMapper.toDto(volo);

        TariffaContext context = tariffaManager.getContextConfigurato(volo);
        dto.setPrezzoCalcolato(context.eseguiCalcolo(volo, volo.getPrezzoBase()));

        return dto;
    }

    public List<VoloDTO> getVoliFilteredConPrezzo(Long originId, Long destId, LocalDate date) {
        List<Volo> voliDalDb = getVoliFiltered(originId, destId, date);
        List<VoloDTO> voliDaMostrare = new ArrayList<>();

        for (Volo volo : voliDalDb) {
            VoloDTO dto = voloMapper.toDto(volo);

            TariffaContext context = tariffaManager.getContextConfigurato(volo);
            dto.setPrezzoCalcolato(context.eseguiCalcolo(volo, volo.getPrezzoBase()));

            voliDaMostrare.add(dto);
        }
        return voliDaMostrare;
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