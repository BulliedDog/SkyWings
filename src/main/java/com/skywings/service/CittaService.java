package com.skywings.service;

import com.skywings.model.Citta;
import com.skywings.repository.interfaces.CittaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CittaService {

    @Autowired
    private CittaDAO cittaDAO;

    public List<Citta> getAllCitta() {
        return cittaDAO.findAll();
    }

    public Citta getCittaById(Long id) {
        return cittaDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Città non trovata con ID: " + id));
    }

    public void addCitta(Citta citta) {
        cittaDAO.save(citta);
    }

    public void deleteCitta(Long id) {
        cittaDAO.deleteById(id);
    }

    public Map<Long, String> getMappaNomiCitta() {
        List<Citta> tutteLeCitta = cittaDAO.findAll();
        // Trasformiamo la lista in mappa: {1: "Roma", 2: "Milano", ...}
        return tutteLeCitta.stream()
                .collect(Collectors.toMap(Citta::getId, Citta::getNome));
    }

    public List<Citta> getCittaConVoliProgrammatiLimit4() {
        return cittaDAO.getCittaConVoliProgrammatiLimit4();
    }
}