package com.taller.patrones.infrastructure.notification;

import com.taller.patrones.application.DamageEvent;
import com.taller.patrones.application.DamageListener;

import java.util.logging.Logger;

public class AuditDamageListener implements DamageListener {
    private static final Logger auditLogger = Logger.getLogger("audit");

    @Override
    public void onDamage(DamageEvent event) {
        // Write a concise audit entry
        auditLogger.info("[AUDIT] battle=" + event.getBattleId()
            + " attacker=" + event.getAttacker()
            + " defender=" + event.getDefender()
            + " damage=" + event.getDamage()
            + " remainingHp=" + event.getDefenderRemainingHp());
    }
}
