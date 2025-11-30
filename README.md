# **Dependencies**
```
dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.navigation.compose)
    implementation(libs.androidx.navigation.runtime.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
```
Core Functionalities
 Personal Finance Management
 Dashboard: A summary view with total balance, income vs. expense chart
(using a library like MPAndroidChart), and recent transactions.
 Categorization: Pre-defined and customizable categories (e.g., Food,
Transport, Salary)
 Add Transactions: A simple form to log income or expenses with amount,
category, date, note, and payment method.
 Search Transactions: A searchable and filterable interfaceto find specific
transactions by title, category, or date.
 View Transactions: A filterable and searchable list of all transactions.
 Budgeting: Set monthly budgets for categories and receive notifications when
approaching the limit.
 Group Finance Management
 Create/Join Groups: Users can create a group (e.g., "Paris Trip 2024") and
invite others via a unique code or link.
 Group Expenses: Any member can add an expense paid by one member on
behalf of the group.
 Balance Settlement: The app automatically calculates who owes whom and
provides a clear summary. Features a "Settle Up" button to mark debts as
cleared.
COMP4521 Mobile Application Development Proposal
 Group Dashboard: Shows total group expenses, individual contributions, and a
feed of recent group activities.
 Dynamic Chart Visualization
 Immediate visual insights: Converts transaction data into immediate,
actionable visual insights
 Group by time: Views by Monthly, Weekly, or Custom period
 Data Categorization: Pie breakdown from sample data: e.g. Food 35%,
Transport 20%, Entertainment 15%, Utilities 15%, Shopping 10%, Other 5%
 User Login and Account Management
 Privacy controls: Users can set granular visibility for profiles, transactions, and
group entries (Private / Group / Public)
 User Data management: bulk edit or permanently delete records with staged
confirmations
 Customized settings: save settings of localization and theme
