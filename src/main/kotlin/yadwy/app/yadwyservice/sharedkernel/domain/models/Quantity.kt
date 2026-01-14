package yadwy.app.yadwyservice.sharedkernel.domain.models

import yadwy.app.yadwyservice.sharedkernel.domain.exceptions.InvalidQuantityException
import yadwy.app.yadwyservice.sharedkernel.domain.models.base.ValueObject

data class Quantity(val value: Int) : ValueObject, Comparable<Quantity> {

    companion object {
        val ZERO: Quantity = Quantity(0)

        fun of(value: Int): Quantity = Quantity(value)
    }

    init {
        if (value < 0) throw InvalidQuantityException(value)
    }

    operator fun plus(increment: Int): Quantity {
        return Quantity(value + increment)
    }

    operator fun plus(increment: Quantity): Quantity {
        return Quantity(value + increment.value)
    }

    operator fun minus(decrement: Int): Quantity {
        return Quantity(value - decrement)
    }

    operator fun minus(decrement: Quantity): Quantity {
        return Quantity(value - decrement.value)
    }

    override operator fun compareTo(other: Quantity): Int {
        return value.compareTo(other.value)
    }

    override fun toString(): String = value.toString()
}
