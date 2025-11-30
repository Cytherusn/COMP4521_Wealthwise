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
## 3.1 Core Functionalities

### Personal Finance Management
- **Dashboard**  
  Summary view showing total balance, income vs. expense chart (using **MPAndroidChart**), and recent transactions.
- **Categorization**  
  Pre-defined and fully customizable categories (e.g., Food, Transport, Salary, Entertainment, Utilities, etc.).
- **Add Transactions**  
  Simple form to log income or expenses with fields: amount, category, date, note, and payment method.
- **Search & Filter Transactions**  
  Searchable and filterable interface by title, category, date range, or type (income/expense).
- **View Transactions**  
  Complete, filterable, and searchable list of all personal transactions.
- **Budgeting**  
  Set monthly (or custom period) budgets per category with push notifications when approaching or exceeding limits.

### Group Finance Management (Split-wise Style)
- **Create/Join Groups**  
  Users can create a group (e.g., "Paris Trip 2024", "Flatmates 2025") and invite others via unique code or shareable link.
- **Group Expenses**  
  Any member can add an expense paid by one person on behalf of the group (supports unequal splits and percentage/custom amounts).
- **Balance Settlement**  
  Automatic calculation of who owes whom + simplified settlement view. Includes **"Settle Up"** button to mark debts as cleared.
- **Group Dashboard**  
  Displays total group expenses, individual contributions, balance summary  status, and a feed of recent group activities.

### Dynamic Chart Visualization
- Real-time visual insights from transaction data
- Time-period grouping: **Daily**, **Weekly**, **Monthly**, **Yearly**, or **Custom range**
- Category breakdown (example pie chart):
  - Food – 35%
  - Transport – 20%
  - Entertainment – 15%
  - Utilities – 15%
  - Shopping – 10%
  - Other – 5%
- Additional chart types: bar charts for income/expense trends, line charts for balance over time

### User Login & Account Management
- Secure user authentication (email/password + optional biometric login)
- **Privacy Controls**  
  Granular visibility settings for profile, personal transactions, and group entries: **Private / Friends-only / Group-only / Public**
- **Data Management**  
  Bulk edit, export, or permanently delete records with multi-stage confirmation
- **Customization**  
  - Theme (Light / Dark / System)
  - Language & localization
  - Currency selection
  - Date format preferences
  - Default category and payment method settings

