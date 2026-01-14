package yadwy.app.yadwyservice.sharedkernel.domain.models

import yadwy.app.yadwyservice.sharedkernel.domain.models.base.ValueObject
import java.math.BigDecimal
import java.math.RoundingMode

data class Amount(val value: BigDecimal) : ValueObject, Comparable<Amount> {

    init {
        require(value >= BigDecimal.ZERO) { "Amount cannot be negative" }
    }

    companion object {
        val ZERO: Amount = Amount(BigDecimal.ZERO)

        fun of(value: BigDecimal): Amount = Amount(value)
        fun of(value: Double): Amount = Amount(BigDecimal.valueOf(value))
        fun of(value: Long): Amount = Amount(BigDecimal.valueOf(value))
    }

    operator fun plus(increment: Amount): Amount {
        return Amount(value + increment.value)
    }

    operator fun minus(decrement: Amount): Amount {
        return Amount(value - decrement.value)
    }

    operator fun times(quantity: Quantity): Amount {
        return Amount(this.value.times(quantity.value.toBigDecimal()))
    }

    operator fun times(amount: Amount): Amount {
        return Amount(this.value.times(amount.value))
    }

    operator fun times(times: Int): Amount {
        return Amount(value.times(times.toBigDecimal()))
    }

    operator fun div(amount: Amount): Amount {
        return Amount(this.value.divide(amount.value, 2, RoundingMode.HALF_UP))
    }

    operator fun div(quantity: Quantity): Amount {
        return Amount(
            value.divide(quantity.value.toBigDecimal(), 2, RoundingMode.HALF_UP)
                .stripTrailingZeros()
        )
    }

    operator fun div(division: Int): Amount {
        return Amount(value.divide(division.toBigDecimal(), 2, RoundingMode.HALF_UP))
    }

    override operator fun compareTo(other: Amount): Int {
        return value.compareTo(other.value)
    }

    override fun toString(): String = value.toPlainString()

    fun round(): Amount {
        val roundedValue = value.setScale(0, RoundingMode.HALF_UP)
        return Amount(roundedValue)
    }

    fun roundDecimals(): Amount {
        val roundedValue = value.setScale(3, RoundingMode.FLOOR)
        return Amount(roundedValue)
    }

    fun roundToTwoDecimalsFloor(): Amount {
        val roundedValue = value.setScale(2, RoundingMode.FLOOR)
        return Amount(roundedValue)
    }
}
