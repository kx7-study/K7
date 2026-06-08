package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserProfile
import com.example.ui.theme.*
import com.example.viewmodel.BattleState
import com.example.viewmodel.MainViewModel
import java.util.Locale

@Composable
fun ArenaScreen(
    viewModel: MainViewModel,
    profile: UserProfile,
    modifier: Modifier = Modifier
) {
    val battleState by viewModel.battleState.collectAsState()

    var activeDuelSubject by remember { mutableStateOf("Physics") }

    val mockLeaderboard = remember {
        listOf(
            LeaderboardRank(1, "JEE_God_NCERT", "Elite 1%", 85600, true),
            LeaderboardRank(2, "MIT_Prestige_9", "Elite 1%", 72100, true),
            LeaderboardRank(3, "Dhaka_SSC_Topper", "Diamond", 34500, false),
            LeaderboardRank(4, "CambridgePhysic_God", "Diamond", 28900, false),
            LeaderboardRank(5, profile.displayName, profile.leagueTier, profile.auraPoints, profile.isPremium),
            LeaderboardRank(6, "CaltechHustler", "Platinum", 18800, false),
            LeaderboardRank(7, "OxfordWiz_99", "Platinum", 16400, false)
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBlack)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when (val state = battleState) {
            is BattleState.Idle -> {
                // ================== STATE: IDLE (Selection) ==================
                item {
                    Column {
                        Text(
                            text = "THE BATTLE ARENA",
                            style = MaterialTheme.typography.displayLarge,
                            color = NeonCyan,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Synchronous 1v1 academic duels. Stake aura points. Maximize cognitive output.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMutedGrey
                        )
                    }
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BorderCyberDark, RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = DeepSpaceSlate)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                text = "select champion subject".uppercase(Locale.getDefault()),
                                style = MaterialTheme.typography.labelMedium,
                                color = NeonCyan
                            )

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("Physics", "Mathematics", "Chemistry", "SAT Battle").forEach { sub ->
                                    val isSel = activeDuelSubject == sub
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { activeDuelSubject = sub }
                                            .background(if (isSel) NeonCyan.copy(alpha = 0.15f) else CyberBlack)
                                            .border(1.dp, if (isSel) NeonCyan else BorderCyberDark, RoundedCornerShape(8.dp))
                                            .padding(vertical = 12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = sub,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSel) NeonCyan else Color.White
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Button(
                                onClick = { viewModel.startBattleSearch(activeDuelSubject) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("initiate_battle_trigger"),
                                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.Black)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("QUEUE ONLINE MATRIX DUEL (STAKE 100 AP)", color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Global Leaderboard & Standings
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BorderCyberDark, RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = DeepSpaceSlate)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "global champion standings".uppercase(Locale.getDefault()),
                                style = MaterialTheme.typography.labelMedium,
                                color = NeonPurple
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            // Leaderboard Rows
                            mockLeaderboard.forEach { rank ->
                                val isSelf = rank.name == profile.displayName
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(if (isSelf) NeonCyan.copy(alpha = 0.1f) else Color.Transparent)
                                        .padding(vertical = 8.dp, horizontal = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            "Rank #${rank.pos}",
                                            fontSize = 12.sp,
                                            fontFamily = FontFamily.Monospace,
                                            color = if (rank.pos <= 3) NeonGreen else TextMutedGrey,
                                            modifier = Modifier.width(64.dp)
                                        )
                                        Text(
                                            rank.name,
                                            color = if (isSelf) NeonCyan else Color.White,
                                            fontWeight = if (isSelf) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 13.sp
                                        )
                                        if (rank.isPremium) {
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(Icons.Default.Favorite, contentDescription = "P", tint = NeonPurple, modifier = Modifier.size(12.dp))
                                        }
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(rank.tier, fontSize = 10.sp, color = NeonPurple)
                                        Text("${rank.points} AP", fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = NeonCyan)
                                    }
                                }
                                Divider(color = BorderCyberDark.copy(alpha = 0.5f))
                            }
                        }
                    }
                }

                // Dark Horse Warning alerts
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeonGreen.copy(alpha = 0.1f))
                            .border(1.dp, NeonGreen.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Build, contentDescription = "Trend", tint = NeonGreen, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "DARK HORSE DETECTOR: Candidate ${profile.displayName} velocity curves indicates Diamond Tier elevation within 48 hours. Keep current Master Streak active.",
                                fontSize = 11.sp,
                                color = NeonGreen
                            )
                        }
                    }
                }

                // Honors Circle
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BorderCyberDark, RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = DeepSpaceSlate)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = "Founder", tint = NeonPurple, modifier = Modifier.size(40.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("ELITE FOUNDERS CIRCLE BADGE STATUS", style = MaterialTheme.typography.labelMedium, color = NeonPurple)
                                Text("Eligibility: Streak \u2265 90, Tier: Elite 1%", fontSize = 11.sp, color = TextMutedGrey)
                                Text("Verified milestone requirements active. Structural scarcity locked.", fontSize = 10.sp, color = TextMutedGrey)
                            }
                        }
                    }
                }
            }

            is BattleState.Searching -> {
                // ================== STATE: SEARCHING ==================
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = NeonCyan, strokeWidth = 5.dp, modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "ESTABLISHING WEBSOCKET LAYER...",
                            style = MaterialTheme.typography.labelMedium,
                            color = NeonCyan,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Searching online nodes for active competitors. Locking parameters...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMutedGrey,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            is BattleState.Active -> {
                // ================== STATE: ACTIVE QUIZ ==================
                val currentQ = state.questionsList[state.currentQuestionIndex]

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // User Status
                        Column {
                            Text("YOU", style = MaterialTheme.typography.labelMedium, color = NeonCyan)
                            Text("${state.userScore} PTS", style = MaterialTheme.typography.headlineMedium, color = NeonCyan, fontWeight = FontWeight.Bold)
                        }

                        // Countdown Timer
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(WarningCrimson.copy(alpha = 0.1f))
                                .border(2.dp, WarningCrimson, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${state.secondsRemaining}s",
                                style = MaterialTheme.typography.titleLarge,
                                color = WarningCrimson,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Opponent Status
                        Column(horizontalAlignment = Alignment.End) {
                            Text(state.opponentName, style = MaterialTheme.typography.labelMedium, color = NeonPurple)
                            Text("${state.opponentScore} PTS", style = MaterialTheme.typography.headlineMedium, color = NeonPurple, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                item {
                    LinearProgressIndicator(
                        progress = { (state.currentQuestionIndex + 1).toFloat() / state.questionsList.size },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(3.dp)),
                        color = NeonCyan,
                        trackColor = BorderCyberDark
                    )
                }

                // Challenge question box
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BorderCyberDark, RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = DeepSpaceSlate)
                    ) {
                        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(NeonCyan)
                                        .size(24.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("${state.currentQuestionIndex + 1}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Duels Subject: ${state.subject}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = NeonGreen
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = currentQ.text,
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.testTag("battle_question_text")
                            )
                        }
                    }
                }

                // Answer Options Buttons list
                items(currentQ.choices.size) { index ->
                    val choice = currentQ.choices[index]
                    Button(
                        onClick = { viewModel.answerBattleQuestion(index) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BorderCyberDark, RoundedCornerShape(8.dp))
                            .testTag("battle_choice_$index"),
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceCardDark),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = choice,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }

            is BattleState.Completed -> {
                // ================== STATE: COMPLETED RESULTS ==================
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Success",
                            tint = if (state.winner == "Competitor Alpha") NeonGreen else WarningCrimson,
                            modifier = Modifier.size(80.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        val hasUserWon = state.winner == "Competitor Alpha"
                        Text(
                            text = if (hasUserWon) "SOLVENT DOMINANCE ESTABLISHED" else "OPPONENT SECURED AREA CONFLICT",
                            style = MaterialTheme.typography.headlineMedium,
                            color = if (hasUserWon) NeonGreen else WarningCrimson,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Winner: ${state.winner}",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Score breakdown
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(DeepSpaceSlate)
                                .border(1.dp, BorderCyberDark, RoundedCornerShape(12.dp))
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("YOUR SCORE", style = MaterialTheme.typography.labelMedium, color = TextMutedGrey)
                                Text("${state.userScoreFinal} PTS", style = MaterialTheme.typography.titleLarge, color = NeonCyan, fontWeight = FontWeight.Bold)
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("OPPONENT SCORE", style = MaterialTheme.typography.labelMedium, color = TextMutedGrey)
                                Text("${state.opponentScoreFinal} PTS", style = MaterialTheme.typography.titleLarge, color = NeonPurple, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Aura Points transferred
                        Text(
                            text = if (hasUserWon) "+${state.auraTransferred} AURA Gained" else "${state.auraTransferred} AURA Decayed",
                            style = MaterialTheme.typography.titleLarge,
                            color = if (hasUserWon) NeonGreen else WarningCrimson,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = { viewModel.dismissBattle() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("battle_dismiss_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                        ) {
                            Text("RETURN TO COMMAND BASE", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

data class LeaderboardRank(
    val pos: Int,
    val name: String,
    val tier: String,
    val points: Int,
    val isPremium: Boolean
)
