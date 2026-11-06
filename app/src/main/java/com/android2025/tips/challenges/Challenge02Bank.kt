package com.android2025.tips.challenges

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

/*
Daily Challenge: Bank Fraud Flagging (Kotlin, Medium)

Scenario
A retail bank’s fraud team reviews a stream of card transactions at the end of each day. They want a
simple rule-based checker that flags suspicious transactions and produces a per-account summary,
so analysts know where to look first.

Task
Implement:
➡️ fun flagTransactions(transactions: List<Transaction>): List<Flag>
✏️ Process transactions in timestamp order. The input may be unsorted.
✏️ Flag a transaction with the first rule that matches, in this priority order:
    ❌ LARGE_AMOUNT: amount >= 5,000.00
    RAPID_FIRE: it is the 4th or later transaction on the same account within any rolling 10-minute window.
        That means at least 3 earlier transactions on that account with timestamp in (t - 10, t].
    FOREIGN_JUMP: its country differs from the account’s previous transaction country and the time gap is
        under 60 minutes.
✏️ Declined transactions (approved == false) are never flagged. They also don’t count toward the rapid-fire
window or the “previous country”.
✏️ Return flags sorted by timestamp, then by transaction id.

➡️ fun summarize(flags: List<Flag>): Map<String, Int>
✏️ Return the number of flags per account id.
✏️ Include only accounts with at least one flag.

➡️ fun topRiskAccounts(summary: Map<String, Int>, limit: Int): List<Pair<String, Int>>
✏️ Sort by count descending, break ties by account id ascending, and keep only the first limit entries.

Timestamps are minutes since midnight (Int) to keep things simple.

Examples
Account A1 has transactions at minutes 100, 102, 104 and 106. All are approved, small and in the same country. The one at 106 is RAPID_FIRE, and the first three are not flagged.
Account A2 has one approved transaction of 5,000.00, which is LARGE_AMOUNT.
Edge: A3 has a declined 6,000.00 transaction, so it gets no flag.
Edge: A4 has a transaction in US at minute 200, then one in FR at minute 259, which is FOREIGN_JUMP. If the FR one were at minute 260 instead (a 60-minute gap), it would not be flagged.
Edge: a transaction that is both large and rapid-fire gets only LARGE_AMOUNT.

 */

enum class Reason { LARGE_AMOUNT, RAPID_FIRE, FOREIGN_JUMP }

data class Transaction(
    val id: String,
    val accountId: String,
    val minute: Int,          // minutes since midnight
    val amount: Double,
    val country: String,
    val approved: Boolean
)

data class Flag(val transactionId: String, val accountId: String, val minute: Int, val reason: Reason)

fun flagTransactions(transactions: List<Transaction>): List<Flag> {
    val flags = mutableListOf<Flag>()

    val groupedByAccountId: Map<String, List<Transaction>> = transactions
        .sortedBy { it.minute }
        .groupBy { it.accountId }

    for (group in groupedByAccountId) {
        val transactions = group.value
        var index = -1

        for (transaction in transactions) {
            index += 1
            if (!transaction.approved) {
                continue
            }

            if (transaction.amount >= 5000) {
                flags.add(
                    Flag(
                        transaction.id,
                        transaction.accountId,
                        transaction.minute,
                        Reason.LARGE_AMOUNT
                    )
                )
                continue
            }

            if (index >= 3) {
                val lastTransaction = transactions[index]
                val firstTransaction = transactions[index - 3]
                val timeDiff = lastTransaction.minute - firstTransaction.minute
                if (timeDiff < 10) {
                    flags.add(
                        Flag(
                            lastTransaction.id,
                            lastTransaction.accountId,
                            lastTransaction.minute,
                            Reason.RAPID_FIRE
                        )
                    )
                    continue
                }
            }

            if (index >= 1) {
                val currentTransaction = transactions[index]
                val lastTransaction = transactions[index - 1]
                val timeDiff = currentTransaction.minute - lastTransaction.minute
                if (timeDiff < 60 && lastTransaction.country != currentTransaction.country) {
                    flags.add(
                        Flag(
                            currentTransaction.id,
                            currentTransaction.accountId,
                            currentTransaction.minute,
                            Reason.FOREIGN_JUMP
                        )
                    )
                }
            }
        }
    }

    return flags
}

fun summarize(flags: List<Flag>): Map<String, Int> {
    val summary = mutableMapOf<String, Int>()
    for (flag in flags) {
        summary[flag.accountId] = summary.getOrDefault(flag.accountId, 0) + 1
    }
    return summary
}

fun topRiskAccounts(summary: Map<String, Int>, limit: Int): List<Pair<String, Int>> {

    val sortedSummary = summary.toList()
        .sortedWith(compareByDescending<Pair<String, Int>> { it.second }
            .thenBy { it.first }
        ).take(limit)

    return sortedSummary
}

fun main() {
    val txs = listOf(
        Transaction("T1", "A1", 100, 20.0, "US", true),
        Transaction("T2", "A1", 102, 15.0, "US", true),
        Transaction("T3", "A1", 104, 8.0, "US", true),
        Transaction("T4", "A1", 116, 30.0, "US", true),
        Transaction("T41", "A1", 118, 30.0, "US", true),
        Transaction("T42", "A1", 122, 30.0, "US", true),
        Transaction("T43", "A1", 123, 30.0, "US", true),
        Transaction("T5", "A2", 300, 5000.0, "US", true),
        Transaction("T6", "A3", 310, 6000.0, "US", false),
        Transaction("T7", "A4", 200, 40.0, "US", true),
        Transaction("T8", "A4", 259, 55.0, "FR", true),
        Transaction("T9", "A2", 310, 6500.0, "US", true),
    )
    val flags = flagTransactions(txs)
    flags.forEach(::println)
    // expected reasons: T4 RAPID_FIRE, T5 LARGE_AMOUNT, T8 FOREIGN_JUMP
    val summary = summarize(flags)
    println(summary)                       // {A1=1, A2=1, A4=1}
    println(topRiskAccounts(summary, 2))   // [(A1, 1), (A2, 1)]
}

@Composable
fun ChallengeBank(modifier: Modifier = Modifier) {
    main()
    Scaffold() {
        Text(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            text = "Challenge 2"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChallengePrev() {
    MaterialTheme {
        ChallengeBank()
    }
}