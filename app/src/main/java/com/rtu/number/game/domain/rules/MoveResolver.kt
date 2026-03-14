package com.rtu.number.game.domain.rules

import com.rtu.number.game.domain.model.MoveResolution
import com.rtu.number.game.domain.model.PlayerId
import com.rtu.number.game.domain.model.ReplacementRule
import com.rtu.number.game.domain.model.ScoreChange
import javax.inject.Inject

interface MoveResolver {
    fun resolve(
        leftValue: Int,
        rightValue: Int,
        currentPlayer: PlayerId,
    ): MoveResolution
}

class DefaultMoveResolver @Inject constructor() : MoveResolver {

    override fun resolve(
        leftValue: Int,
        rightValue: Int,
        currentPlayer: PlayerId,
    ): MoveResolution {
        val sum = leftValue + rightValue

        return when {
            sum > 7 -> MoveResolution(
                replacementValue = 1,
                appliedRule = ReplacementRule.SUM_GREATER_THAN_SEVEN,
                scoreChange = scoreChangeForGreaterThanSeven(currentPlayer),
            )

            sum < 7 -> MoveResolution(
                replacementValue = 3,
                appliedRule = ReplacementRule.SUM_LESS_THAN_SEVEN,
                scoreChange = scoreChangeForLessThanSeven(currentPlayer),
            )

            else -> MoveResolution(
                replacementValue = 2,
                appliedRule = ReplacementRule.SUM_EQUALS_SEVEN,
                scoreChange = ScoreChange(
                    firstPlayerDelta = 1,
                    secondPlayerDelta = 1,
                ),
            )
        }
    }

    private fun scoreChangeForGreaterThanSeven(currentPlayer: PlayerId): ScoreChange {
        return if (currentPlayer == PlayerId.FIRST) {
            ScoreChange(firstPlayerDelta = 1)
        } else {
            ScoreChange(secondPlayerDelta = 1)
        }
    }

    private fun scoreChangeForLessThanSeven(currentPlayer: PlayerId): ScoreChange {
        return if (currentPlayer == PlayerId.FIRST) {
            ScoreChange(secondPlayerDelta = -1)
        } else {
            ScoreChange(firstPlayerDelta = -1)
        }
    }
}
