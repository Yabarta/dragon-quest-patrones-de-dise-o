package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Character;


public class CriticalDamageStrategy implements DamageStrategy {

    private final NormalDamageStrategy base = new NormalDamageStrategy();

    @Override
    public int calculateDamage(Character attacker, Character defender, Attack attack) {
        int dmg = base.calculateDamage(attacker, defender, attack);
        if (Math.random() < 0.20) {
            dmg = Math.max(1, (int) Math.round(dmg * 1.5));
        }
        return dmg;
    }
}
