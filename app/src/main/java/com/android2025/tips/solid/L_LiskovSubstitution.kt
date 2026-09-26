package com.android2025.tips.solid

/**
 * L — Liskov Substitution Principle (LSP)
 *
 * "If S is a subtype of T, then objects of type T may be replaced with objects of type S
 *  without altering any of the desirable properties of the program." — Barbara Liskov
 *
 * In practice: any code written against a base type must keep working, unchanged and
 * unsurprised, when it receives a subtype. A subclass may *add* behavior, but must not break
 * the promises (the contract) of its parent:
 *  - don't demand stronger preconditions (accept at least what the parent accepts),
 *  - don't deliver weaker postconditions (guarantee at least what the parent guarantees),
 *  - don't throw exceptions the parent never throws,
 *  - keep the parent's invariants (e.g. "setting width doesn't change height").
 *
 * "Is-a" in the real world does NOT automatically mean "is-a" in code: a square *is* a
 * rectangle in geometry, but a mutable Square is not substitutable for a mutable Rectangle.
 *
 * Smells that signal a violation:
 *  - Overrides that throw `UnsupportedOperationException` / `NotImplementedError`.
 *  - Callers doing `if (x is SpecialSubtype)` to avoid a crash.
 *  - Empty overrides that silently do nothing.
 *
 * Android takeaway: a `FakeTodoRepository` used in tests must honor the same contract as
 * `RoomTodoRepository` (e.g. ordering, emitting updates), otherwise tests pass for the wrong
 * reasons.
 */

// -----------------------------------------------------------------------------------------
// Example 1: Rectangle / Square
// -----------------------------------------------------------------------------------------
object LspRectangleExample {

    // ❌ Violation: Square overrides setters to keep sides equal, which breaks the Rectangle
    // invariant "changing width leaves height alone". Code that works for Rectangle fails.
    object Bad {
        open class Rectangle {
            open var width: Int = 0
            open var height: Int = 0
            fun area() = width * height
        }

        class Square : Rectangle() {
            override var width: Int
                get() = super.width
                set(value) { super.width = value; super.height = value }
            override var height: Int
                get() = super.height
                set(value) { super.width = value; super.height = value }
        }

        // Written against Rectangle. Expects 5 * 4 = 20.
        fun resizeAndCheck(r: Rectangle): Boolean {
            r.width = 5
            r.height = 4
            return r.area() == 20 // Square returns 16 -> surprise!
        }
    }

    // ✅ Refactored: immutable shapes share only what is truly common (an area). No shape
    // pretends to support an operation it can't honor.
    object Good {
        interface Shape {
            fun area(): Int
        }

        data class Rectangle(val width: Int, val height: Int) : Shape {
            override fun area() = width * height
        }

        data class Square(val side: Int) : Shape {
            override fun area() = side * side
        }
    }

    fun demo() {
        println("Rectangle ok? ${Bad.resizeAndCheck(Bad.Rectangle())}") // true
        println("Square ok?    ${Bad.resizeAndCheck(Bad.Square())}")    // false -> LSP broken

        listOf<Good.Shape>(Good.Rectangle(5, 4), Good.Square(4)).forEach { println("Area: ${it.area()}") }
    }
}

// -----------------------------------------------------------------------------------------
// Example 2: birds
// -----------------------------------------------------------------------------------------
object LspBirdExample {

    // ❌ Violation: the base class promises every bird can fly; Penguin breaks that promise.
    object Bad {
        open class Bird {
            open fun fly(): String = "Flying"
        }

        class Sparrow : Bird()

        class Penguin : Bird() {
            override fun fly(): String = throw UnsupportedOperationException("Penguins can't fly")
        }

        fun migrate(birds: List<Bird>) = birds.forEach { println(it.fly()) } // crashes on Penguin
    }

    // ✅ Refactored: flying is a capability only some birds have. `migrate` asks for exactly
    // what it needs, so a Penguin can't even be passed to it.
    object Good {
        abstract class Bird {
            abstract fun eat(): String
        }

        interface FlyingBird {
            fun fly(): String
        }

        class Sparrow : Bird(), FlyingBird {
            override fun eat() = "Sparrow eats seeds"
            override fun fly() = "Sparrow flying"
        }

        class Penguin : Bird() {
            override fun eat() = "Penguin eats fish"
            fun swim() = "Penguin swimming"
        }

        fun feed(birds: List<Bird>) = birds.forEach { println(it.eat()) }
        fun migrate(birds: List<FlyingBird>) = birds.forEach { println(it.fly()) }

        fun demo() {
            val sparrow = Sparrow()
            val penguin = Penguin()
            feed(listOf(sparrow, penguin))
            migrate(listOf(sparrow)) // migrate(listOf(penguin)) doesn't compile — good!
            println(penguin.swim())
        }
    }
}

// -----------------------------------------------------------------------------------------
// Example 3 (bonus): read-only storage
// -----------------------------------------------------------------------------------------
object LspStorageExample {

    // ❌ Violation: a "read-only" subclass that throws on write. Any caller holding a
    // FileStorage now needs to know which concrete type it really has.
    object Bad {
        open class FileStorage {
            protected val files = mutableMapOf<String, String>()
            open fun read(name: String): String? = files[name]
            open fun write(name: String, content: String) { files[name] = content }
        }

        class ReadOnlyFileStorage : FileStorage() {
            override fun write(name: String, content: String) =
                throw UnsupportedOperationException("Read only!")
        }
    }

    // ✅ Refactored: split the contract. Read-only storage simply doesn't claim to write.
    object Good {
        interface ReadableStorage {
            fun read(name: String): String?
        }

        interface WritableStorage : ReadableStorage {
            fun write(name: String, content: String)
        }

        class ReadOnlyStorage(private val files: Map<String, String>) : ReadableStorage {
            override fun read(name: String) = files[name]
        }

        class InMemoryStorage : WritableStorage {
            private val files = mutableMapOf<String, String>()
            override fun read(name: String) = files[name]
            override fun write(name: String, content: String) { files[name] = content }
        }

        fun demo() {
            val storage: WritableStorage = InMemoryStorage()
            storage.write("notes.txt", "SOLID")
            val readers: List<ReadableStorage> = listOf(storage, ReadOnlyStorage(mapOf("a.txt" to "A")))
            readers.forEach { println(it.read("notes.txt") ?: it.read("a.txt")) }
        }
    }
}

fun liskovSubstitutionDemo() {
    LspRectangleExample.demo()
    LspBirdExample.Good.demo()
    LspStorageExample.Good.demo()
}
