package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Character;

/**
 * Strategy para calcular daño de un ataque.
 */
public interface DamageStrategy {
    int calculateDamage(Character attacker, Character defender, Attack attack);
}
