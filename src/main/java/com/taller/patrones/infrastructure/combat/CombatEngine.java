package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Character;

/**
 * Motor de combate. Calcula daño y crea ataques.
 * <p>
 * Nota: Esta clase crece cada vez que añadimos un ataque nuevo o un tipo de daño distinto.
 */
public class CombatEngine {

    public Attack createAttack(String name) {
        return AttackFactoryRegistry.create(name);
    }

    public int calculateDamage(Character attacker, Character defender, Attack attack) {
        return DamageStrategyRegistry.getStrategy(attack.getType())
                .calculateDamage(attacker, defender, attack);
    }
}
