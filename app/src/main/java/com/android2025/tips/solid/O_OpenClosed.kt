package com.android2025.tips.solid

import kotlin.math.PI

/**
 * O — Open/Closed Principle (OCP)
 *
 * "Software entities should be open for extension, but closed for modification."
 *  — Bertrand Meyer
 *
 * You should be able to add new behavior (a new discount, a new shape, a new payment method)
 * by *adding* code — a new class — instead of *editing* code that already works and is already
 * tested. The usual tool is an abstraction (interface / abstract class / sealed type) that the
 * stable code depends on, with each variation living in its own implementation.
 *
 * Smells that signal a violation:
 *  - A growing `when` / `if-else` chain on a "type" field that you edit for every new case.
 *  - The same `when(type)` duplicated in several places — forget one and you get a bug.
 *  - A bug fix for one case accidentally changes the behavior of another.
 *
 * Kotlin note: a `sealed` hierarchy + `when` is fine when the set of cases is truly closed and
 * owned by you (the compiler forces exhaustiveness). Prefer an interface when *other* modules
 * or future features need to plug in new cases.
 *
 * Android takeaway: RecyclerView adapters with view types, analytics trackers, or payment
 * providers are classic places where a strategy interface beats a giant `when`.
 */

// -----------------------------------------------------------------------------------------
// Example 1: discounts
// -----------------------------------------------------------------------------------------
object OcpDiscountExample {

    // ❌ Violation: every new customer tier means editing this function (and re-testing it).
    object Bad {
        enum class CustomerType { REGULAR, PREMIUM, VIP }

        class DiscountCalculator {
            fun discount(type: CustomerType, amount: Double): Double = when (type) {
                CustomerType.REGULAR -> 0.0
                CustomerType.PREMIUM -> amount * 0.10
                CustomerType.VIP -> amount * 0.20
                // adding STUDENT? -> modify this class again
            }
        }
    }

    // ✅ Refactored: the calculator depends on DiscountPolicy. A new tier is a new class;
    // DiscountCalculator never changes again.
    object Good {
        fun interface DiscountPolicy {
            fun discount(amount: Double): Double
        }

        object RegularDiscount : DiscountPolicy {
            override fun discount(amount: Double) = 0.0
        }

        object PremiumDiscount : DiscountPolicy {
            override fun discount(amount: Double) = amount * 0.10
        }

        object VipDiscount : DiscountPolicy {
            override fun discount(amount: Double) = amount * 0.20
        }

        // Extension added later, without touching anything above:
        class SeasonalDiscount(private val percent: Double) : DiscountPolicy {
            override fun discount(amount: Double) = amount * percent
        }

        class DiscountCalculator {
            fun finalPrice(amount: Double, policy: DiscountPolicy): Double =
                amount - policy.discount(amount)
        }

        fun demo() {
            val calculator = DiscountCalculator()
            listOf(RegularDiscount, PremiumDiscount, VipDiscount, SeasonalDiscount(0.30)).forEach {
                println("${it::class.simpleName}: ${calculator.finalPrice(100.0, it)}")
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// Example 2: shapes
// -----------------------------------------------------------------------------------------
object OcpShapeExample {

    // ❌ Violation: the calculator type-checks every shape. A Triangle means editing it.
    object Bad {
        class Circle(val radius: Double)
        class Rectangle(val width: Double, val height: Double)

        class AreaCalculator {
            fun area(shape: Any): Double = when (shape) {
                is Circle -> PI * shape.radius * shape.radius
                is Rectangle -> shape.width * shape.height
                else -> throw IllegalArgumentException("Unknown shape")
            }
        }
    }

    // ✅ Refactored: each shape knows its own area, so the calculator is closed for
    // modification — new shapes just implement Shape.
    object Good {
        interface Shape {
            fun area(): Double
        }

        class Circle(private val radius: Double) : Shape {
            override fun area() = PI * radius * radius
        }

        class Rectangle(private val width: Double, private val height: Double) : Shape {
            override fun area() = width * height
        }

        class Triangle(private val base: Double, private val height: Double) : Shape {
            override fun area() = base * height / 2
        }

        class AreaCalculator {
            fun totalArea(shapes: List<Shape>): Double = shapes.sumOf { it.area() }
        }

        fun demo() {
            val shapes = listOf(Circle(1.0), Rectangle(2.0, 3.0), Triangle(4.0, 5.0))
            println("Total area: ${AreaCalculator().totalArea(shapes)}")
        }
    }
}

// -----------------------------------------------------------------------------------------
// Example 3 (bonus): payment methods
// -----------------------------------------------------------------------------------------
object OcpPaymentExample {

    // ✅ Checkout depends only on PaymentProcessor. Shipping "Pay with crypto" next sprint
    // is a new class plus one line of wiring (in the to-do app that would be a Koin module).
    interface PaymentProcessor {
        val name: String
        fun pay(amount: Double): Boolean
    }

    class CardPayment : PaymentProcessor {
        override val name = "Card"
        override fun pay(amount: Double) = true.also { println("Charged $amount to card") }
    }

    class PayPalPayment : PaymentProcessor {
        override val name = "PayPal"
        override fun pay(amount: Double) = true.also { println("Charged $amount via PayPal") }
    }

    class Checkout(private val processor: PaymentProcessor) {
        fun complete(amount: Double) {
            val ok = processor.pay(amount)
            println("${processor.name} payment ${if (ok) "succeeded" else "failed"}")
        }
    }

    fun demo() {
        Checkout(CardPayment()).complete(25.0)
        Checkout(PayPalPayment()).complete(40.0)
    }
}

fun openClosedDemo() {
    OcpDiscountExample.Good.demo()
    OcpShapeExample.Good.demo()
    OcpPaymentExample.demo()
}
