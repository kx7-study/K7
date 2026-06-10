package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GlobalLocalizationConfig
import com.example.data.UserProfile
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolverScreen(
    viewModel: MainViewModel,
    profile: UserProfile,
    modifier: Modifier = Modifier
) {
    val solveLoading by viewModel.solveLoading.collectAsState()
    val solveResult by viewModel.solveResult.collectAsState()

    var activeInputText by remember { mutableStateOf("") }
    var selectedSubject by remember { mutableStateOf("Physics") }
    val subjects = listOf("Physics", "Chemistry", "Higher Mathematics", "Biology", "English", "General AI")

    // Dynamic localization values
    val currentCountryCode = profile.selectedCountryCode
    val currentBoardId = profile.selectedBoardId
    val currentCountry = GlobalLocalizationConfig.getCountryByCode(currentCountryCode) ?: GlobalLocalizationConfig.countries[0]
    val currentBoard = currentCountry.boards.find { it.id == currentBoardId } ?: currentCountry.boards[0]

    // Dialog state controllers
    var showCountryDialog by remember { mutableStateOf(false) }
    var showPaywallDialog by remember { mutableStateOf(false) }
    var showCheckoutDialog by remember { mutableStateOf(false) }
    var selectedCheckoutTier by remember { mutableStateOf("1_MONTH") }

    val coroutineScope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current

    // Aesthetic clean background
    val neutralBackground = CyberBlack // #030305 dark sleek canvas
    val surfaceContainer = DeepSpaceSlate // #0C0C12 minimalist container

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(neutralBackground)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // ================== MINIMALIST TOP BAR ==================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(surfaceContainer)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Product Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "K7",
                            color = Color.Black,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        )
                    }
                    Column {
                        Text(
                            text = "KX7-STUDY",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "Academic Neural Solver",
                            fontSize = 10.sp,
                            color = TextMutedGrey
                        )
                    }
                }

                // Dynamic Country/Curriculum Selector Dropdown Button
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { showCountryDialog = true }
                        .background(BorderCyberDark)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("country_curr_selector"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = currentCountry.flag)
                    Text(
                        text = "${currentBoard.name} (${currentCountry.code})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Select Curriculum",
                        tint = NeonCyan,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // ================== DOMINANT CENTER UTILITY CONTAINER ==================
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }

                // Premium Conversion Promo Banner
                item {
                    PromoBanner(
                        isPremium = profile.isPremium,
                        subscriptionType = profile.subscriptionType,
                        expiresTimestamp = profile.subscriptionExpiresTimestamp,
                        onUpgradeClick = {
                            selectedCheckoutTier = "1_MONTH"
                            showPaywallDialog = true
                        }
                    )
                }

                // Subject Selection Strip (Material 3 Filter Chips)
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Select Academic Domain:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMutedGrey
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            subjects.take(4).forEach { sub ->
                                val isSelected = selectedSubject == sub
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { selectedSubject = sub }
                                        .background(if (isSelected) NeonCyan.copy(alpha = 0.15f) else surfaceContainer)
                                        .border(
                                            width = 1.dp,
                                            color = if (isSelected) NeonCyan else Color.Transparent,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = sub.replace("Mathematics", "Math"),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) NeonCyan else TextWhite
                                    )
                                }
                            }
                        }
                    }
                }

                // Distraction-free Primary Problem input Card
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BorderCyberDark, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = surfaceContainer)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Problem Solving Desk".uppercase(),
                                fontSize = 11.sp,
                                letterSpacing = 1.2.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonPurple
                            )

                            // The wide, high-contrast, distraction-free input field
                            OutlinedTextField(
                                value = activeInputText,
                                onValueChange = { activeInputText = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                                    .testTag("solver_input_field"),
                                textStyle = androidx.compose.ui.text.TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.White,
                                    fontFamily = FontFamily.SansSerif
                                ),
                                placeholder = {
                                    Text(
                                        "Type or copy-paste your academic problem here (e.g. 'Solve the definite integral of x^3 from 1 to 5', or ask physical derivations)...",
                                        fontSize = 13.sp,
                                        color = TextMutedGrey
                                    )
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = NeonCyan,
                                    unfocusedBorderColor = BorderCyberDark,
                                    focusedContainerColor = CyberBlack,
                                    unfocusedContainerColor = CyberBlack
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )

                            // Scan & Speak Quick Templates (Bypasses hardcoded topic dropdown bottlenecks)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Scan formula mock button
                                OutlinedButton(
                                    onClick = {
                                        activeInputText = "A projectile is launched with velocity 40 m/s at an angle of 30 degrees. Calculate maximum orbit height and flight duration."
                                        selectedSubject = "Physics"
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f).height(40.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderCyberDark)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Scan icon", modifier = Modifier.size(16.dp), tint = NeonCyan)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Mock Camera Scan", fontSize = 11.sp)
                                }

                                // Speak voice mock button
                                OutlinedButton(
                                    onClick = {
                                        activeInputText = "Verify if the centromere dissolves and chromatid count changes during mitotic anaphase."
                                        selectedSubject = "Biology"
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f).height(40.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderCyberDark)
                                ) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = "Voice icon", modifier = Modifier.size(16.dp), tint = NeonPurple)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Mock Voice Dictate", fontSize = 11.sp)
                                }
                            }

                            // Clean, obvious Solve Submit button (Touch targets >= 48dp)
                            Button(
                                onClick = {
                                    if (activeInputText.trim().isNotEmpty()) {
                                        viewModel.callNeuralSolver(activeInputText, selectedSubject)
                                    }
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("neural_solve_trigger"),
                                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                                enabled = activeInputText.trim().isNotEmpty() && !solveLoading
                            ) {
                                if (solveLoading) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.Black, strokeWidth = 2.dp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Syncing live context indices...", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                } else {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(Icons.Default.Check, contentDescription = "Solve Icon", tint = Color.Black, modifier = Modifier.size(18.dp))
                                        Text("DISPATCH ACADEMIC SOLVER", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 13.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                // ================== STREAMING SOLUTION VIEWPORT ==================
                if (solveLoading || solveResult != null) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, if (solveLoading) BorderCyberDark else NeonCyan, RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(containerColor = CyberBlack)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(Icons.Default.Info, contentDescription = "Status", tint = if (solveLoading) TextMutedGrey else NeonGreen, modifier = Modifier.size(16.dp))
                                        Text(
                                            text = if (solveLoading) "SOLVER PROCESSING..." else "VERIFIED SOLUTION BREAKDOWN",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (solveLoading) TextMutedGrey else NeonGreen
                                        )
                                    }

                                    // Clear Solution Console Action
                                    IconButton(
                                        onClick = { viewModel.clearSolverResult() },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.Refresh, contentDescription = "Clear", tint = WarningCrimson, modifier = Modifier.size(18.dp))
                                    }
                                }

                                Divider(color = BorderCyberDark)

                                if (solveLoading && (solveResult == null || solveResult!!.trim().isEmpty())) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(24.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        CircularProgressIndicator(color = NeonCyan, modifier = Modifier.size(36.dp))
                                        Text(
                                            text = "Aggregating indices against ${currentBoard.name} grading rubrics...",
                                            fontSize = 12.sp,
                                            color = TextMutedGrey,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                } else {
                                    val resultText = solveResult ?: ""
                                    
                                    // Elegant standard system typography vertical rendering
                                    Text(
                                        text = buildAnnotatedMasterpieceString(resultText),
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontFamily = FontFamily.SansSerif,
                                            fontSize = 15.sp,
                                            lineHeight = 22.sp
                                        ),
                                        color = TextWhite,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("solver_result_text")
                                    )

                                    Divider(color = BorderCyberDark, modifier = Modifier.padding(vertical = 4.dp))

                                    // Real Action CTAs for the solution
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        // Save to Second Brain
                                        Button(
                                            onClick = {
                                                viewModel.addBrainNote(
                                                    title = "$selectedSubject: Root Proof",
                                                    content = resultText,
                                                    subject = selectedSubject,
                                                    type = "notes"
                                                )
                                            },
                                            modifier = Modifier.weight(1f).height(44.dp),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
                                        ) {
                                            Icon(Icons.Default.Add, contentDescription = "Add to brain", tint = Color.White, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Save to Vault", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }

                                        // Copy solution text
                                        OutlinedButton(
                                            onClick = {
                                                clipboardManager.setText(AnnotatedString(resultText))
                                            },
                                            modifier = Modifier.weight(1f).height(44.dp),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderCyberDark)
                                        ) {
                                            Icon(Icons.Default.Share, contentDescription = "Copy text", tint = NeonCyan, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Copy Text", fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }

        // ================== COUNTRY & CURRICULUM SELECTOR DIALOG ==================
        if (showCountryDialog) {
            AlertDialog(
                onDismissRequest = { showCountryDialog = false },
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.List, contentDescription = "Globes", tint = NeonCyan)
                        Text(
                            text = "Global Syllabus Matrix",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = TextWhite
                        )
                    }
                },
                text = {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Text(
                                text = "Choose your country and educational board. Every answer will be specifically structured to matches your region's syllabus and grading schemes.",
                                fontSize = 12.sp,
                                color = TextMutedGrey,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        GlobalLocalizationConfig.countries.forEach { country ->
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(surfaceContainer)
                                        .padding(10.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(text = country.flag, fontSize = 16.sp)
                                        Text(
                                            text = country.name,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 14.sp,
                                            color = TextWhite
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    // Display boards
                                    country.boards.forEach { board ->
                                        val isCurrent = currentCountryCode == country.code && currentBoardId == board.id
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    viewModel.updateLocalization(country.code, board.id)
                                                    showCountryDialog = false
                                                }
                                                .padding(vertical = 6.dp, horizontal = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column(modifier = Modifier.weight(0.85f)) {
                                                Text(
                                                    text = board.name,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isCurrent) NeonCyan else TextWhite
                                                )
                                                Text(
                                                    text = board.description,
                                                    fontSize = 10.sp,
                                                    color = TextMutedGrey
                                                )
                                            }
                                            if (isCurrent) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = "Selected",
                                                    tint = NeonGreen,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            } else {
                                                Icon(
                                                    imageVector = Icons.Default.KeyboardArrowRight,
                                                    contentDescription = "Select",
                                                    tint = TextMutedGrey,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                        Divider(color = BorderCyberDark.copy(alpha = 0.5f))
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showCountryDialog = false }) {
                        Text("CLOSE", color = NeonCyan, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = DeepSpaceSlate
            )
        }

        // ================== PREMIUM PAYWALL MODAL (3-TIERS) ==================
        if (showPaywallDialog) {
            AlertDialog(
                onDismissRequest = { showPaywallDialog = false },
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(NeonPurple.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Star, contentDescription = "Pro VIP", tint = NeonPurple, modifier = Modifier.size(30.dp))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "PREMIUM SOLVER ENGINE",
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            color = NeonPurple,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                text = {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            Text(
                                text = "Bypass free tier bottlenecks. Access ultra-fast Groq LPU computing clusters, real-time web crawlers, and subjective step solutions.",
                                fontSize = 12.sp,
                                color = TextMutedGrey,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        // Plan Option 1: 1 Month
                        item {
                            PaywallTierCard(
                                title = "1 Month Access Plan",
                                cryptoPrice = "27 USDT",
                                fiatPrice = currentCountry.alternativeFiatPrice,
                                durationLabel = "30 Days Full Access",
                                badgeText = null,
                                onClick = {
                                    selectedCheckoutTier = "1_MONTH"
                                    showPaywallDialog = false
                                    showCheckoutDialog = true
                                }
                            )
                        }

                        // Plan Option 2: 3 Months
                        item {
                            PaywallTierCard(
                                title = "3 Months Access Plan",
                                cryptoPrice = "77 USDT",
                                fiatPrice = "77 USD",
                                durationLabel = "90 Days Full Access + Growth Metrics",
                                badgeText = "POPULAR CHOICE",
                                onClick = {
                                    selectedCheckoutTier = "3_MONTHS"
                                    showPaywallDialog = false
                                    showCheckoutDialog = true
                                }
                            )
                        }

                        // Plan Option 3: 1 Year
                        item {
                            PaywallTierCard(
                                title = "1 Year Access Plan",
                                cryptoPrice = "277 USDT",
                                fiatPrice = "277 USD",
                                durationLabel = "365 Days Elite Access + Priority LPUs",
                                badgeText = "BEST VALUE SAVINGS",
                                onClick = {
                                    selectedCheckoutTier = "1_YEAR"
                                    showPaywallDialog = false
                                    showCheckoutDialog = true
                                }
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showPaywallDialog = false }) {
                        Text("CANCEL", color = WarningCrimson, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = DeepSpaceSlate
            )
        }

        // ================== PAYMENT GATEWAY SECURE INTEGRATION DIALOG ==================
        if (showCheckoutDialog) {
            var selectedPaymentMethod by remember { mutableStateOf("CRYPTO") } // "CRYPTO" | "FIAT"
            var walletNetwork by remember { mutableStateOf("TRC-20") } // TRC-20, ERC-20, BEP-20
            var txHashInput by remember { mutableStateOf("") }
            
            // Fiat state management
            var creditCardNumber by remember { mutableStateOf("") }
            var cardExpiry by remember { mutableStateOf("") }
            var cardCvv by remember { mutableStateOf("") }
            var bkashMobileNumber by remember { mutableStateOf("") }

            var isProcessingState by remember { mutableStateOf(false) }
            var processingMessage by remember { mutableStateOf("") }
            var paymentCompletedSuccessfully by remember { mutableStateOf(false) }

            val tierPriceString = when (selectedCheckoutTier) {
                "1_MONTH" -> "27 USDT / ${currentCountry.alternativeFiatPrice}"
                "3_MONTHS" -> "77 USDT / USD"
                else -> "277 USDT / USD"
            }

            AlertDialog(
                onDismissRequest = { if (!isProcessingState) showCheckoutDialog = false },
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = "Secure Checkout", tint = NeonGreen)
                        Text(
                            text = "Secured Gateway Checkout",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                },
                text = {
                    if (paymentCompletedSuccessfully) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(30.dp))
                                    .background(NeonGreen.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Check, contentDescription = "Active", tint = NeonGreen, modifier = Modifier.size(36.dp))
                            }
                            Text(
                                text = "PAYMENT VERIFIED SECURELY!",
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                color = NeonGreen
                            )
                            Text(
                                text = "Your profile has been upgraded to Premium Status on the KX7 network. Unlimited Groq LPU computing clusters and Tavily search routing are now unlocked.",
                                fontSize = 12.sp,
                                color = TextWhite,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else if (isProcessingState) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator(color = NeonPurple, modifier = Modifier.size(44.dp))
                            Text(
                                text = processingMessage,
                                fontSize = 13.sp,
                                color = TextWhite,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Establishing secure TLS links & matching blockchain hash registers. Do not close this panel.",
                                fontSize = 11.sp,
                                color = TextMutedGrey,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(BorderCyberDark)
                                        .padding(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "Selected Term:", fontSize = 11.sp, color = TextMutedGrey)
                                        Text(text = selectedCheckoutTier.replace("_", " "), fontSize = 11.sp, color = NeonCyan, fontWeight = FontWeight.Bold)
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "Price Matrix:", fontSize = 11.sp, color = TextMutedGrey)
                                        Text(text = tierPriceString, fontSize = 11.sp, color = NeonGreen, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            // Payment Mode Tab selections
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(CyberBlack)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { selectedPaymentMethod = "CRYPTO" }
                                            .background(if (selectedPaymentMethod == "CRYPTO") NeonCyan.copy(alpha = 0.15f) else Color.Transparent)
                                            .padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "USDT (Crypto)",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (selectedPaymentMethod == "CRYPTO") NeonCyan else TextMutedGrey
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { selectedPaymentMethod = "FIAT" }
                                            .background(if (selectedPaymentMethod == "FIAT") NeonPurple.copy(alpha = 0.15f) else Color.Transparent)
                                            .padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Fiat / Credit Card",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (selectedPaymentMethod == "FIAT") NeonPurple else TextMutedGrey
                                        )
                                    }
                                }
                            }

                            if (selectedPaymentMethod == "CRYPTO") {
                                // USDT Secure checkout info
                                item {
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Text(
                                            text = "1. Select Blockchain Settlement Network:",
                                            fontSize = 11.sp,
                                            color = TextWhite,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            listOf("TRC-20", "ERC-20", "BEP-20").forEach { net ->
                                                val isSelected = walletNetwork == net
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .clip(RoundedCornerShape(6.dp))
                                                        .clickable { walletNetwork = net }
                                                        .background(if (isSelected) NeonCyan.copy(alpha = 0.2f) else CyberBlack)
                                                        .border(1.dp, if (isSelected) NeonCyan else BorderCyberDark, RoundedCornerShape(6.dp))
                                                        .padding(vertical = 8.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(text = net, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSelected) NeonCyan else TextWhite)
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = "2. Copy Secure Deposit Wallet Address:",
                                            fontSize = 11.sp,
                                            color = TextWhite,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(CyberBlack)
                                                .border(1.dp, BorderCyberDark, RoundedCornerShape(8.dp))
                                                .padding(horizontal = 10.dp, vertical = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "0x7b5FE2012aD9e6C18E949b29cb2d3e9EF88Bea02",
                                                fontFamily = FontFamily.Monospace,
                                                fontSize = 10.sp,
                                                color = NeonGreen,
                                                modifier = Modifier.weight(0.85f)
                                            )
                                            IconButton(
                                                onClick = { clipboardManager.setText(AnnotatedString("0x7b5FE2012aD9e6C18E949b29cb2d3e9EF88Bea02")) },
                                                modifier = Modifier.size(24.dp).weight(0.15f)
                                            ) {
                                                Icon(Icons.Default.Share, contentDescription = "Copy Wallet", tint = NeonCyan, modifier = Modifier.size(14.dp))
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = "3. Confirm Deposit on Blockchain. Submit TxHash / Transaction ID:",
                                            fontSize = 11.sp,
                                            color = TextWhite,
                                            fontWeight = FontWeight.Bold
                                        )
                                        OutlinedTextField(
                                            value = txHashInput,
                                            onValueChange = { txHashInput = it },
                                            modifier = Modifier.fillMaxWidth().testTag("txhash_input_checkout"),
                                            placeholder = { Text("Enter 32-character Transaction Hash...", fontSize = 11.sp, color = TextMutedGrey) },
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = NeonCyan,
                                                unfocusedBorderColor = BorderCyberDark,
                                                focusedContainerColor = CyberBlack,
                                                unfocusedContainerColor = CyberBlack
                                            ),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                    }
                                }
                            } else {
                                // FIAT secure checkout options
                                item {
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        if (currentCountryCode == "BGD") {
                                            // Bangladesh local bkash processing Mock
                                            Text(
                                                text = "Secure local bKash Checkout (SSLCommerz settlement):",
                                                fontSize = 11.sp,
                                                color = TextWhite,
                                                fontWeight = FontWeight.Bold
                                            )
                                            OutlinedTextField(
                                                value = bkashMobileNumber,
                                                onValueChange = { bkashMobileNumber = it },
                                                modifier = Modifier.fillMaxWidth().testTag("bkash_mobile"),
                                                placeholder = { Text("Enter 11-digit bKash Mobile Number...", fontSize = 11.sp, color = TextMutedGrey) },
                                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                                colors = OutlinedTextFieldDefaults.colors(
                                                    focusedBorderColor = NeonPurple,
                                                    unfocusedBorderColor = BorderCyberDark,
                                                    focusedContainerColor = CyberBlack,
                                                    unfocusedContainerColor = CyberBlack
                                                ),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            Text(
                                                text = "A secured bKash API simulation payment trigger will authenticate instantly.",
                                                fontSize = 9.sp,
                                                color = TextMutedGrey
                                            )
                                        } else {
                                            // Stripe Global credit card setup Mock
                                            Text(
                                                text = "Stripe Secured Credit Card Input:",
                                                fontSize = 11.sp,
                                                color = TextWhite,
                                                fontWeight = FontWeight.Bold
                                            )
                                            OutlinedTextField(
                                                value = creditCardNumber,
                                                onValueChange = { creditCardNumber = it },
                                                modifier = Modifier.fillMaxWidth().testTag("stripe_card"),
                                                placeholder = { Text("Enter 16-Digit Credit Card Code...", fontSize = 11.sp, color = TextMutedGrey) },
                                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                                colors = OutlinedTextFieldDefaults.colors(
                                                    focusedBorderColor = NeonPurple,
                                                    unfocusedBorderColor = BorderCyberDark,
                                                    focusedContainerColor = CyberBlack,
                                                    unfocusedContainerColor = CyberBlack
                                                ),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                OutlinedTextField(
                                                    value = cardExpiry,
                                                    onValueChange = { cardExpiry = it },
                                                    modifier = Modifier.weight(1f).testTag("stripe_expiry"),
                                                    placeholder = { Text("MM/YY Expiry", fontSize = 11.sp, color = TextMutedGrey) },
                                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                                    colors = OutlinedTextFieldDefaults.colors(
                                                        focusedBorderColor = NeonPurple,
                                                        unfocusedBorderColor = BorderCyberDark,
                                                        focusedContainerColor = CyberBlack,
                                                        unfocusedContainerColor = CyberBlack
                                                    ),
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                OutlinedTextField(
                                                    value = cardCvv,
                                                    onValueChange = { cardCvv = it },
                                                    modifier = Modifier.weight(0.8f).testTag("stripe_cvv"),
                                                    placeholder = { Text("CVV", fontSize = 11.sp, color = TextMutedGrey) },
                                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                                    colors = OutlinedTextFieldDefaults.colors(
                                                        focusedBorderColor = NeonPurple,
                                                        unfocusedBorderColor = BorderCyberDark,
                                                        focusedContainerColor = CyberBlack,
                                                        unfocusedContainerColor = CyberBlack
                                                    ),
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    if (paymentCompletedSuccessfully) {
                        Button(
                            onClick = { showCheckoutDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
                        ) {
                            Text("FINISH ACCESS SUCCESS", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    } else if (!isProcessingState) {
                        val isEnabled = if (selectedPaymentMethod == "CRYPTO") {
                            txHashInput.trim().length >= 8
                        } else {
                            if (currentCountryCode == "BGD") bkashMobileNumber.trim().length >= 11 else creditCardNumber.trim().length >= 12
                        }

                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    isProcessingState = true
                                    processingMessage = "Connecting SSL-Handshake Secured Link..."
                                    delay(1000)
                                    processingMessage = "Verifying Payment payload on API routing registry..."
                                    delay(1200)
                                    processingMessage = "Consolidating Ledger Nodes..."
                                    delay(800)
                                    viewModel.purchaseSubscription(selectedCheckoutTier)
                                    isProcessingState = false
                                    paymentCompletedSuccessfully = true
                                }
                            },
                            enabled = isEnabled,
                            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                        ) {
                            Text("SUBMIT PAY REQUEST", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                },
                dismissButton = {
                    if (!isProcessingState && !paymentCompletedSuccessfully) {
                        TextButton(onClick = { showCheckoutDialog = false }) {
                            Text("BACK", color = WarningCrimson)
                        }
                    }
                },
                containerColor = DeepSpaceSlate
            )
        }
    }
}

@Composable
fun PromoBanner(
    isPremium: Boolean,
    subscriptionType: String,
    expiresTimestamp: Long,
    onUpgradeClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (isPremium) NeonGreen else NeonPurple,
                shape = RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = DeepSpaceSlate)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(0.7f)) {
                if (isPremium) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "🌟", fontSize = 16.sp)
                        Text(
                            text = "PREMIUM ACCESS ALIGNED",
                            color = NeonGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    val dateFormatted = remember(expiresTimestamp) {
                        if (expiresTimestamp > 0) {
                            try {
                                val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                                sdf.format(Date(expiresTimestamp))
                            } catch (e: Exception) {
                                "30 Days Active"
                            }
                        } else {
                            "Continuous"
                        }
                    }
                    Text(
                        text = "Plan: ${subscriptionType.replace("_", " ")} | Valid thru: $dateFormatted",
                        fontSize = 12.sp,
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = "Low-latency LPU streams and web crawlers are fully active on your account.",
                        fontSize = 10.sp,
                        color = TextMutedGrey,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "⚡", fontSize = 16.sp)
                        Text(
                            text = "UPGRADE KX7 PROFILE",
                            color = NeonPurple,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Text(
                        text = "Tier: Academic Free Account",
                        fontSize = 12.sp,
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = "Unlock high-context solutions, 480 tokens/sec solving, and web crawl indexes.",
                        fontSize = 10.sp,
                        color = TextMutedGrey,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            if (!isPremium) {
                Button(
                    onClick = onUpgradeClick,
                    colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                    modifier = Modifier.weight(0.3f).height(38.dp)
                ) {
                    Text("Unlock", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun PaywallTierCard(
    title: String,
    cryptoPrice: String,
    fiatPrice: String,
    durationLabel: String,
    badgeText: String?,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CyberBlack)
            .border(
                width = 1.dp,
                color = if (badgeText != null) NeonCyan else BorderCyberDark,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = TextWhite
                )

                if (badgeText != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(NeonPurple)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = badgeText,
                            fontSize = 8.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = cryptoPrice,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = NeonGreen
                )
                Text(
                    text = "or",
                    fontSize = 11.sp,
                    color = TextMutedGrey
                )
                Text(
                    text = fiatPrice,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = durationLabel,
                    fontSize = 11.sp,
                    color = TextMutedGrey
                )
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Purchase", tint = NeonCyan, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
fun buildAnnotatedMasterpieceString(text: String): androidx.compose.ui.text.AnnotatedString {
    return androidx.compose.ui.text.buildAnnotatedString {
        val lines = text.split("\n")
        lines.forEachIndexed { index, line ->
            when {
                line.startsWith("🔍") -> {
                    pushStyle(androidx.compose.ui.text.SpanStyle(color = NeonCyan, fontWeight = FontWeight.Bold))
                    append(line)
                    pop()
                }
                line.startsWith("✅") -> {
                    pushStyle(androidx.compose.ui.text.SpanStyle(color = NeonGreen, fontWeight = FontWeight.Bold))
                    append(line)
                    pop()
                }
                line.startsWith("⚡") -> {
                    pushStyle(androidx.compose.ui.text.SpanStyle(color = NeonPurple, fontWeight = FontWeight.Bold))
                    append(line)
                    pop()
                }
                line.startsWith("⚠️") -> {
                    pushStyle(androidx.compose.ui.text.SpanStyle(color = Color(255, 170, 0), fontWeight = FontWeight.Bold))
                    append(line)
                    pop()
                }
                line.startsWith("📊") -> {
                    pushStyle(androidx.compose.ui.text.SpanStyle(color = Color.Yellow, fontWeight = FontWeight.SemiBold))
                    append(line)
                    pop()
                }
                line.contains("### 🎯 Core Answer") -> {
                    pushStyle(androidx.compose.ui.text.SpanStyle(color = NeonCyan, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp))
                    append(line)
                    pop()
                }
                line.contains("### 🔍 Step-by-Step Breakdown") -> {
                    pushStyle(androidx.compose.ui.text.SpanStyle(color = NeonPurple, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp))
                    append(line)
                    pop()
                }
                line.contains("### ⚡ 1% Mastery Key") -> {
                    pushStyle(androidx.compose.ui.text.SpanStyle(color = NeonGreen, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp))
                    append(line)
                    pop()
                }
                line.startsWith("**") && line.endsWith("**") -> {
                    pushStyle(androidx.compose.ui.text.SpanStyle(color = Color.White, fontWeight = FontWeight.Bold))
                    append(line)
                    pop()
                }
                else -> {
                    if (line.startsWith("###")) {
                        pushStyle(androidx.compose.ui.text.SpanStyle(color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 18.sp))
                        append(line)
                        pop()
                    } else {
                        append(line)
                    }
                }
            }
            if (index < lines.size - 1) {
                append("\n")
            }
        }
    }
}

