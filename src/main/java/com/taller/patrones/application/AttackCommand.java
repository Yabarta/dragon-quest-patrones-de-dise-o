package com.taller.patrones.application;

import java.util.List;

public interface AttackCommand {
    List<DamageEvent> execute();
    void undo();
}
