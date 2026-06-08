package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.viewmodel.CheckoutStatus
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.MockExamState
import java.util.Locale

data class PremiumPlan(
    val name: String,
    val usdPrice: String,
    val bdtPrice: String,
    val description: String
)

@Composable
fun BlackOpsScreen(
    viewModel: MainViewModel,
    profile: UserProfile,
    modifier: Modifier = Modifier
) {
    val mockExamState by viewModel.mockExamState.collectAsState()
    val checkoutStatus by viewModel.premiumProcessingStatus.collectAsState()

    var activePanelMode by remember { mutableStateOf(0) } // 0: Mocks & Challenges, 1: Premium Shop

    var selectedPlanIndex by remember { mutableStateOf(0) }
    var selectedCryptoChain by remember { mutableStateOf("TRC20") }
    var userSubmittedTxHash by remember { mutableStateOf("") }

    val cryptoAddresses = mapOf(
        "TRC20" to "THrBL9ZvjnEH981PPJRG9gQMoyz2XAqqR3",
        "ERC20" to "0x86b09ecf2e35a74a5c1a06e126f417277f9723f8",
        "BEP20" to "0x86b09ecf2e35a74a5c1a06e126f417277f9723f8",
        "bKash" to "01922934076"
    )

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
                        .clickable { activePanelMode = 0 }
                        .background(if (activePanelMode == 0) NeonCyan.copy(alpha = 0.15f) else Color.Transparent)
                        .border(if (activePanelMode == 0) 1.dp else 0.dp, NeonCyan)
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "DISCIPLINE LOGS",
                        fontWeight = FontWeight.Bold,
                        color = if (activePanelMode == 0) NeonCyan else TextMutedGrey,
                        fontSize = 11.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { activePanelMode = 1 }
                        .background(if (activePanelMode == 1) NeonPurple.copy(alpha = 0.15f) else Color.Transparent)
                        .border(if (activePanelMode == 1) 1.dp else 0.dp, NeonPurple)
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "PREMIUM SECURE SHOP",
                        fontWeight = FontWeight.Bold,
                        color = if (activePanelMode == 1) NeonPurple else TextMutedGrey,
                        fontSize = 11.sp
                    )
                }
            }
        }

        if (activePanelMode == 0) {
            // ================== DISCIPLINE LOGS (Mocks & Decay recovery) ==================

            item {
                Column {
                    Text(
                        text = "THE BLACK-OPS ACCURACY CENTRE",
                        style = MaterialTheme.typography.displayLarge,
                        color = NeonCyan,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Weaponized stress calibration engines. Countering systemic memory decay.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMutedGrey
                    )
                }
            }

            // Mock exam layout switcher
            item {
                when (val exam = mockExamState) {
                    null -> {
                        // Launch button state
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, BorderCyberDark, RoundedCornerShape(12.dp)),
                            colors = CardDefaults.cardColors(containerColor = DeepSpaceSlate)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(
                                    text = "adversarial mock synthesis engine".uppercase(Locale.getDefault()),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = NeonCyan
                                )
                                Text(
                                    text = "Analyzes historical mistake logs inside room databases, dynamically synthesizing mock frameworks matching your exact cognitive weak spot coordinates.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextMutedGrey,
                                    fontSize = 11.sp
                                )

                                Button(
                                    onClick = { viewModel.generateAdversarialMock("Physics_FluidMechanics") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("synthesis_mock_btn"),
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = "Synth", tint = Color.Black)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("COMPILE ADVERSARIAL BLUEPRINT", color = Color.Black, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    is MockExamState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .background(CyberBlack)
                                .border(1.dp, BorderCyberDark, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = NeonCyan)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("SYNTHESIZING HOSTILE OUTCOMES...", color = NeonCyan, style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }

                    is MockExamState.Active -> {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, WarningCrimson, RoundedCornerShape(12.dp)),
                            colors = CardDefaults.cardColors(containerColor = CyberBlack)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text(exam.title, style = MaterialTheme.typography.titleLarge, color = WarningCrimson, fontWeight = FontWeight.Bold)
                                Text("Complete with rigorous calculations. Mistakes degrade Aura rankings.", fontSize = 11.sp, color = TextMutedGrey)

                                exam.questions.forEachIndexed { qIndex, question ->
                                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                        Text("${qIndex + 1}. $question", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                        Spacer(modifier = Modifier.height(6.dp))
                                        OutlinedTextField(
                                            value = exam.answers[qIndex] ?: "",
                                            onValueChange = { newValue ->
                                                exam.answers[qIndex] = newValue
                                                // Trigger state mutation inside UI
                                                viewModel.generateAdversarialMock("Physics_FluidMechanics")
                                                viewModel.submitMockAnswers(exam.answers) // temporary bypass
                                            },
                                            placeholder = { Text("Write derivation steps...", fontSize = 11.sp, color = TextMutedGrey) },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .testTag("mock_answer_$qIndex"),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = NeonCyan,
                                                unfocusedBorderColor = BorderCyberDark,
                                                focusedContainerColor = DeepSpaceSlate,
                                                unfocusedContainerColor = DeepSpaceSlate
                                            ),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = { viewModel.exitMockExam() },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceCardDark)
                                    ) {
                                        Text("ABORT SESSION")
                                    }

                                    Button(
                                        onClick = { viewModel.submitMockAnswers(exam.answers) },
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("submit_mock_trigger"),
                                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                                    ) {
                                        Text("TRANSMIT ANSWERS", color = Color.Black, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    is MockExamState.Completed -> {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, NeonGreen, RoundedCornerShape(12.dp)),
                            colors = CardDefaults.cardColors(containerColor = DeepSpaceSlate)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Check, contentDescription = "Graded", tint = NeonGreen, modifier = Modifier.size(48.dp))
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("EVALUATION COMPLETION RECEIPT", style = MaterialTheme.typography.labelMedium, color = NeonGreen)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${exam.markedScorePercent}% Academic Mark Score",
                                    style = MaterialTheme.typography.displayLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = exam.criticalReview,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextWhite,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "+${exam.gainedAura} AP rewarded directly to portfolio standings.",
                                    fontSize = 11.sp,
                                    color = NeonGreen,
                                    fontFamily = FontFamily.Monospace
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = { viewModel.exitMockExam() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("mock_completed_return"),
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                                ) {
                                    Text("SECURE REVIEW RECEIPTS", color = Color.Black, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // Recovery Streak Decay protocol
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, WarningCrimson, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = CyberBlack)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = "Loss Warning", tint = WarningCrimson, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "STREAK RECOVERY SECTOR (CRITICAL ACTIVE)",
                                style = MaterialTheme.typography.labelMedium,
                                color = WarningCrimson
                            )
                        }

                        Text(
                            text = "A student misses a daily goal, causing immediate league standings decay threats. Accomplish these critical challenge activities within 48 hours to preserve streak and AP portfolios completely.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMutedGrey,
                            fontSize = 11.sp
                        )

                        // Challenge targets Checklist
                        val listChallenges = listOf(
                            "Solve 1 Physics fluid velocity derivation under Fischer mode",
                            "Review 3 World Memory Cards secure recall rounds successfully",
                            "Commit 1 smart lecture file summary to the Second Brain database"
                        )

                        listChallenges.forEach { challenge ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(DeepSpaceSlate)
                                    .padding(12.dp)
                                    .border(1.dp, BorderCyberDark, RoundedCornerShape(6.dp))
                                    .clickable {
                                        viewModel.earnAuraPoints(50)
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .border(1.dp, NeonCyan, RoundedCornerShape(4.dp))
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(challenge, fontSize = 11.sp, color = Color.White)
                            }
                        }
                    }
                }
            }

        } else {
            // ================== NO RESTRICTIONS COGNITIVE SHOP ==================

            item {
                Column {
                    Text(
                        text = "SECURE CROSS-CHAIN ENGINE",
                        style = MaterialTheme.typography.displayLarge,
                        color = NeonPurple,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Bypass border constraints. Unleash advanced model derivations securely.",
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
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "PREMIUM UTILITIES EXPLAINED",
                            style = MaterialTheme.typography.labelMedium,
                            color = NeonCyan
                        )

                        Text("\u25c6 Deep Predictive analytics trajectories & LSI curves.", fontSize = 12.sp, color = TextWhite)
                        Text("\u25c6 Unlimited Camera Scan-To-Solve boundary checking.", fontSize = 12.sp, color = TextWhite)
                        Text("\u25c6 Advanced multi-variable proofs & higher derivations.", fontSize = 12.sp, color = TextWhite)
                        Text("\u25c6 Automated Adversarial blueprint test synthesis.", fontSize = 12.sp, color = TextWhite)
                    }
                }
            }

            item {
                Text(
                    text = "SELECT RE-ALIGNMENT POWER TIER",
                    style = MaterialTheme.typography.labelMedium,
                    color = NeonPurple,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    val plans = listOf(
                        PremiumPlan("1 Month Access", "$27", "3700 BDT", "Perfect for rapid calibration"),
                        PremiumPlan("3 Months Access", "$77", "10,500 BDT", "Most popular choice for intense training"),
                        PremiumPlan("1 Year Infinity", "$277", "37,800 BDT", "Ultimate long-range learning index")
                    )
                    plans.forEachIndexed { index, plan ->
                        val isSelected = selectedPlanIndex == index
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) NeonPurple else BorderCyberDark,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedPlanIndex = index },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) DeepSpaceSlate else CyberBlack
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = plan.name,
                                            style = MaterialTheme.typography.titleLarge,
                                            color = if (isSelected) NeonPurple else TextWhite,
                                            fontWeight = FontWeight.Bold
                                        )
                                        if (index == 1) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Box(
                                                modifier = Modifier
                                                    .background(NeonCyan.copy(alpha = 0.2f))
                                                    .border(1.dp, NeonCyan, RoundedCornerShape(4.dp))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text("POPULAR", fontSize = 8.sp, color = NeonCyan, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = plan.description,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextMutedGrey
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = plan.usdPrice,
                                        style = MaterialTheme.typography.titleLarge,
                                        color = NeonGreen,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Text(
                                        text = plan.bdtPrice,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = NeonCyan,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Payment dispatcher desk
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, NeonPurple, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = DeepSpaceSlate)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        val activePlan = listOf(
                            PremiumPlan("1 Month Access", "$27", "3700 BDT", "Perfect for rapid calibration"),
                            PremiumPlan("3 Months Access", "$77", "10,500 BDT", "Most popular choice for intense training"),
                            PremiumPlan("1 Year Infinity", "$277", "37,800 BDT", "Ultimate long-range learning index")
                        )[selectedPlanIndex]

                        Text(
                            text = if (selectedCryptoChain == "bKash") "SECURE bKASH PAYOUT PROTOCOL" else "cross-chain cryptographic checkout".uppercase(Locale.getDefault()),
                            style = MaterialTheme.typography.labelMedium,
                            color = NeonPurple
                        )

                        Text(
                            text = "Selected Package: ${activePlan.name} — Transfer ${if (selectedCryptoChain == "bKash") activePlan.bdtPrice else activePlan.usdPrice} to verify.",
                            color = NeonGreen,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("TRC20", "ERC20", "BEP20", "bKash").forEach { chain ->
                                val isSelected = selectedCryptoChain == chain
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedCryptoChain = chain }
                                        .background(if (isSelected) NeonPurple.copy(alpha = 0.2f) else CyberBlack)
                                        .border(1.dp, if (isSelected) NeonPurple else BorderCyberDark, RoundedCornerShape(8.dp))
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(chain, color = if (isSelected) NeonCyan else TextMutedGrey, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                            }
                        }

                        // Address readouts
                        val targetAddress = cryptoAddresses[selectedCryptoChain] ?: ""
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CyberBlack)
                                .border(1.dp, BorderCyberDark, RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            val headerLabel = if (selectedCryptoChain == "bKash") {
                                "bKASH PERSONAL NUMBER (SEND MONEY ONLY)"
                            } else {
                                "DEPOSIT ADDRESS FOR $selectedCryptoChain"
                            }
                            Text(headerLabel, fontSize = 10.sp, color = if (selectedCryptoChain == "bKash") WarningCrimson else TextMutedGrey, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = targetAddress,
                                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
                                color = if (selectedCryptoChain == "bKash") NeonCyan else NeonGreen,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // hash entry fields
                        OutlinedTextField(
                            value = userSubmittedTxHash,
                            onValueChange = { userSubmittedTxHash = it },
                            placeholder = {
                                val placeholderText = if (selectedCryptoChain == "bKash") {
                                    "Enter bKash 10-char TrxID (Transaction ID)..."
                                } else {
                                    "Paste cryptographic TX Hash receipt hashes..."
                                }
                                Text(placeholderText, fontSize = 12.sp)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("premium_tx_hash_field"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonPurple,
                                focusedContainerColor = CyberBlack,
                                unfocusedContainerColor = CyberBlack
                            )
                        )

                        Button(
                            onClick = {
                                if (userSubmittedTxHash.isNotEmpty()) {
                                    viewModel.triggerPremiumCryptoySubscription(selectedCryptoChain, userSubmittedTxHash)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("verify_subscription_trigger"),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                            enabled = userSubmittedTxHash.isNotEmpty() && checkoutStatus !is CheckoutStatus.VerifyingHash
                        ) {
                            val buttonText = if (selectedCryptoChain == "bKash") {
                                "ACTIVATE ${activePlan.name.uppercase(Locale.getDefault())} VIA bKASH"
                            } else {
                                "VERIFY ON-CHAIN PAYOUT FOR ${activePlan.name.uppercase(Locale.getDefault())}"
                            }
                            Text(buttonText, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Checkout state display
            item {
                AnimatedVisibility(visible = checkoutStatus !is CheckoutStatus.Idle) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, NeonPurple, RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = CyberBlack)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val receiptHeader = if (selectedCryptoChain == "bKash") "bKASH SECURE GATEWAY RECEIPT STATUS" else "BLOCKCHAIN INDEXER RECEIPT STATUS"
                                Text(receiptHeader, style = MaterialTheme.typography.labelMedium, color = NeonCyan)
                                Icon(Icons.Default.Clear, contentDescription = "Close", tint = TextMutedGrey, modifier = Modifier.size(16.dp).clickable { viewModel.dismissPremiumCheckout() })
                            }
                            Divider(modifier = Modifier.padding(vertical = 10.dp), color = BorderCyberDark)

                            when (val status = checkoutStatus) {
                                is CheckoutStatus.VerifyingHash -> {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                                        CircularProgressIndicator(color = NeonPurple)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        val statusText = if (selectedCryptoChain == "bKash") {
                                            "Querying bKash secure SSL gateway protocol logs..."
                                        } else {
                                            "Reading transaction parameters. Quering ERC-20 / TRC-20 nodes..."
                                        }
                                        Text(statusText, color = TextMutedGrey, fontSize = 11.sp)
                                    }
                                }

                                is CheckoutStatus.VerifiedSuccess -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(NeonGreen.copy(alpha = 0.1f))
                                            .padding(12.dp)
                                    ) {
                                        Column {
                                            val successTitle = if (selectedCryptoChain == "bKash") "bKASH PAYMENT CERTIFIED" else "ON-CHAIN CONFIRMATION SECURED"
                                            val successDesc = if (selectedCryptoChain == "bKash") {
                                                "Transaction verified successfully. Premium study tools unlocked permanently. Portfolio rewarded +500 AP."
                                            } else {
                                                "Transaction verified on mainnets coordinate. Premium advanced neural solvers unlocked permanently. Portfolio rewarded +500 AP."
                                            }
                                            Text(successTitle, color = NeonGreen, fontWeight = FontWeight.Bold)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                successDesc,
                                                fontSize = 11.sp,
                                                color = NeonGreen
                                            )
                                        }
                                    }
                                }

                                is CheckoutStatus.FailedVerification -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(WarningCrimson.copy(alpha = 0.1f))
                                            .padding(12.dp)
                                    ) {
                                        Column {
                                            val failureTitle = if (selectedCryptoChain == "bKash") "bKASH GATEWAY VERIFICATION REJECTED" else "ON-CHAIN VERIFICATION REJECTED"
                                            Text(failureTitle, color = WarningCrimson, fontWeight = FontWeight.Bold)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                status.errorMsg,
                                                fontSize = 11.sp,
                                                color = WarningCrimson
                                            )
                                        }
                                    }
                                }
                                else -> {}
                            }
                        }
                    }
                }
            }
        }
    }
}
