package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DamageStrategyRegistry {

    private static final Map<Attack.AttackType, DamageStrategy> STRATEGIES = new ConcurrentHashMap<>();

    static {
        STRATEGIES.put(Attack.AttackType.NORMAL, new NormalDamageStrategy());
        STRATEGIES.put(Attack.AttackType.SPECIAL, new SpecialDamageStrategy());
        STRATEGIES.put(Attack.AttackType.STATUS, new StatusDamageStrategy());
        STRATEGIES.put(Attack.AttackType.CRITICAL, new CriticalDamageStrategy());
    }

    public static DamageStrategy getStrategy(Attack.AttackType type) {
        return STRATEGIES.getOrDefault(type, STRATEGIES.get(Attack.AttackType.NORMAL));
    }

    public static void register(Attack.AttackType type, DamageStrategy strategy) {
        if (type == null || strategy == null) return;
        STRATEGIES.put(type, strategy);
    }
}
