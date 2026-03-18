package com.taller.patrones.infrastructure.notification;

import com.taller.patrones.application.DamageEvent;
import com.taller.patrones.application.DamageListener;

public class RealtimeStatsDamageListener implements DamageListener {
    @Override
    public void onDamage(DamageEvent event) {
        // Simulate updating in-memory real-time stats
        System.out.println("[REALTIME] update stats for battle=" + event.getBattleId()
            + " defender=" + event.getDefender()
            + " remainingHp=" + event.getDefenderRemainingHp());
    }
}
