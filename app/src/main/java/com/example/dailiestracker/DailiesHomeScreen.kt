package com.example.dailiestracker

import android.R.attr.text
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailiestracker.ui.theme.DailiesTrackerTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Separated state lists for each category
            val sectionsState = remember {
                mutableStateListOf<TrackerSection>().apply {
                    addAll(MockData.sampleSections)
                }
            }

            LaunchedEffect(Unit) {
                while (true) {
                    delay(1000L) // Tick exactly every 1 second
                    sectionsState.forEachIndexed { sectionIndex, section ->

                        // Process resources with their own individual rates
                        val updatedResources = section.resourceItems.map { item ->
                            var newAmount = item.currentAmount
                            var newSeconds = item.secondsRemaining // Use the existing value
                            var newItemProgress = item.currentSecondsProgress

                            if (newAmount < item.maxAmount) {
                                // Simply subtract 1 second if there is time left
                                if (newSeconds > 0) {
                                    newSeconds--
                                }
                                newItemProgress++

                                // Handle Resource Gain
                                if (newItemProgress >= item.rechargeRateInSeconds) {
                                    newAmount++
                                    newSeconds = (item.maxAmount - newAmount) * item.rechargeRateInSeconds
                                    newItemProgress = 0L
                                }
                            } else {
                                newSeconds = 0L // Ensure it says 0 when full
                            }

                            item.copy(
                                currentAmount = newAmount,
                                secondsRemaining = newSeconds,
                                currentSecondsProgress = newItemProgress
                            )
                        }

                        // Process other items with the exact same personalized rate logic
                        val updatedOtherItems = section.otherItems.map { item ->
                            var newAmount = item.currentAmount
                            var newSeconds = item.secondsRemaining
                            var newItemProgress = item.currentSecondsProgress

                            if (newAmount < item.maxAmount) {
                                if (newSeconds > 0) newSeconds--
                                newItemProgress++

                                if (newItemProgress >= item.rechargeRateInSeconds) {
                                    newAmount++
                                    newItemProgress = 0L
                                }
                            }

                            item.copy(
                                currentAmount = newAmount,
                                secondsRemaining = newSeconds,
                                currentSecondsProgress = newItemProgress
                            )
                        }

                        sectionsState[sectionIndex] = section.copy(
                            resourceItems = updatedResources,
                            otherItems = updatedOtherItems
                        )
                    }
                }
            }

            DailiesTrackerTheme {
                MainLayoutScreen(
                    sections = sectionsState,
                    onUpdateItem = { clickedItem, newValue ->
                        // Find which section contains the item by checking IDs
                        val index = sectionsState.indexOfFirst { section ->
                            section.resourceItems.any { it.id == clickedItem.id } || section.otherItems.any { it.id == clickedItem.id }
                        }

                        if (index != -1) {
                            val section = sectionsState[index]

                            // Create the updated copies using ID matching
                            val updatedResources = section.resourceItems.map {
                                if (it.id == clickedItem.id) {
                                    // reset the local progress ticker
                                    it.copy(
                                        currentAmount = newValue,
                                        currentSecondsProgress = 0L,
                                        secondsRemaining = (clickedItem.maxAmount - newValue) * clickedItem.rechargeRateInSeconds)
                                } else {
                                    it
                                }
                            }
                            val updatedOthers = section.otherItems.map {
                                if (it.id == clickedItem.id) {
                                    it.copy(
                                        currentAmount = newValue,
                                        currentSecondsProgress = 0L,
                                        secondsRemaining = (clickedItem.maxAmount - newValue) * clickedItem.rechargeRateInSeconds
                                    )
                                } else {
                                    it
                                }
                            }

                            // Replace the section in the state list
                            sectionsState[index] = section.copy(
                                resourceItems = updatedResources,
                                otherItems = updatedOthers
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MainLayoutScreen(
    sections: List<TrackerSection>,
    onUpdateItem: (TrackerItem, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        TopBar()
        DashboardScreen(
            sections = sections,
            onUpdateItem = onUpdateItem
        )
    }
}

@Composable
fun TopBar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Dailies Tracker",
            fontSize = 48.sp,
            color = Color.DarkGray,
        )
    }
}

@Composable
fun MainTitleHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
    )
}

@Composable
fun DashboardScreen(
    sections: List<TrackerSection>,
    onUpdateItem: (TrackerItem, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedItem by remember {
        mutableStateOf<TrackerItem?>(null)
    }

    var resetValue by remember { mutableStateOf("") }

    if (selectedItem != null) {
        AlertDialog(
            onDismissRequest = {
                selectedItem = null
            },
            title = { Text(text = "Reset ${selectedItem?.title}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Enter a new value (0 - ${selectedItem?.maxAmount}):")

                    androidx.compose.material3.TextField(
                        value = resetValue,
                        onValueChange = { newValue ->
                            // Only allow numbers to be typed
                            if (newValue.all { it.isDigit() }) {
                                resetValue = newValue
                            }
                        },
                        placeholder = { Text("e.g. 50") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                        ),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Quick Reset Button
                    androidx.compose.material3.TextButton(onClick = {
                        onUpdateItem(selectedItem!!, 0)
                        selectedItem = null
                        resetValue = ""
                    }) {
                        Text("Reset to 0")
                    }

                    // Save Button
                    Button(onClick = {
                        val newValue = resetValue.toIntOrNull()
                        if (newValue != null && selectedItem != null && newValue in 0..selectedItem!!.maxAmount) {
                            onUpdateItem(selectedItem!!, newValue)
                            selectedItem = null
                            resetValue = ""
                        }
                    }) {
                        Text("Save")
                    }
                }
            },
            dismissButton = {
                Button(onClick = { selectedItem = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        sections.forEach { section ->
            // Main Title Header
            item(
                key = "title_${section.id}",
                span = { GridItemSpan(maxLineSpan) }
            ) {
                MainTitleHeader(title = section.title)
            }

            // Resources Sub-Header
            item(
                key = "res_header_${section.id}",
                span = { GridItemSpan(maxLineSpan) }
            ) {
                CategoryHeader(title = "resources")
            }

            // Grid items for this section's resources
            items(
                items = section.resourceItems,
                key = { item -> "res_${section.id}_${item.id}" } // Keeps keys unique globally
            ) { item ->
                GridCardItem(
                    item = item,
                    onItemClick = {
                        selectedItem = item
                    }
                )
            }

            // Other Sub-Header
            item(
                key = "oth_header_${section.id}",
                span = { GridItemSpan(maxLineSpan) }
            ) {
                CategoryHeader(title = "other")
            }

            // Grid items for this section's other items
            items(
                items = section.otherItems,
                key = { item -> "oth_${section.id}_${item.id}" }
            ) { item ->
                GridCardItem(
                    item = item,
                    onItemClick = {
                        selectedItem = item
                    }
                )
            }
        }
    }
}

@Composable
fun CategoryHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}


@Composable
fun GridCardItem(
    item: TrackerItem,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable { onItemClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = item.progressText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = item.timerText,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(
    name = "Google Pixel 7",
    showSystemUi = true,
    device = Devices.PIXEL_7,
    showBackground = true
)
@Composable
fun DailiesTrackerPreview() {
    DailiesTrackerTheme {
        MainLayoutScreen(
            sections = MockData.sampleSections,
            onUpdateItem = { _, _ ->
                // Do nothing in preview mode
            }
        )
    }
}
