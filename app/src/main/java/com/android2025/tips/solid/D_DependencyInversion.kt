package com.android2025.tips.solid

/**
 * D — Dependency Inversion Principle (DIP)
 *
 * "A. High-level modules should not depend on low-level modules. Both should depend on
 *     abstractions.
 *  B. Abstractions should not depend on details. Details should depend on abstractions."
 *  — Robert C. Martin
 *
 * High-level code holds the business policy ("place an order", "notify the user"). Low-level
 * code holds the mechanics (MySQL, Retrofit, SMTP, Firebase). Without DIP, the policy imports
 * and constructs the mechanics directly, so swapping a database or faking it in a test means
 * editing business code. With DIP, the policy declares the interface it needs, and the
 * mechanics implement it — the source-code dependency arrow is "inverted".
 *
 * DIP (the principle) vs DI (the technique): Dependency *Injection* — passing dependencies in,
 * usually via the constructor — is how you apply DIP. A DI container (Koin, Hilt) just
 * automates the wiring.
 *
 * Smells that signal a violation:
 *  - `private val db = MySqlDatabase()` inside a service/ViewModel.
 *  - You can't unit-test a class without a network, database or Android device.
 *  - Singletons accessed globally (`Api.instance.get(...)`) from business logic.
 *
 * Android takeaway: see the to-do app — `TodoListViewModel` depends on the `TodoRepository`
 * interface, `RoomTodoRepository` is the detail, Koin wires them in `todo/di/TodoModule.kt`,
 * and unit tests swap in `FakeTodoRepository` without touching the ViewModel.
 */

// -----------------------------------------------------------------------------------------
// Example 1: orders and persistence
// -----------------------------------------------------------------------------------------
object DipOrderExample {

    data class Order(val id: Int, val total: Double)

    // ❌ Violation: OrderService creates its own MySqlDatabase. It can't run without MySQL,
    // and moving to Room/Firestore means rewriting OrderService.
    object Bad {
        class MySqlDatabase {
            fun insert(order: Order) = println("INSERT INTO orders VALUES (${order.id}, ${order.total})")
        }

        class OrderService {
            private val database = MySqlDatabase() // hard-wired low-level detail

            fun place(order: Order) {
                require(order.total > 0) { "Empty order" }
                database.insert(order)
            }
        }
    }

    // ✅ Refactored: OrderService owns the abstraction it needs (OrderRepository) and receives
    // an implementation through its constructor.
    object Good {
        interface OrderRepository {
            fun save(order: Order)
            fun all(): List<Order>
        }

        class MySqlOrderRepository : OrderRepository {
            private val rows = mutableListOf<Order>()
            override fun save(order: Order) {
                println("INSERT INTO orders VALUES (${order.id}, ${order.total})")
                rows.add(order)
            }
            override fun all() = rows.toList()
        }

        // Perfect for unit tests — same idea as FakeTodoRepository.
        class InMemoryOrderRepository : OrderRepository {
            private val orders = mutableListOf<Order>()
            override fun save(order: Order) { orders.add(order) }
            override fun all() = orders.toList()
        }

        class OrderService(private val repository: OrderRepository) {
            fun place(order: Order) {
                require(order.total > 0) { "Empty order" }
                repository.save(order)
            }
        }
    }

    fun demo() {
        // Production wiring
        Good.OrderService(Good.MySqlOrderRepository()).place(Order(id = 1, total = 99.9))

        // Test-style wiring: no database needed
        val fakeRepo = Good.InMemoryOrderRepository()
        Good.OrderService(fakeRepo).place(Order(id = 2, total = 10.0))
        println("Saved in memory: ${fakeRepo.all()}")
    }
}

// -----------------------------------------------------------------------------------------
// Example 2: notifications
// -----------------------------------------------------------------------------------------
object DipNotificationExample {

    // ❌ Violation: NotificationManager is glued to email. Adding SMS means new fields,
    // new branches, and re-testing everything.
    object Bad {
        class EmailSender {
            fun sendEmail(to: String, body: String) = println("Email to $to: $body")
        }

        class NotificationManager {
            private val email = EmailSender()
            fun notify(user: String, message: String) = email.sendEmail(user, message)
        }
    }

    // ✅ Refactored: the high-level manager depends on MessageSender. Channels are details
    // plugged in from outside — one, many, or a fake in tests.
    object Good {
        fun interface MessageSender {
            fun send(to: String, message: String)
        }

        class EmailSender : MessageSender {
            override fun send(to: String, message: String) = println("Email to $to: $message")
        }

        class SmsSender : MessageSender {
            override fun send(to: String, message: String) = println("SMS to $to: $message")
        }

        class PushSender : MessageSender {
            override fun send(to: String, message: String) = println("Push to $to: $message")
        }

        class NotificationManager(private val senders: List<MessageSender>) {
            fun notify(user: String, message: String) = senders.forEach { it.send(user, message) }
        }

        // In the to-do app this wiring would live in a Koin module, e.g.:
        //   single<MessageSender>(named("email")) { EmailSender() }
        //   single { NotificationManager(listOf(get(named("email")), ...)) }

        fun demo() {
            val manager = NotificationManager(listOf(EmailSender(), SmsSender(), PushSender()))
            manager.notify("ada", "Your order shipped")

            // A lambda works too, thanks to `fun interface`:
            NotificationManager(listOf(MessageSender { to, msg -> println("Log[$to]: $msg") }))
                .notify("ada", "Debug message")
        }
    }
}

fun dependencyInversionDemo() {
    DipOrderExample.demo()
    DipNotificationExample.Good.demo()
}
