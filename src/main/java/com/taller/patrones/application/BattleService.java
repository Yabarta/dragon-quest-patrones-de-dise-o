package com.taller.patrones.application;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Battle;
import com.taller.patrones.domain.Character;
import com.taller.patrones.infrastructure.combat.CombatEngine;
import com.taller.patrones.infrastructure.persistence.BattleRepository;
import com.taller.patrones.infrastructure.notification.AnalyticsDamageListener;
import com.taller.patrones.infrastructure.notification.AuditDamageListener;
import com.taller.patrones.infrastructure.notification.RealtimeStatsDamageListener;

import java.util.List;
import java.util.UUID;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * Caso de uso: gestionar batallas.
 * <p>
 * Nota: Crea sus propias dependencias con new. Cada vez que necesitamos
 * un CombatEngine o BattleRepository, hacemos new aquí.
 */
public class BattleService {

    private final CombatEngine combatEngine = new CombatEngine();
    private final BattleRepository battleRepository = BattleRepository.getInstance();
    private final java.util.List<DamageListener> damageListeners = new java.util.ArrayList<>();
    private final Map<String, Deque<AttackCommand>> commandHistory = new HashMap<>();

    public BattleService() {
        this.addDamageListener(new AnalyticsDamageListener());
        this.addDamageListener(new AuditDamageListener());
        this.addDamageListener(new RealtimeStatsDamageListener());
    }

    public static final List<String> PLAYER_ATTACKS = List.of("TACKLE", "SLASH", "FIREBALL", "ICE_BEAM", "POISON_STING", "THUNDER");
    public static final List<String> ENEMY_ATTACKS = List.of("TACKLE", "SLASH", "FIREBALL");

    public BattleStartResult startBattle(String playerName, String enemyName) {
        Character player = Character.builder()
            .name(playerName != null ? playerName : "Héroe")
            .maxHp(150)
            .attack(25)
            .defense(15)
            .speed(20)
            .build();

        Character enemy = Character.builder()
            .name(enemyName != null ? enemyName : "Dragón")
            .maxHp(120)
            .attack(30)
            .defense(10)
            .speed(15)
            .build();

        Battle battle = new Battle(player, enemy);
        String battleId = UUID.randomUUID().toString();
        battleRepository.save(battleId, battle);

        return new BattleStartResult(battleId, battle);
    }

    public Battle getBattle(String battleId) {
        return battleRepository.findById(battleId);
    }

    public void executePlayerAttack(String battleId, String attackName) {
        Battle battle = battleRepository.findById(battleId);
        if (battle == null || battle.isFinished() || !battle.isPlayerTurn()) return;

        Attack attack = combatEngine.createAttack(attackName);
        int damage = combatEngine.calculateDamage(battle.getPlayer(), battle.getEnemy(), attack);

        ExecuteAttackCommand cmd = new ExecuteAttackCommand(battleId, battle, battle.getPlayer(), battle.getEnemy(), damage, attack);
        java.util.List<DamageEvent> events = cmd.execute();
        for (DamageEvent e : events) notifyDamageListeners(e);
        commandHistory.computeIfAbsent(battleId, k -> new ArrayDeque<>()).push(cmd);
    }

    public void executeEnemyAttack(String battleId, String attackName) {
        Battle battle = battleRepository.findById(battleId);
        if (battle == null || battle.isFinished() || battle.isPlayerTurn()) return;

        Attack attack = combatEngine.createAttack(attackName != null ? attackName : "TACKLE");
        int damage = combatEngine.calculateDamage(battle.getEnemy(), battle.getPlayer(), attack);

        ExecuteAttackCommand cmd = new ExecuteAttackCommand(battleId, battle, battle.getEnemy(), battle.getPlayer(), damage, attack);
        java.util.List<DamageEvent> events = cmd.execute();
        for (DamageEvent e : events) notifyDamageListeners(e);
        commandHistory.computeIfAbsent(battleId, k -> new ArrayDeque<>()).push(cmd);
    }

    public boolean undoLastAttack(String battleId) {
        Deque<AttackCommand> history = commandHistory.get(battleId);
        if (history == null || history.isEmpty()) return false;
        AttackCommand cmd = history.pop();
        try {
            cmd.undo();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void addDamageListener(DamageListener listener) {
        if (listener != null) this.damageListeners.add(listener);
    }

    public void removeDamageListener(DamageListener listener) {
        this.damageListeners.remove(listener);
    }

    private void notifyDamageListeners(DamageEvent event) {
        for (DamageListener l : new java.util.ArrayList<>(damageListeners)) {
            try {
                l.onDamage(event);
            } catch (Exception ignored) {
            }
        }
    }

    public BattleStartResult startBattleFromExternal(String fighter1Name, int fighter1Hp, int fighter1Atk,
                                                     String fighter2Name, int fighter2Hp, int fighter2Atk) {
        Character player = Character.builder()
            .name(fighter1Name)
            .maxHp(fighter1Hp)
            .attack(fighter1Atk)
            .defense(10)
            .speed(10)
            .build();

        Character enemy = Character.builder()
            .name(fighter2Name)
            .maxHp(fighter2Hp)
            .attack(fighter2Atk)
            .defense(10)
            .speed(10)
            .build();
        Battle battle = new Battle(player, enemy);
        String battleId = UUID.randomUUID().toString();
        battleRepository.save(battleId, battle);
        return new BattleStartResult(battleId, battle);
    }

    public record BattleStartResult(String battleId, Battle battle) {}
}
