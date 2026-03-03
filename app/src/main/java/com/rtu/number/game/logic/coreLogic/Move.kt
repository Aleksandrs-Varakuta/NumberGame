package com.rtu.number.game.logic.coreLogic

abstract class Move {
    abstract override fun equals(other: Any?): Boolean
    abstract override fun hashCode(): Int
    abstract override fun toString(): String
}
