package com.taller.patrones.application;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Battle;
import com.taller.patrones.domain.Character;

import java.util.ArrayList;
import java.util.List;

public class ExecuteAttackCommand implements AttackCommand {
    private final String battleId;
    private final Battle battle;
    private final Character attacker;
    private final Character defender;
    private final int damage;
    private final Attack attack;
    private final com.taller.patrones.infrastructure.combat.CombatEngine combatEngine = new com.taller.patrones.infrastructure.combat.CombatEngine();

    // State to allow undo
    private int previousDefenderHp;
    private boolean previousFinished;
    private String previousCurrentTurn;
    private int previousLastDamage;
    private String previousLastDamageTarget;
    private int previousLogSize;

    public ExecuteAttackCommand(String battleId, Battle battle, Character attacker, Character defender, int damage, Attack attack) {
        this.battleId = battleId;
        this.battle = battle;
        this.attacker = attacker;
        this.defender = defender;
        this.damage = damage;
        this.attack = attack;
    }

    @Override
    public List<DamageEvent> execute() {
        List<DamageEvent> events = new ArrayList<>();

        // capture previous state
        previousDefenderHp = defender.getCurrentHp();
        previousFinished = battle.isFinished();
        previousCurrentTurn = battle.getCurrentTurn();
        previousLastDamage = battle.getLastDamage();
        previousLastDamageTarget = battle.getLastDamageTarget();
        previousLogSize = battle.getBattleLog().size();

        if (attack instanceof com.taller.patrones.domain.CompositeAttack) {
            com.taller.patrones.domain.CompositeAttack comp = (com.taller.patrones.domain.CompositeAttack) attack;
            for (Attack part : comp.getParts()) {
                int d = combatEngine.calculateDamage(attacker, defender, part);
                defender.takeDamage(d);
                String target = defender == battle.getPlayer() ? "player" : "enemy";
                battle.setLastDamage(d, target);
                String logMsg = attacker.getName() + " usa " + part.getName() + " y hace " + d + " de daño a " + defender.getName();
                battle.log(logMsg);
                events.add(new DamageEvent(battleId, attacker.getName(), defender.getName(), d, part.getName(), defender.getCurrentHp()));
                if (!defender.isAlive()) {
                    battle.finish(attacker.getName());
                    break;
                }
            }
            // after sequence, switch turn once
            battle.switchTurn();
        } else {
            // single attack
            defender.takeDamage(damage);
            String target = defender == battle.getPlayer() ? "player" : "enemy";
            battle.setLastDamage(damage, target);
            String logMsg = attacker.getName() + " usa " + attack.getName() + " y hace " + damage + " de daño a " + defender.getName();
            battle.log(logMsg);
            if (!defender.isAlive()) {
                battle.finish(attacker.getName());
            }
            battle.switchTurn();
            events.add(new DamageEvent(battleId, attacker.getName(), defender.getName(), damage, attack.getName(), defender.getCurrentHp()));
        }

        return events;
    }

    @Override
    public void undo() {
        // restore defender HP
        defender.restoreHp(previousDefenderHp);

        // restore last damage values
        battle.setLastDamage(previousLastDamage, previousLastDamageTarget);

        // remove log entries added by execute
        java.util.List<String> log = battle.getBattleLog();
        while (log.size() > previousLogSize) {
            battle.removeLastLogEntry();
        }

        // restore finished flag and current turn
        battle.setFinished(previousFinished);
        battle.setCurrentTurn(previousCurrentTurn);
    }
}
