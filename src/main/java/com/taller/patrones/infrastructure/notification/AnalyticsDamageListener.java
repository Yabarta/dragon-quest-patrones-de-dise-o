package com.taller.patrones.infrastructure.notification;

import com.taller.patrones.application.DamageEvent;
import com.taller.patrones.application.DamageListener;

public class AnalyticsDamageListener implements DamageListener {
    @Override
    public void onDamage(DamageEvent event) {
        // Simulate sending an analytics event
        System.out.println("[ANALYTICS] battle=" + event.getBattleId()
            + " attacker=" + event.getAttacker()
            + " defender=" + event.getDefender()
            + " attack=" + event.getAttackName()
            + " damage=" + event.getDamage());
    }
}
