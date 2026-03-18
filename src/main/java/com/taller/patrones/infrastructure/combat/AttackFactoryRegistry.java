package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registro simple de factorías de ataques.
 *
 * Al añadir nuevos ataques se puede registrar la factoría aquí
 * (o extender este registro para cargar con ServiceLoader si se desea).
 */
public class AttackFactoryRegistry {

    private static final Map<String, AttackFactory> REGISTRY = new ConcurrentHashMap<>();

    static {
        // Register built-in attacks
        register("TACKLE", () -> new Attack("Tackle", 40, Attack.AttackType.NORMAL));
        register("SLASH", () -> new Attack("Slash", 55, Attack.AttackType.NORMAL));
        register("FIREBALL", () -> new Attack("Fireball", 80, Attack.AttackType.SPECIAL));
        register("ICE_BEAM", () -> new Attack("Ice Beam", 70, Attack.AttackType.SPECIAL));
        register("POISON_STING", () -> new Attack("Poison Sting", 20, Attack.AttackType.STATUS));
        register("THUNDER", () -> new Attack("Thunder", 90, Attack.AttackType.SPECIAL));
        register("METEOR", () -> new Attack("Meteoro", 120, Attack.AttackType.SPECIAL));
        // Composite (combo) attacks
        register("COMBO_TRIPLE", () -> new com.taller.patrones.domain.CompositeAttack(
            "Combo Triple",
            java.util.List.of(
                create("TACKLE"),
                create("SLASH"),
                create("FIREBALL")
            )
        ));
    }

    public static void register(String name, AttackFactory factory) {
        if (name == null || factory == null) return;
        REGISTRY.put(name.toUpperCase(), factory);
    }

    public static Attack create(String name) {
        String n = name != null ? name.toUpperCase() : "";
        AttackFactory f = REGISTRY.get(n);
        if (f != null) return f.create();
        return new Attack("Golpe", 30, Attack.AttackType.NORMAL);
    }
}
