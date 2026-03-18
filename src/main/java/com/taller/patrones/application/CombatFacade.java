package com.taller.patrones.application;

import com.taller.patrones.domain.Battle;

public class CombatFacade {
    private final BattleService battleService;

    public CombatFacade(BattleService battleService) {
        this.battleService = battleService;
    }

    // Simplified API: perform an attack regardless of who is attacker
    public Battle performAttack(String battleId, String attackName) {
        Battle battle = battleService.getBattle(battleId);
        if (battle == null) return null;

        if (battle.isPlayerTurn()) {
            battleService.executePlayerAttack(battleId, attackName);
        } else {
            battleService.executeEnemyAttack(battleId, attackName);
        }
        return battleService.getBattle(battleId);
    }

    public Battle startBattle(String playerName, String enemyName) {
        return battleService.startBattle(playerName, enemyName).battle();
    }

    public Battle getBattle(String battleId) { return battleService.getBattle(battleId); }

    public boolean undoLastAttack(String battleId) { return battleService.undoLastAttack(battleId); }
}
