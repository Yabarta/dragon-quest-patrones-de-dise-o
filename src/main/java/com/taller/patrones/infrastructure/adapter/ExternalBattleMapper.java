package com.taller.patrones.infrastructure.adapter;

import java.util.Map;

/**
 * Mapea distintos formatos de payload externo a un DTO interno normalizado.
 *
 * Soporta formatos del ejercicio:
 * - formato actual: fighter1_name, fighter1_hp, fighter1_atk, fighter2_name, ...
 * - nuevo proveedor: player.health, player.attack, enemy.health, ...
 */
public class ExternalBattleMapper {

    public ExternalBattleDto fromMap(Map<String, Object> body) {
        if (body == null) body = Map.of();

        // Primero, intenta formato original (fighter1_*)
        if (body.containsKey("fighter1_name") || body.containsKey("fighter2_name")) {
            String f1Name = (String) body.getOrDefault("fighter1_name", "Héroe");
            int f1Hp = ((Number) body.getOrDefault("fighter1_hp", 150)).intValue();
            int f1Atk = ((Number) body.getOrDefault("fighter1_atk", 25)).intValue();
            String f2Name = (String) body.getOrDefault("fighter2_name", "Dragón");
            int f2Hp = ((Number) body.getOrDefault("fighter2_hp", 120)).intValue();
            int f2Atk = ((Number) body.getOrDefault("fighter2_atk", 30)).intValue();
            return new ExternalBattleDto(f1Name, f1Hp, f1Atk, f2Name, f2Hp, f2Atk);
        }

        // Segundo, intenta formato nuevo proveedor: player.* and enemy.*
        if (body.containsKey("player") || body.containsKey("enemy")) {
            Object playerObj = body.get("player");
            Object enemyObj = body.get("enemy");

            String pName = "Héroe";
            int pHp = 150;
            int pAtk = 25;
            String eName = "Dragón";
            int eHp = 120;
            int eAtk = 30;

            if (playerObj instanceof Map) {
                Map<?,?> p = (Map<?,?>) playerObj;
                if (p.containsKey("name")) pName = (String) p.get("name");
                if (p.containsKey("health")) pHp = ((Number) p.get("health")).intValue();
                if (p.containsKey("attack")) pAtk = ((Number) p.get("attack")).intValue();
            }

            if (enemyObj instanceof Map) {
                Map<?,?> e = (Map<?,?>) enemyObj;
                if (e.containsKey("name")) eName = (String) e.get("name");
                if (e.containsKey("health")) eHp = ((Number) e.get("health")).intValue();
                if (e.containsKey("attack")) eAtk = ((Number) e.get("attack")).intValue();
            }

            return new ExternalBattleDto(pName, pHp, pAtk, eName, eHp, eAtk);
        }

        // Fallback: intenta detectar claves sueltas (player.health etc. en raíz)
        String f1Name = (String) body.getOrDefault("fighter1_name", body.getOrDefault("player.name", "Héroe"));
        int f1Hp = toInt(body.getOrDefault("fighter1_hp", body.getOrDefault("player.health", 150)));
        int f1Atk = toInt(body.getOrDefault("fighter1_atk", body.getOrDefault("player.attack", 25)));
        String f2Name = (String) body.getOrDefault("fighter2_name", body.getOrDefault("enemy.name", "Dragón"));
        int f2Hp = toInt(body.getOrDefault("fighter2_hp", body.getOrDefault("enemy.health", 120)));
        int f2Atk = toInt(body.getOrDefault("fighter2_atk", body.getOrDefault("enemy.attack", 30)));

        return new ExternalBattleDto(f1Name, f1Hp, f1Atk, f2Name, f2Hp, f2Atk);
    }

    private int toInt(Object o) {
        if (o instanceof Number) return ((Number) o).intValue();
        if (o instanceof String) {
            try { return Integer.parseInt((String) o); } catch (NumberFormatException ignored) {}
        }
        return 0;
    }
}
