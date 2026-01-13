package yadwy.app.yadwyservice.product.domain.models

import yadwy.app.yadwyservice.sharedkernel.domain.models.base.ValueObject
import java.math.BigDecimal

@JvmInline
value class Money private constructor(val value: BigDecimal) : ValueObject, Comparable<Money> {

    init {
        require(value >= BigDecimal.ZERO) { "Money cannot be negative" }
    }

    override fun compareTo(other: Money): Int = value.compareTo(other.value)

    companion object {
        fun of(value: BigDecimal): Money = Money(value)
        fun of(value: Double): Money = Money(BigDecimal.valueOf(value))
        fun of(value: Long): Money = Money(BigDecimal.valueOf(value))
        val ZERO: Money = Money(BigDecimal.ZERO)
    }
}
