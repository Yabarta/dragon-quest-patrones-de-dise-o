package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;

/**
 * Factoría para crear instancias de `Attack`.
 */
@FunctionalInterface
public interface AttackFactory {
    Attack create();
}
