package com.example.dailiestracker

data class TrackerItem(
    val id: Int,
    val title: String,
    var currentAmount: Int,
    val maxAmount: Int,
    val rechargeRateInSeconds: Long,
    var secondsRemaining: Long = 0L,
    var currentSecondsProgress: Long = 0L
) {
    val progressText: String
        get() = "$currentAmount/$maxAmount"

    val timerText: String
        get() {
            if (secondsRemaining <= 0 || currentAmount >= maxAmount) {
                return "Full"
            }
            val hours = secondsRemaining / 3600
            val minutes = (secondsRemaining % 3600) / 60
            return "${hours}h ${minutes}m Left"
        }
}


