package com.example.dailiestracker

object MockData {
    val sampleResourceItems = listOf(
        TrackerItem(
            1,
            "Resin",
            0,
            200,
            5L
        ),
        TrackerItem(
            2,
            "Realm Currency",
            100,
            2400,
            180L
        ),
        TrackerItem(
            3,
            "Currency",
            200,
            200,
            120L
        )
    )

    val sampleOtherItems = listOf(
        TrackerItem(
            4,
            "Investigation",
            1,
            1,
            60L
        ),
        TrackerItem(
            5,
            "Check-In",
            0,
            1,
            120L
        ),
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