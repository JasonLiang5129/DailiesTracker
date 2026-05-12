package com.example.dailiestracker

object MockData {
    val sampleResourceItems = listOf(
        TrackerItem(1, "Resin", "66/200", "17h 5m Left"),
        TrackerItem(2, "Realm Currency", "1000/2400", "17h 5m Left"),
        TrackerItem(3, "Currency", "200/200", "Full")
        )

    val sampleOtherItems = listOf(
        TrackerItem(4, "Investigation", "Not Done", "Reset: 17h 5m"),
        TrackerItem(5, "Check-In", "Done", "Reset: 17h 5m"),
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