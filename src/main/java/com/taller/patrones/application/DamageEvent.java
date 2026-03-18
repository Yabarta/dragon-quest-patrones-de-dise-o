package com.taller.patrones.application;

public class DamageEvent {
    private final String battleId;
    private final String attacker;
    private final String defender;
    private final int damage;
    private final String attackName;
    private final int defenderRemainingHp;

    public DamageEvent(String battleId, String attacker, String defender, int damage, String attackName, int defenderRemainingHp) {
        this.battleId = battleId;
        this.attacker = attacker;
        this.defender = defender;
        this.damage = damage;
        this.attackName = attackName;
        this.defenderRemainingHp = defenderRemainingHp;
    }

    public String getBattleId() { return battleId; }
    public String getAttacker() { return attacker; }
    public String getDefender() { return defender; }
    public int getDamage() { return damage; }
    public String getAttackName() { return attackName; }
    public int getDefenderRemainingHp() { return defenderRemainingHp; }
}
