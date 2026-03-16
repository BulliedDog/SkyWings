package com.skywings.observer;

import com.skywings.model.Volo;

public interface Subject {
    void addObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers(Volo volo);
}