package com.example.dailiestracker

data class TrackerSection(
    val id: String,                    // Unique ID for the section (perfect for dynamic lists)
    val title: String,                 // e.g., "Title 1", "Daily Tasks", "Weekly Objectives"
    val resourceItems: List<TrackerItem>,  // The grid items under the resources header
    val otherItems: List<TrackerItem>  // The grid items under the other header
)