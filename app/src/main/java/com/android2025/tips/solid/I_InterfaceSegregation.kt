package com.android2025.tips.solid

/**
 * I — Interface Segregation Principle (ISP)
 *
 * "Clients should not be forced to depend on methods they do not use." — Robert C. Martin
 *
 * Prefer several small, focused interfaces over one "fat" interface. When an interface bundles
 * unrelated capabilities, implementers are forced to write dummy or throwing methods, and
 * clients get coupled to operations they never call — so a change to one of those methods
 * ripples into classes that don't care about it.
 *
 * ISP is closely related to LSP: fat interfaces are a common *cause* of LSP violations,
 * because implementers end up throwing from methods they can't support.
 *
 * Smells that signal a violation:
 *  - Implementations with empty bodies, `TODO()`, or `throw UnsupportedOperationException()`.
 *  - An interface with methods that are always used by different callers.
 *  - Test fakes that must implement 15 methods to exercise 1.
 *
 * Kotlin note: a class can implement many interfaces, and interfaces can extend each other,
 * so composing small roles (`interface MultiFunctionDevice : Printer, Scanner`) is cheap.
 *
 * Android takeaway: Android itself moved from giant listener interfaces toward single-method
 * ones (`View.OnClickListener`, Kotlin `fun interface`, lambdas in Compose `onClick`).
 */

// -----------------------------------------------------------------------------------------
// Example 1: workers
// -----------------------------------------------------------------------------------------
object IspWorkerExample {

    // ❌ Violation: Robot is forced to implement eat() and sleep(), which make no sense for it.
    object Bad {
        interface Worker {
            fun work(): String
            fun eat(): String
            fun sleep(): String
        }

        class Human : Worker {
            override fun work() = "Human working"
            override fun eat() = "Human eating"
            override fun sleep() = "Human sleeping"
        }

        class Robot : Worker {
            override fun work() = "Robot working"
            override fun eat() = throw UnsupportedOperationException("Robots don't eat")
            override fun sleep() = "" // silently does nothing — misleading
        }
    }

    // ✅ Refactored: each capability is its own role. Classes pick only what applies, and a
    // client (e.g. a shift scheduler) depends only on the role it needs.
    object Good {
        fun interface Workable { fun work(): String }
        fun interface Feedable { fun eat(): String }
        fun interface Restable { fun sleep(): String }
        fun interface Rechargeable { fun recharge(): String }

        class Human : Workable, Feedable, Restable {
            override fun work() = "Human working"
            override fun eat() = "Human eating"
            override fun sleep() = "Human sleeping"
        }

        class Robot : Workable, Rechargeable {
            override fun work() = "Robot working"
            override fun recharge() = "Robot recharging"
        }

        class ShiftScheduler {
            fun startShift(workers: List<Workable>) = workers.forEach { println(it.work()) }
        }

        class Cafeteria {
            fun lunch(diners: List<Feedable>) = diners.forEach { println(it.eat()) }
        }

        fun demo() {
            val human = Human()
            val robot = Robot()
            ShiftScheduler().startShift(listOf(human, robot))
            Cafeteria().lunch(listOf(human)) // Robot can't be passed here — by design
            println(robot.recharge())
        }
    }
}

// -----------------------------------------------------------------------------------------
// Example 2: office devices
// -----------------------------------------------------------------------------------------
object IspDeviceExample {

    // ❌ Violation: a basic printer must pretend to scan and fax.
    object Bad {
        interface MultiFunctionDevice {
            fun print(document: String)
            fun scan(document: String): String
            fun fax(document: String, number: String)
        }

        class BasicPrinter : MultiFunctionDevice {
            override fun print(document: String) = println("Printing $document")
            override fun scan(document: String): String = throw UnsupportedOperationException()
            override fun fax(document: String, number: String) = throw UnsupportedOperationException()
        }
    }

    // ✅ Refactored: small interfaces, composed when a device really supports more.
    object Good {
        interface Printer { fun print(document: String) }
        interface Scanner { fun scan(document: String): String }
        interface Fax { fun fax(document: String, number: String) }

        // Composition of roles — still usable anywhere a Printer or Scanner is expected.
        interface MultiFunctionDevice : Printer, Scanner, Fax

        class BasicPrinter : Printer {
            override fun print(document: String) = println("BasicPrinter printing $document")
        }

        class OfficeMachine : MultiFunctionDevice {
            override fun print(document: String) = println("OfficeMachine printing $document")
            override fun scan(document: String) = "scanned($document)"
            override fun fax(document: String, number: String) =
                println("OfficeMachine faxing $document to $number")
        }

        // Clients ask only for the capability they use.
        class ReportService(private val printer: Printer) {
            fun printReport() = printer.print("Q3 report")
        }

        class Archiver(private val scanner: Scanner) {
            fun archive(document: String) = println("Archived ${scanner.scan(document)}")
        }

        fun demo() {
            val office = OfficeMachine()
            ReportService(BasicPrinter()).printReport()
            ReportService(office).printReport()
            Archiver(office).archive("contract.pdf")
        }
    }
}

fun interfaceSegregationDemo() {
    IspWorkerExample.Good.demo()
    IspDeviceExample.Good.demo()
}
