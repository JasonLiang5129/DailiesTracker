package com.example.dailiestracker

object MockData {
    val sampleResourceItems = listOf(
        TrackerItem(1, "Resin", 66, 200, 61500L),
        TrackerItem(2, "Realm Currency", 100, 2400, 61500L),
        TrackerItem(3, "Currency", 200, 200, 0)
        )

    val sampleOtherItems = listOf(
        TrackerItem(4, "Investigation", 1, 1, 61500L),
        TrackerItem(5, "Check-In", 0, 1, 61500L),
    )

    val sampleSections = listOf(
        TrackerSection(
            id = "sec_1",
            title = "Title 1",
            resourceItems = sampleResourceItems,
            otherItems = sampleOtherItems
        ),
        TrackerSection(
            id = "sec_2",
            title = "Title 2",
            resourceItems = sampleResourceItems, // Reusing mock items for testing
            otherItems = sampleOtherItems
        )
    )
}