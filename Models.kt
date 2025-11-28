package com.example.wealthwise

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Enum classes for shared types
@Serializable
enum class TransactionType {
    @SerialName("income") INCOME,
    @SerialName("expense") EXPENSE
}

@Serializable
enum class PaymentMethod {
    @SerialName("cash") CASH,
    @SerialName("credit_card") CREDIT_CARD,
    @SerialName("debit_card") DEBIT_CARD,
    @SerialName("bank_transfer") BANK_TRANSFER,
    @SerialName("digital_wallet") DIGITAL_WALLET,
    @SerialName("other") OTHER
}

@Serializable
enum class Privacy {
    @SerialName("private") PRIVATE,
    @SerialName("group") GROUP,
    @SerialName("public") PUBLIC
}

@Serializable
enum class SplitType {
    @SerialName("equal") EQUAL,
    @SerialName("custom") CUSTOM
}

@Serializable
enum class Role {
    @SerialName("owner") OWNER,
    @SerialName("member") MEMBER
}

@Serializable
enum class Status {
    @SerialName("pending") PENDING,
    @SerialName("completed") COMPLETED
}

// Data classes from JSON schemas

@Serializable
data class Transaction(
    val amount: Double,
    val type: TransactionType,
    val categoryId: String? = null,
    val categoryName: String,
    val title: String,
    val note: String? = null,
    val date: String,
    val paymentMethod: PaymentMethod? = null,
    val privacy: Privacy = Privacy.PRIVATE
)

@Serializable
data class GroupExpense(
    val groupId: String,
    val title: String,
    val amount: Double,
    val paidByEmail: String,
    val paidByName: String,
    val date: String,
    val category: String? = null,
    val splitType: SplitType = SplitType.EQUAL,
    val participants: List<Participant>? = null
)

@Serializable
data class Participant(
    val email: String? = null,
    val name: String? = null,
    val share: Double? = null
)

@Serializable
data class Group(
    val name: String,
    val description: String? = null,
    val inviteCode: String,
    val totalExpenses: Double = 0.0,
    val memberCount: Int = 1,
    val currency: String = "USD"
)

@Serializable
data class Category(
    val name: String,
    val type: TransactionType,
    val icon: String? = null,
    val color: String? = null,
    val isCustom: Boolean = false
)

@Serializable
data class GroupMember(
    val groupId: String,
    val userEmail: String,
    val userName: String,
    val role: Role = Role.MEMBER,
    val totalPaid: Double = 0.0,
    val balance: Double = 0.0
)

@Serializable
data class Settlement(
    val groupId: String,
    val fromEmail: String,
    val fromName: String,
    val toEmail: String,
    val toName: String,
    val amount: Double,
    val date: String,
    val status: Status = Status.COMPLETED
)

@Serializable
data class Budget(
    val categoryId: String? = null,
    val categoryName: String,
    val limitAmount: Double,
    val spentAmount: Double = 0.0,
    val month: String,
    val alertThreshold: Double = 80.0
)