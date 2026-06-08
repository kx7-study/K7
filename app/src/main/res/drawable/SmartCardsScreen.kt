package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.WorldCard
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel
import java.util.Locale

@Composable
fun SmartCardsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val worldCards by viewModel.worldCards.collectAsState()
    val smartLoading by viewModel.smartIngestionLoading.collectAsState()
    val smartResult by viewModel.smartIngestionResult.collectAsState()

    var activeTabState by remember { mutableStateOf(0) } // 0: World Cards Deck, 1: Smart Ingestion
    var selectedInjestSubject by remember { mutableStateOf("Physics") }
    var rawSourceTextInput by remember { mutableStateOf("") }

    // Manual Card creation States
    var showManualCreator by remember { mutableStateOf(false) }
    var customFront by remember { mutableStateOf("") }
    var customBack by remember { mutableStateOf("") }
    var customSubject by remember { mutableStateOf("Physics") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBlack)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Tab Selection Matrix ---
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderCyberDark, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .background(DeepSpaceSlate)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { activeTabState = 0 }
                        .background(if (activeTabState == 0) NeonCyan.copy(alpha = 0.15f) else Color.Transparent)
                        .border(if (activeTabState == 0) 1.dp else 0.dp, NeonCyan)
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "WORLD MEMORY CARDS",
                        fontWeight = FontWeight.Bold,
                        color = if (activeTabState == 0) NeonCyan else TextMutedGrey,
                        fontSize = 11.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { activeTabState = 1 }
                        .background(if (activeTabState == 1) NeonPurple.copy(alpha = 0.15f) else Color.Transparent)
                        .border(if (activeTabState == 1) 1.dp else 0.dp, NeonPurple)
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "SMART CONTENT ENGINE",
                        fontWeight = FontWeight.Bold,
                        color = if (activeTabState == 1) NeonPurple else TextMutedGrey,
                        fontSize = 11.sp
                    )
                }
            }
        }

        if (activeTabState ==  0) {
            // ================== TAB 0: WORLD MEMORY CARDS ==================

            // Manual Card Creator overlay toggle
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "active recall spaced flashcards".uppercase(Locale.getDefault()),
                        style = MaterialTheme.typography.labelMedium,
                        color = NeonCyan
                    )

                    Button(
                        onClick = { showManualCreator = !showManualCreator },
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceCardDark),
                        modifier = Modifier.testTag("add_card_toggle")
                    ) {
                        Icon(
                            imageVector = if (showManualCreator) Icons.Default.Clear else Icons.Default.Add,
                            contentDescription = "Toggle",
                            tint = NeonCyan
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (showManualCreator) "CANCEL" else "CREATE CARD", color = Color.White, fontSize = 11.sp)
                    }
                }
            }

            if (showManualCreator) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BorderCyberDark, RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = DeepSpaceSlate)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("NEW WORLD CARD CONFIGURATION", style = MaterialTheme.typography.titleLarge, color = TextWhite)

                            OutlinedTextField(
                                value = customFront,
                                onValueChange = { customFront = it },
                                label = { Text("Question (Front)", color = TextMutedGrey) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonCyan)
                            )

                            OutlinedTextField(
                                value = customBack,
                                onValueChange = { customBack = it },
                                label = { Text("Answer (Back)", color = TextMutedGrey) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonCyan)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = {
                                        if (customFront.isNotEmpty() && customBack.isNotEmpty()) {
                                            viewModel.addWorldCard(customFront, customBack, customSubject)
                                            customFront = ""
                                            customBack = ""
                                            showManualCreator = false
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                                ) {
                                    Text("INJECT WORLD CARD", color = Color.Black, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // Interactive Revision game desk
            item {
                if (worldCards.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(DeepSpaceSlate)
                            .border(1.dp, BorderCyberDark, RoundedCornerShape(12.dp))
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Build, contentDescription = "Empty", tint = TextMutedGrey, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "No memory cards configured.",
                                style = MaterialTheme.typography.titleLarge,
                                color = TextMutedGrey
                            )
                            Text(
                                "Create manually above or feed the Smart Content Engine to synthesize auto decks.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextMutedGrey,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    var currentIndex by remember { mutableStateOf(0) }
                    var isAnswerExposed by remember { mutableStateOf(false) }

                    val activeIndex = currentIndex.coerceIn(0, worldCards.size - 1)
                    val card = worldCards[activeIndex]

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BorderCyberDark, RoundedCornerShape(12.dp))
                            .clip(RoundedCornerShape(12.dp))
                            .background(DeepSpaceSlate)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NeonCyan.copy(alpha = 0.1f))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(card.subject.uppercase(Locale.getDefault()), color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 9.sp)
                            }

                            Text(
                                text = "Card ${activeIndex + 1} of ${worldCards.size}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextMutedGrey
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Front of Card Box
                        Text(
                            text = card.front,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.testTag("flashcard_question_text")
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Exposed answer panel
                        AnimatedVisibility(visible = isAnswerExposed) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CyberBlack)
                                    .border(1.dp, BorderCyberDark, RoundedCornerShape(8.dp))
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "EXPOSED VERIFICATION RESPONSE",
                                    fontSize = 10.sp,
                                    color = NeonPurple,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = card.back,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = TextWhite,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.testTag("flashcard_answer_text")
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Controls
                        if (!isAnswerExposed) {
                            Button(
                                onClick = { isAnswerExposed = true },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("expose_back_button")
                            ) {
                                Text("EXPOSE ANSWER PROTOCOL", color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Decay option (Wrong recall)
                                Button(
                                    onClick = {
                                        viewModel.reviewWorldCard(card, false)
                                        isAnswerExposed = false
                                        if (currentIndex < worldCards.size - 1) {
                                            currentIndex += 1
                                        } else {
                                            currentIndex = 0
                                        }
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("verify_failed_recall"),
                                    colors = ButtonDefaults.buttonColors(containerColor = WarningCrimson)
                                ) {
                                    Text("DECAY (Wrong)")
                                }

                                // Strengthen option (Correct recall)
                                Button(
                                    onClick = {
                                        viewModel.reviewWorldCard(card, true)
                                        isAnswerExposed = false
                                        if (currentIndex < worldCards.size - 1) {
                                            currentIndex += 1
                                        } else {
                                            currentIndex = 0
                                        }
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("verify_secure_recall"),
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
                                ) {
                                    Text("SECURE RECALL", color = Color.Black, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Retention indicator
                        LinearProgressIndicator(
                            progress = { card.retentionStrength },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (card.retentionStrength > 0.6f) NeonGreen else if (card.retentionStrength > 0.3f) NeonCyan else WarningCrimson,
                            trackColor = BorderCyberDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Memory strength index: ${(card.retentionStrength * 100).toInt()}% strength",
                            fontSize = 10.sp,
                            color = TextMutedGrey
                        )
                    }
                }
            }

        } else {
            // ================== TAB 1: SMART CONTENT INGESTION ==================

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderCyberDark, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = DeepSpaceSlate)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "multimodal asset summarizer".uppercase(Locale.getDefault()),
                            style = MaterialTheme.typography.labelMedium,
                            color = NeonPurple
                        )
                        Text(
                            "Paste long text documents, video lecture notes, or complex formulations. Our smart AI structures key summaries and auto-injects World Flashcards.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMutedGrey,
                            fontSize = 11.sp
                        )

                        // Selector
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Physics", "Chemistry", "Math", "Biology").forEach { sub ->
                                val isSel = selectedInjestSubject == sub
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedInjestSubject = sub }
                                    .background(if (isSel) NeonPurple.copy(alpha = 0.2f) else CyberBlack)
                                    .border(1.dp, if (isSel) NeonPurple else BorderCyberDark, RoundedCornerShape(6.dp))
                                    .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(sub, fontSize = 10.sp, color = if (isSel) NeonCyan else TextMutedGrey, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        OutlinedTextField(
                            value = rawSourceTextInput,
                            onValueChange = { rawSourceTextInput = it },
                            placeholder = { Text("Paste lecture links, document paragraphs, formulas, or raw text logs...", fontSize = 12.sp, color = TextMutedGrey) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .testTag("smart_summary_input"),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonPurple, focusedContainerColor = CyberBlack, unfocusedContainerColor = CyberBlack)
                        )

                        Button(
                            onClick = {
                                if (rawSourceTextInput.isNotEmpty()) {
                                    viewModel.ingestSmartContent(rawSourceTextInput, selectedInjestSubject)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("summary_trigger_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                            enabled = rawSourceTextInput.isNotEmpty() && !smartLoading
                        ) {
                            if (smartLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                            } else {
                                Text("INGEST & GENERATE CARD DECKS", fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }

            // Output Display
            item {
                AnimatedVisibility(visible = smartResult != null || smartLoading) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, NeonPurple, RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = CyberBlack)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("GENERATED RECALL VECTORS", style = MaterialTheme.typography.labelMedium, color = NeonGreen)
                            Divider(modifier = Modifier.padding(vertical = 8.dp), color = BorderCyberDark)

                            if (smartLoading) {
                                CircularProgressIndicator(color = NeonPurple)
                            } else {
                                Text(
                                    text = smartResult ?: "Success! Material processed. System updated.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextWhite
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(NeonGreen.copy(alpha = 0.1f))
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        "ALGORITHMIC RESTRUCTURED SECURE: Flashcards auto-loaded to your deck. Return to World Memory tab above to review them.",
                                        fontSize = 11.sp,
                                        color = NeonGreen
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
