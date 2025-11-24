package com.syouth.dollarapp.ui.common

import androidx.compose.ui.unit.min

object EditDistance {


    sealed interface EditOperation {
        data class Insert(val char: Char, val index: Int) : EditOperation
        data class Delete(val char: Char, val index: Int) : EditOperation
        data class Replace(val oldChar: Char, val newChar: Char, val index: Int) : EditOperation
    }

    fun calculateEditOperations(source: String, target: String): List<EditOperation> {
        val m = source.length
        val n = target.length

        val dp = Array(m + 1) { IntArray(n + 1) }

        for (i in 0..m) dp[i][0] = i
        for (j in 0..n) dp[0][j] = j

        for (i in 1..m) {
            for (j in 1..n) {
                if (source[i - 1] == target[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1]
                } else {
                    dp[i][j] = 1 + minOf(
                        dp[i - 1][j],
                        minOf(
                            dp[i][j - 1],
                            dp[i - 1][j - 1]
                        )
                    )
                }
            }
        }

        val operations = mutableListOf<EditOperation>()
        var i = m
        var j = n

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && source[i - 1] == target[j - 1]) {
                i--
                j--
            } else {
                val replaceCost = if (i > 0 && j > 0) dp[i - 1][j - 1] else Int.MAX_VALUE
                val insertCost = if (j > 0) dp[i][j - 1] else Int.MAX_VALUE
                val deleteCost = if (i > 0) dp[i - 1][j] else Int.MAX_VALUE

                val minCost = minOf(replaceCost, minOf(insertCost, deleteCost))

                when (minCost) {
                    replaceCost -> {
                        operations.add(EditOperation.Replace(source[i - 1], target[j - 1], i - 1))
                        i--
                        j--
                    }
                    insertCost -> {
                        operations.add(EditOperation.Insert(target[j - 1], j - 1))
                        j--
                    }
                    deleteCost -> {
                        operations.add(EditOperation.Delete(source[i - 1], i - 1))
                        i--
                    }
                }
            }
        }

        return operations.reversed()
    }

}