package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Character;

/**
 * Los ataques de estado no hacen daño directo.
 */
public class StatusDamageStrategy implements DamageStrategy {

    @Override
    public int calculateDamage(Character attacker, Character defender, Attack attack) {
        return 0;
    }
}
