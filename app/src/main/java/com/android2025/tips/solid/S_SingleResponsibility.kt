package com.android2025.tips.solid

/**
 * S — Single Responsibility Principle (SRP)
 *
 * "A class should have one, and only one, reason to change." — Robert C. Martin
 *
 * A "responsibility" is not "one method" — it's one *reason to change*, usually one actor
 * or concern in the system (business rules, persistence, presentation, notifications...).
 * When a single class mixes several of them, a change requested for one concern (e.g. a new
 * email template) risks breaking an unrelated one (e.g. how users are saved), and the class
 * becomes hard to test because you can't exercise one piece without dragging the others along.
 *
 * Smells that signal a violation:
 *  - Class names like `Manager`, `Helper`, `Utils` or `Service` that do "everything".
 *  - Describing the class needs the word "and" ("it validates AND saves AND emails").
 *  - Unit tests need to fake a database just to check a validation rule.
 *
 * Android takeaway: this is why we split ViewModel (UI state) / Repository (data access) /
 * DAO (SQL) in the to-do app instead of putting Room calls inside a Composable or Activity.
 */

// -----------------------------------------------------------------------------------------
// Example 1: user registration
// -----------------------------------------------------------------------------------------
object SrpUserExample {

    data class User(val name: String, val email: String)

    // ❌ Violation: validation rules, persistence and notifications all live in one class.
    // Changing the email provider, the storage, or a validation rule all edit this same file.
    object Bad {
        class UserService {
            private val users = mutableListOf<User>()

            fun register(user: User) {
                if (user.name.isBlank()) throw IllegalArgumentException("Name required")
                if (!user.email.contains("@")) throw IllegalArgumentException("Invalid email")

                users.add(user) // imagine SQL here

                println("SMTP -> ${user.email}: Welcome ${user.name}!") // imagine SMTP here
            }
        }
    }

    // ✅ Refactored: each class has one reason to change; UserRegistration only coordinates.
    // Every piece can now be tested (or replaced) on its own.
    object Good {
        class UserValidator {
            fun validate(user: User) {
                require(user.name.isNotBlank()) { "Name required" }
                require(user.email.contains("@")) { "Invalid email" }
            }
        }

        class UserRepository {
            private val users = mutableListOf<User>()
            fun save(user: User) { users.add(user) }
            fun count(): Int = users.size
        }

        class EmailNotifier {
            fun sendWelcome(user: User) = println("Email -> ${user.email}: Welcome ${user.name}!")
        }

        class UserRegistration(
            private val validator: UserValidator,
            private val repository: UserRepository,
            private val notifier: EmailNotifier,
        ) {
            fun register(user: User) {
                validator.validate(user)
                repository.save(user)
                notifier.sendWelcome(user)
            }
        }
    }

    fun demo() {
        val registration = Good.UserRegistration(
            validator = Good.UserValidator(),
            repository = Good.UserRepository(),
            notifier = Good.EmailNotifier(),
        )
        registration.register(User(name = "Ada", email = "ada@example.com"))
    }
}

// -----------------------------------------------------------------------------------------
// Example 2: invoices
// -----------------------------------------------------------------------------------------
object SrpInvoiceExample {

    data class LineItem(val description: String, val price: Double, val quantity: Int)

    // ❌ Violation: the Invoice knows how to calculate money, how to render itself as text,
    // and where to output it. Accounting, design and "print to PDF instead" requests all
    // collide in the same class.
    object Bad {
        class Invoice(private val items: List<LineItem>) {
            fun total(): Double = items.sumOf { it.price * it.quantity }

            fun format(): String = buildString {
                items.forEach { appendLine("${it.description} x${it.quantity} = ${it.price * it.quantity}") }
                append("TOTAL: ${total()}")
            }

            fun print() = println(format())
        }
    }

    // ✅ Refactored: Invoice is pure data + business math. Formatting and output are separate,
    // so adding an HTML formatter or a file printer doesn't touch the money logic.
    object Good {
        class Invoice(val items: List<LineItem>) {
            fun total(): Double = items.sumOf { it.price * it.quantity }
        }

        class InvoiceFormatter {
            fun format(invoice: Invoice): String = buildString {
                invoice.items.forEach {
                    appendLine("${it.description} x${it.quantity} = ${it.price * it.quantity}")
                }
                append("TOTAL: ${invoice.total()}")
            }
        }

        class InvoicePrinter {
            fun print(text: String) = println(text)
        }
    }

    fun demo() {
        val invoice = Good.Invoice(
            listOf(
                LineItem("Keyboard", 49.99, 1),
                LineItem("USB-C cable", 9.50, 2),
            )
        )
        Good.InvoicePrinter().print(Good.InvoiceFormatter().format(invoice))
    }
}

fun singleResponsibilityDemo() {
    SrpUserExample.demo()
    SrpInvoiceExample.demo()
}
