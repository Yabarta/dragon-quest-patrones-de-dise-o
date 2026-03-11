package com.taller.patrones.infrastructure.adapter;

/**
 * DTO normalizado para representar la información necesaria para crear
 * una batalla a partir de distintos formatos externos.
 */
public class ExternalBattleDto {
    private final String fighter1Name;
    private final int fighter1Hp;
    private final int fighter1Atk;
    private final String fighter2Name;
    private final int fighter2Hp;
    private final int fighter2Atk;

    public ExternalBattleDto(String fighter1Name, int fighter1Hp, int fighter1Atk,
                             String fighter2Name, int fighter2Hp, int fighter2Atk) {
        this.fighter1Name = fighter1Name;
        this.fighter1Hp = fighter1Hp;
        this.fighter1Atk = fighter1Atk;
        this.fighter2Name = fighter2Name;
        this.fighter2Hp = fighter2Hp;
        this.fighter2Atk = fighter2Atk;
    }

    public String getFighter1Name() { return fighter1Name; }
    public int getFighter1Hp() { return fighter1Hp; }
    public int getFighter1Atk() { return fighter1Atk; }
    public String getFighter2Name() { return fighter2Name; }
    public int getFighter2Hp() { return fighter2Hp; }
    public int getFighter2Atk() { return fighter2Atk; }
}
