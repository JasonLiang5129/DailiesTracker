package com.example.dailiestracker

object MockData {
    val sampleResourceItems = listOf(
        TrackerItem(
            1,
            "A",
            0,
            200,
            5L
        ),
        TrackerItem(
            2,
            "B",
            100,
            2400,
            180L
        ),
//        TrackerItem(
//            3,
//            "Currency",
//            200,
//            200,
//            120L
//        )
    )

    val sampleOtherItems = listOf(
        TrackerItem(
            4,
            "C",
            1,
            1,
            86400L
        ),
        TrackerItem(
            5,
            "D",
            0,
            1,
            86400L
        ),
    )

    val sampleSections = listOf(
        TrackerSection(
            id = "sec_1",
            title = "Title 1",
            resourceItems = sampleResourceItems,
            otherItems = sampleOtherItems
        ),
//        TrackerSection(
//            id = "sec_2",
//            title = "Title 2",
//            resourceItems = sampleResourceItems, // Reusing mock items for testing
//            otherItems = sampleOtherItems
//        )
    )
}