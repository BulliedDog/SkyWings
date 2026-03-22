package com.skywings.service;

import com.skywings.model.Aereo;
import com.skywings.repository.interfaces.AereoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AereoService {

    @Autowired
    private AereoDAO aereoDAO;

    public List<Aereo> getAllAerei() {
        return aereoDAO.findAll();
    }

    public Aereo getAereoById(Long id) {
        return aereoDAO.findById(id).orElse(null);
    }

    public void addAereo(Aereo aereo) {
        aereoDAO.save(aereo);
    }

    public void deleteAereo(Long id) {
        aereoDAO.deleteById(id);
    }

    public List<Aereo> getAereiCasualiLimit2() {
        return aereoDAO.getAereiCasualiLimit2();
    }
}