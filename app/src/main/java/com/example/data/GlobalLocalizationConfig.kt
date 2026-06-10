package com.example.data

data class BoardInfo(
    val id: String,
    val name: String,
    val description: String,
    val gradingCriteria: String
)

data class CountryConfig(
    val code: String,
    val name: String,
    val flag: String,
    val currency: String,
    val currencySymbol: String,
    val alternativeFiatPrice: String,
    val boards: List<BoardInfo>
)

object GlobalLocalizationConfig {
    val countries = listOf(
        CountryConfig(
            code = "USA",
            name = "United States",
            flag = "🇺🇸",
            currency = "USD",
            currencySymbol = "$",
            alternativeFiatPrice = "$27 USD",
            boards = listOf(
                BoardInfo("SAT", "SAT Exam", "Standardized college entrance", "Focus on analytical reasoning and evidence-based problem-solving."),
                BoardInfo("AP", "Advanced Placement", "High school college-credit", "Focus on conceptual depth, AP grading rubrics, and structured explanations."),
                BoardInfo("ACT", "ACT Achievement", "Curriculum-based college test", "Focus on speed, precision, and direct syllabus validation.")
            )
        ),
        CountryConfig(
            code = "GBR",
            name = "United Kingdom",
            flag = "🇬🇧",
            currency = "GBP",
            currencySymbol = "£",
            alternativeFiatPrice = "£21 GBP",
            boards = listOf(
                BoardInfo("A_LEVELS", "GCE A-Levels", "Advanced level secondary exams", "Strict adherence to UK A-Level mark schemes, Command Words, and exact formula derivations."),
                BoardInfo("GCSE", "GCSE Exam", "General Certificate of Secondary Education", "Focus on fundamental clarity and textbook grading protocols.")
            )
        ),
        CountryConfig(
            code = "IND",
            name = "India",
            flag = "🇮🇳",
            currency = "INR",
            currencySymbol = "₹",
            alternativeFiatPrice = "₹2,250 INR",
            boards = listOf(
                BoardInfo("CBSE", "CBSE Board", "Central Board of Secondary Education", "Align strictly with NCERT textbooks, step-wise marks distribution, and board scoring matrices."),
                BoardInfo("JEE", "JEE (Joint Entrance Exam)", "Elite engineering entrance", "High-difficulty calculus, multi-step physics mechanics, and logic derivations."),
                BoardInfo("ICSE", "ICSE Board", "Indian Certificate of Secondary Education", "Thorough explanatory answers with English linguistic precision.")
            )
        ),
        CountryConfig(
            code = "BGD",
            name = "Bangladesh",
            flag = "🇧🇩",
            currency = "BDT",
            currencySymbol = "৳",
            alternativeFiatPrice = "3,700 BDT",
            boards = listOf(
                BoardInfo("NCTB", "NCTB Board (HSC/SSC)", "National Curriculum & Textbook Board", "Strict alignment with NCTB textbook concepts (e.g. Pramanik, Tapas, Ishaq solvers), creative question (Srijonshil) structures, and HSC grading guidelines.")
            )
        ),
        CountryConfig(
            code = "CAN",
            name = "Canada",
            flag = "🇨🇦",
            currency = "CAD",
            currencySymbol = "C$",
            alternativeFiatPrice = "C$37",
            boards = listOf(
                BoardInfo("ONTARIO", "Ontario Curriculum", "OSSD standards", "Ontario Ministry expectations for Application, Knowledge, Communication, and Thinking."),
                BoardInfo("BC", "British Columbia (BC) Curriculum", "Provincial guidelines", "Core competencies approach with evidence-based reasoning checks.")
            )
        ),
        CountryConfig(
            code = "AUS",
            name = "Australia",
            flag = "🇦🇺",
            currency = "AUD",
            currencySymbol = "A$",
            alternativeFiatPrice = "A$40",
            boards = listOf(
                BoardInfo("HSC", "HSC Course (NSW)", "Higher School Certificate", "NSW NESA standards focusing on cognitive verbs and assessment markers."),
                BoardInfo("VCE", "VCE Course (Victoria)", "Victorian Certificate of Education", "VCAA study design criteria.")
            )
        ),
        CountryConfig(
            code = "ARE",
            name = "United Arab Emirates",
            flag = "🇦🇪",
            currency = "AED",
            currencySymbol = "AED ",
            alternativeFiatPrice = "AED 99",
            boards = listOf(
                BoardInfo("MOD_UAE", "UAE Ministry of Education", "National public standard", "Standard aligned with UAE Ministry guidelines and Arabic/English language streams."),
                BoardInfo("KHDA", "KHDA Private Boards", "Dubai international private curriculum", "Syllbus matching elite private assessments.")
            )
        ),
        CountryConfig(
            code = "TUR",
            name = "Turkey",
            flag = "🇹🇷",
            currency = "TRY",
            currencySymbol = "₺",
            alternativeFiatPrice = "₺900",
            boards = listOf(
                BoardInfo("YKS", "YKS Exam (TYT/AYT)", "Higher Education Institutions Exam", "Focus on Turkish university entrance styles, conceptual mechanics, and rapid analysis."),
                BoardInfo("LGS", "LGS Board", "High school transition exam", "Focused on logic and reading comprehension standard benchmarks.")
            )
        ),
        CountryConfig(
            code = "JPN",
            name = "Japan",
            flag = "🇯🇵",
            currency = "JPY",
            currencySymbol = "¥",
            alternativeFiatPrice = "¥4,200",
            boards = listOf(
                BoardInfo("CENTER_TEST", "Common Test for University Admissions", "Kyotsu-test standards", "Focus on dynamic logical rigor, multiple-choice structures, and precision theory.")
            )
        ),
        CountryConfig(
            code = "SGP",
            name = "Singapore",
            flag = "🇸🇬",
            currency = "SGD",
            currencySymbol = "S$",
            alternativeFiatPrice = "S$36",
            boards = listOf(
                BoardInfo("SEAB_O_LEVEL", "GCE O-Levels (SEAB)", "Singapore exams board standard", "Extremely high standards of structure, mathematical proofs, and command action words."),
                BoardInfo("SEAB_A_LEVEL", "GCE A-Levels (Singapore)", "Cambridge-Singapore A-Level joint", "Advanced step-wise marking and rigorous analytical breakdowns.")
            )
        ),
        CountryConfig(
            code = "DEU",
            name = "Germany",
            flag = "🇩🇪",
            currency = "EUR",
            currencySymbol = "€",
            alternativeFiatPrice = "€25",
            boards = listOf(
                BoardInfo("ABITUR", "Abitur Standards", "Gymnasium graduation exam", "Focus on deep structured conceptual explanations, essay layout formats, and rigorous proofs.")
            )
        ),
        CountryConfig(
            code = "FRA",
            name = "France",
            flag = "🇫🇷",
            currency = "EUR",
            currencySymbol = "€",
            alternativeFiatPrice = "€25",
            boards = listOf(
                BoardInfo("BAC", "Baccalauréat Exam", "Secondary school exit exam", "Philosophical, algebraic, and scientific synthesis with methodological rigor.")
            )
        ),
        CountryConfig(
            code = "SAU",
            name = "Saudi Arabia",
            flag = "🇸🇦",
            currency = "SAR",
            currencySymbol = "SR ",
            alternativeFiatPrice = "101 SR",
            boards = listOf(
                BoardInfo("QIYAS", "Qiyas (Tahsili/Qudrat)", "National testing assessment", "Focus on Tahsili subject scores and Qudrat conceptual logic tests.")
            )
        ),
        CountryConfig(
            code = "ZAF",
            name = "South Africa",
            flag = "🇿🇦",
            currency = "ZAR",
            currencySymbol = "R ",
            alternativeFiatPrice = "R 500",
            boards = listOf(
                BoardInfo("NSC", "NSC (National Senior Certificate)", "Matriculation standards", "Strict alignment with Department of Basic Education guidelines and marking rubrics.")
            )
        ),
        CountryConfig(
            code = "PAK",
            name = "Pakistan",
            flag = "🇵🇰",
            currency = "PKR",
            currencySymbol = "₨",
            alternativeFiatPrice = "₨7,500",
            boards = listOf(
                BoardInfo("BISE", "Matric/FSc Board", "Provincial Boards of Intermediate and Secondary", "Syllbus alignment with provincial textbook boards and step-by-step scoring rules.")
            )
        ),
        CountryConfig(
            code = "MYS",
            name = "Malaysia",
            flag = "🇲🇾",
            currency = "MYR",
            currencySymbol = "RM ",
            alternativeFiatPrice = "RM 115",
            boards = listOf(
                BoardInfo("SPM", "SPM National Exam", "Sijil Pelajaran Malaysia", "Strict scoring standards aligned with MoE Malaysia directives.")
            )
        ),
        CountryConfig(
            code = "KOR",
            name = "South Korea",
            flag = "🇰🇷",
            currency = "KRW",
            currencySymbol = "₩",
            alternativeFiatPrice = "₩36,000",
            boards = listOf(
                BoardInfo("SUNEUNG", "Suneung (CSAT Exam)", "College scholastic ability test", "Ultra-high logical rigor, reasoning-intensive questions, and quick-thinking steps.")
            )
        ),
        CountryConfig(
            code = "EGY",
            name = "Egypt",
            flag = "🇪🇬",
            currency = "EGP",
            currencySymbol = "E£",
            alternativeFiatPrice = "E£1,280",
            boards = listOf(
                BoardInfo("THANAWEYA", "Thanaweya Amma", "General secondary certificate", "Standard aligned with Egyptian Ministry of Education grading expectations.")
            )
        ),
        CountryConfig(
            code = "BRA",
            name = "Brazil",
            flag = "🇧🇷",
            currency = "BRL",
            currencySymbol = "R$",
            alternativeFiatPrice = "R$138",
            boards = listOf(
                BoardInfo("ENEM", "ENEM Exam", "National High School Exam", "Interdisciplinary problem-solving and critical essay rubrics.")
            )
        ),
        CountryConfig(
            code = "NGA",
            name = "Nigeria",
            flag = "🇳🇬",
            currency = "NGN",
            currencySymbol = "₦",
            alternativeFiatPrice = "₦40,000",
            boards = listOf(
                BoardInfo("WAEC", "WAEC Exam", "West African Examinations Council", "Structured explanations, regional criteria, and exact paper formats."),
                BoardInfo("JAMB", "JAMB UTME", "Joint Admissions matriculation", "Fast retrieval of standard conceptual structures.")
            )
        ),
        CountryConfig(
            code = "MEX",
            name = "Mexico",
            flag = "🇲🇽",
            currency = "MXN",
            currencySymbol = "$",
            alternativeFiatPrice = "$540 MXN",
            boards = listOf(
                BoardInfo("CENEVAL", "EXANI-II Exam", "National admissions board", "Focus on analytical math, reading, and specific diagnostic areas.")
            )
        ),
        CountryConfig(
            code = "ITA",
            name = "Italy",
            flag = "🇮🇹",
            currency = "EUR",
            currencySymbol = "€",
            alternativeFiatPrice = "€25",
            boards = listOf(
                BoardInfo("MATURITA", "Esame di Stato (Maturità)", "High school exit state exams", "Emphasis on historical, classical, mathematical and comprehensive synthesis.")
            )
        ),
        CountryConfig(
            code = "ESP",
            name = "Spain",
            flag = "🇪🇸",
            currency = "EUR",
            currencySymbol = "€",
            alternativeFiatPrice = "€25",
            boards = listOf(
                BoardInfo("SELECTIVIDAD", "Selectividad (EBAU/EvAU)", "Spanish university entrance", "Focus on exact wording, proof formats, and autonomous community rubrics.")
            )
        ),
        CountryConfig(
            code = "NPL",
            name = "Nepal",
            flag = "🇳🇵",
            currency = "NPR",
            currencySymbol = "₨",
            alternativeFiatPrice = "₨3,500",
            boards = listOf(
                BoardInfo("NEB", "NEB (Class 11/12)", "National Examinations Board", "Align with NEB Syllabus guidelines, exact long question criteria, and numerical marks breakdown.")
            )
        ),
        CountryConfig(
            code = "LKA",
            name = "Sri Lanka",
            flag = "🇱🇰",
            currency = "LKR",
            currencySymbol = "Rs ",
            alternativeFiatPrice = "Rs 8,200",
            boards = listOf(
                BoardInfo("GCE_AL", "GCE A-Levels (Sri Lanka)", "Department of Examinations SL", "High mathematical rigor, strict local board definitions, essay questions.")
            )
        ),
        CountryConfig(
            code = "QAT",
            name = "Qatar",
            flag = "🇶🇦",
            currency = "QAR",
            currencySymbol = "QR ",
            alternativeFiatPrice = "98 QR",
            boards = listOf(
                BoardInfo("QAT_CERT", "Qatar Senior School Certificate", "Ministry of Education standards", "Focus on scientific literacy and standardized calculations.")
            )
        ),
        CountryConfig(
            code = "NZL",
            name = "New Zealand",
            flag = "🇳🇿",
            currency = "NZD",
            currencySymbol = "NZ$",
            alternativeFiatPrice = "NZ$44",
            boards = listOf(
                BoardInfo("NCEA", "NCEA LEVEL 1-3", "NZ Qualifications Authority", "Evaluates Achieved, Merit, and Excellence levels for external assessment standards.")
            )
        )
    )

    fun getCountryByCode(code: String): CountryConfig? {
        return countries.find { it.code.equals(code, ignoreCase = true) }
    }

    fun getBoardInfo(countryCode: String, boardId: String): BoardInfo? {
        return getCountryByCode(countryCode)?.boards?.find { it.id.equals(boardId, ignoreCase = true) }
    }

    fun getSyllabusContextPrompt(countryCode: String, boardId: String): String {
        val country = getCountryByCode(countryCode) ?: return ""
        val board = country.boards.find { it.id.equals(boardId, ignoreCase = true) } ?: return ""
        return """
            --- ACADEMIC STANDARDS MATRIX ENFORCEMENT ---
            [Target Country]: ${country.name}
            [Target Board / Curriculum]: ${board.name}
            [Curriculum Scope]: ${board.description}
            [Grading & Rubrics Expectation]: ${board.gradingCriteria}
            You MUST evaluate and explain this academic query strictly in accordance with the aforementioned national guidelines, textbook structures, and regional scoring standards. Keep answer formatting clean and optimized for this curriculum's students.
        """.trimIndent()
    }
}
