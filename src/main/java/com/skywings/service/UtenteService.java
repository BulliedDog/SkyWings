package com.skywings.service;

import com.skywings.model.Utente;
import com.skywings.repository.interfaces.UtenteDAO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UtenteService {

    private final UtenteDAO utenteDAO;
    private final AutenticazioneService autenticazioneService;

    public UtenteService(UtenteDAO utenteDAO, AutenticazioneService autenticazioneService) {
        this.utenteDAO = utenteDAO;
        this.autenticazioneService = autenticazioneService;
    }

    public List<Utente> getAllUtenti() {
        return utenteDAO.findAll();
    }

    public Utente getUtenteById(Long id) {
        return utenteDAO.findById(id).orElse(null);
    }

    public void updateUtente(Utente utenteDalForm) {
        // 1. Se l'ID è nullo, è un NUOVO utente: cripta e salva
        if (utenteDalForm.getId() == null || utenteDalForm.getId() == 0) {
            String hash = autenticazioneService.criptaPassword(utenteDalForm.getPassword());
            utenteDalForm.setPassword(hash);
            utenteDAO.save(utenteDalForm); // Inserimento
            return;
        }

        // 2. Se l'ID esiste, è una MODIFICA: recupera l'originale
        Optional<Utente> optional = utenteDAO.findById(utenteDalForm.getId());
        if (optional.isPresent()) {
            Utente utenteNelDb = optional.get();

            // QUI STA LA LOGICA CHE CERCHI:
            // Se il form ti ha inviato null (perché vuoto), ripristiniamo l'hash esistente
            if (utenteDalForm.getPassword() == null) {
                utenteDalForm.setPassword(utenteNelDb.getPassword());
            } else {
                // Se non è null, l'utente ha scritto una nuova password: criptala
                utenteDalForm.setPassword(autenticazioneService.criptaPassword(utenteDalForm.getPassword()));
            }

            // 3. Esegui l'update solo ora
            utenteDAO.update(utenteDalForm);
        } else {
            throw new RuntimeException("Utente non trovato");
        }
    }

    public void deleteUtente(Long id) {
        utenteDAO.deleteById(id);
    }

    public List<Utente> getAllStaff() {
        return utenteDAO.findAll().stream()
                .filter(u -> !"USER".equals(u.getRuolo()))
                .collect(Collectors.toList());
    }

    public List<Utente> getMembriEquipaggio() {
        return utenteDAO.findAll().stream()
                .filter(u -> "PILOTA".equals(u.getRuolo()) || "HOSTESS".equals(u.getRuolo()))
                .collect(Collectors.toList());
    }
}