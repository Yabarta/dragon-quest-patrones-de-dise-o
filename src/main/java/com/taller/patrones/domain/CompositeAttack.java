package com.taller.patrones.domain;

import java.util.List;

/**
 * Composite attack that groups multiple `Attack` instances and behaves like one.
 */
public class CompositeAttack extends Attack {
    private final List<Attack> parts;

    public CompositeAttack(String name, List<Attack> parts) {
        super(name, parts.stream().mapToInt(Attack::getBasePower).sum(), Attack.AttackType.NORMAL);
        this.parts = List.copyOf(parts);
    }

    public List<Attack> getParts() { return parts; }
}
