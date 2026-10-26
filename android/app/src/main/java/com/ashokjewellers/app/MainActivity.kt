package com.ashokjewellers.app

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

private val Burgundy = Color(0xFF6D1425)
private val Gold = Color(0xFFD4B16A)
private val Cream = Color(0xFFF8F1E6)
private val Ink = Color(0xFF2D2020)
private val gson = Gson()

data class Design(
    val id: String = UUID.randomUUID().toString(),
    val productId: String,
    val name: String,
    val category: String,
    val metal: String,
    val purity: String,
    val weight: String,
    val photoUrl: String = "",
    val available: Boolean = true,
    val description: String = ""
)
data class MetalRate(val name: String, val amount: String, val updatedAt: Long = System.currentTimeMillis())
data class LineItem(val description: String, val quantity: String, val weight: String, val amount: Double)
data class Payment(val id: String = UUID.randomUUID().toString(), val amount: Double, val method: String, val reference: String, val time: Long = System.currentTimeMillis())
data class Bill(
    val id: String = UUID.randomUUID().toString(),
    val number: String,
    val customerName: String,
    val mobile: String,
    val address: String,
    val invoiceType: String,
    val items: List<LineItem>,
    val total: Double,
    val payments: List<Payment> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val cancelled: Boolean = false,
    val changeHistory: List<String> = emptyList()
) {
    val paid: Double get() = payments.sumOf { it.amount }
    val balance: Double get() = total - paid
}
data class AppData(
    val designs: List<Design> = emptyList(),
    val rates: List<MetalRate> = listOf(MetalRate("22K Gold / g",""), MetalRate("24K Gold / g",""), MetalRate("Silver / g","")),
    val bills: List<Bill> = emptyList(),
    val rateHistory: List<MetalRate> = emptyList(),
    val nextBillNumber: Int = 1
)

class Store(context: Context) {
    private val prefs = context.getSharedPreferences("ashok_jewellers_local", Context.MODE_PRIVATE)
    fun load(): AppData = try {
        gson.fromJson(prefs.getString("data", null), AppData::class.java) ?: AppData()
    } catch (_: Exception) { AppData() }
    fun save(data: AppData) { prefs.edit().putString("data", gson.toJson(data)).apply() }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AshokApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AshokApp() {
    val context = LocalContext.current
    val store = remember { Store(context) }
    var data by remember { mutableStateOf(store.load()) }
    var tab by remember { mutableStateOf("Home") }
    var query by remember { mutableStateOf("") }
    var showAddDesign by remember { mutableStateOf(false) }
    var showNewBill by remember { mutableStateOf(false) }
    var paymentBillId by remember { mutableStateOf<String?>(null) }
    var editBillId by remember { mutableStateOf<String?>(null) }
    var notice by remember { mutableStateOf("") }

    fun update(transform: (AppData) -> AppData) {
        data = transform(data)
        store.save(data)
    }

    MaterialTheme(
        colorScheme = lightColorScheme(primary = Burgundy, secondary = Gold, background = Cream, surface = Color.White)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("ASHOK JEWELLERS", fontWeight = FontWeight.Bold, color = Burgundy, fontSize = 17.sp)
                            Text("Owner app · Offline mode", fontSize = 11.sp, color = Color.Gray)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Cream),
                    actions = { Icon(Icons.Default.Lock, contentDescription = "Local data", tint = Burgundy, modifier = Modifier.padding(end = 16.dp)) }
                )
            },
            bottomBar = {
                NavigationBar(containerColor = Cream) {
                    listOf("Home" to Icons.Default.Home, "Designs" to Icons.Default.Diamond, "Bills" to Icons.Default.ReceiptLong, "Reports" to Icons.Default.BarChart, "Settings" to Icons.Default.Settings).forEach { (name, icon) ->
                        NavigationBarItem(
                            selected = tab == name,
                            onClick = { tab = name; query = "" },
                            icon = { Icon(icon, contentDescription = name) },
                            label = { Text(name, fontSize = 10.sp) }
                        )
                    }
                }
            },
            snackbarHost = { SnackbarHost(remember { SnackbarHostState() }) }
        ) { padding ->
            Box(Modifier.fillMaxSize().background(Color(0xFFFFFDF9)).padding(padding)) {
                when (tab) {
                    "Home" -> HomeScreen(data, onNewBill = { showNewBill = true }, onDesigns = { tab = "Designs" }, onRates = { tab = "Settings" }, onBills = { tab = "Bills" })
                    "Designs" -> DesignsScreen(data, query, { query = it }, { showAddDesign = true }, { design ->
                        update { old -> old.copy(designs = old.designs.map { if (it.id == design.id) design.copy(available = !design.available) else it }) }
                    }, { design ->
                        update { old -> old.copy(designs = old.designs.filterNot { it.id == design.id }) }
                    })
                    "Bills" -> BillsScreen(data, query, { query = it }, { showNewBill = true }, { bill ->
                        editBillId = bill.id
                    }, { bill -> paymentBillId = bill.id }, { bill ->
                        update { old -> old.copy(bills = old.bills.map { if (it.id == bill.id) it.copy(cancelled = true, changeHistory = it.changeHistory + "Cancelled at ${now()}") else it }) }
                    }, { bill -> shareBillPdf(context, bill, data) { notice = it } })
                    "Reports" -> ReportsScreen(data)
                    else -> SettingsScreen(data, onSaveRates = { rates ->
                        update { old ->
                            val stamped = rates.map { it.copy(updatedAt = System.currentTimeMillis()) }
                            old.copy(rates = stamped, rateHistory = old.rateHistory + stamped)
                        }
                        notice = "Rates saved on this phone"
                    }, onExport = { exportBackup(context, data) { notice = it } })
                }
            }
        }

        if (showAddDesign) {
            DesignDialog(onDismiss = { showAddDesign = false }, onSave = { design ->
                update { old ->
                    if (old.designs.any { it.productId.equals(design.productId, true) }) old
                    else old.copy(designs = old.designs + design)
                }
                showAddDesign = false
            })
        }
        if (showNewBill) {
            BillDialog(
                nextNumber = data.nextBillNumber,
                onDismiss = { showNewBill = false },
                onSave = { bill ->
                    update { old -> old.copy(bills = old.bills + bill, nextBillNumber = old.nextBillNumber + 1) }
                    showNewBill = false
                    tab = "Bills"
                }
            )
        }
        paymentBillId?.let { id ->
            data.bills.firstOrNull { it.id == id }?.let { bill ->
                PaymentDialog(bill, onDismiss = { paymentBillId = null }, onSave = { payment ->
                    update { old -> old.copy(bills = old.bills.map { b ->
                        if (b.id == id) b.copy(payments = b.payments + payment, changeHistory = b.changeHistory + "Payment ₹${payment.amount} recorded at ${now()}") else b
                    }) }
                    paymentBillId = null
                })
            }
        }
        editBillId?.let { id ->
            data.bills.firstOrNull { it.id == id }?.let { bill ->
                EditBillDialog(bill, onDismiss = { editBillId = null }, onSave = { updated ->
                    update { old -> old.copy(bills = old.bills.map { b ->
                        if (b.id == id) updated.copy(changeHistory = b.changeHistory + "Bill edited at ${now()}") else b
                    }) }
                    editBillId = null
                })
            }
        }
    }
}

@Composable
private fun ScreenTitle(title: String, subtitle: String = "") {
    Column(Modifier.padding(bottom = 16.dp)) {
        Text(title, fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Burgundy)
        if (subtitle.isNotBlank()) Text(subtitle, color = Color.Gray, fontSize = 13.sp)
    }
}
@Composable
private fun ActionCard(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable(onClick = onClick), colors = CardDefaults.cardColors(containerColor = Cream), shape = RoundedCornerShape(12.dp)) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Burgundy, modifier = Modifier.size(28.dp))
            Spacer(Modifier.width(14.dp))
            Column { Text(title, fontWeight = FontWeight.SemiBold, color = Burgundy); Text(subtitle, fontSize = 12.sp, color = Color.Gray) }
        }
    }
}
@Composable
private fun HomeScreen(data: AppData, onNewBill: () -> Unit, onDesigns: () -> Unit, onRates: () -> Unit, onBills: () -> Unit) {
    val today = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
    val todayBills = data.bills.filter { SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date(it.createdAt)) == today && !it.cancelled }
    val collections = data.bills.filter { !it.cancelled }.flatMap { it.payments }.filter { SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date(it.time)) == today }.sumOf { it.amount }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ScreenTitle("Owner dashboard", "Your business records stay on this device")
        Card(colors = CardDefaults.cardColors(containerColor = Burgundy)) {
            Column(Modifier.fillMaxWidth().padding(18.dp)) {
                Text("TODAY'S COLLECTIONS", color = Gold, fontSize = 12.sp)
                Text(money(collections), color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Text("${todayBills.size} bills created today", color = Color.White.copy(alpha = .8f), fontSize = 12.sp)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            SummaryCard("Total bills", data.bills.count { !it.cancelled }.toString(), Modifier.weight(1f))
            SummaryCard("Balance due", money(data.bills.filter { !it.cancelled }.sumOf { it.balance.coerceAtLeast(0.0) }), Modifier.weight(1f))
        }
        ActionCard("Create a bill", "Enter items and amounts manually", Icons.Default.Add, onNewBill)
        ActionCard("Manage designs", "${data.designs.size} designs saved locally", Icons.Default.Diamond, onDesigns)
        ActionCard("Update metal rates", "22K, 24K and silver", Icons.Default.TrendingUp, onRates)
        ActionCard("Search bills", "Review payments and invoice history", Icons.Default.Search, onBills)
        Text("Offline-first MVP · Cloud sync is not connected", color = Color.Gray, fontSize = 11.sp)
    }
}
@Composable
private fun SummaryCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier, colors = CardDefaults.cardColors(containerColor = Color.White), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE8DCC9))) {
        Column(Modifier.padding(14.dp)) { Text(label, fontSize = 12.sp, color = Color.Gray); Text(value, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Burgundy) }
    }
}
@Composable
private fun DesignsScreen(data: AppData, query: String, onQuery: (String) -> Unit, onAdd: () -> Unit, onToggle: (Design) -> Unit, onDelete: (Design) -> Unit) {
    val filtered = data.designs.filter { listOf(it.productId, it.name, it.category, it.metal, it.purity).any { s -> s.contains(query, true) } }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            ScreenTitle("Design catalogue", "${data.designs.size} saved designs")
            Button(onClick = onAdd, colors = ButtonDefaults.buttonColors(containerColor = Burgundy)) { Icon(Icons.Default.Add, null); Text("Add") }
        }
        OutlinedTextField(query, onQuery, Modifier.fillMaxWidth(), label = { Text("Search ID, name or category") }, singleLine = true)
        Spacer(Modifier.height(10.dp))
        if (filtered.isEmpty()) Text("No designs yet. Tap Add to create one.", color = Color.Gray, modifier = Modifier.padding(8.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(9.dp)) {
            items(filtered, key = { it.id }) { d ->
                Card(colors = CardDefaults.cardColors(containerColor = Cream)) {
                    Column(Modifier.fillMaxWidth().padding(12.dp)) {
                        Text("${d.productId} · ${d.name}", fontWeight = FontWeight.Bold, color = Burgundy)
                        Text("${d.category} · ${d.metal} ${d.purity} · ${d.weight}", fontSize = 12.sp)
                        Text(if (d.available) "Available on website" else "Marked unavailable", fontSize = 12.sp, color = if (d.available) Color(0xFF267044) else Color.Gray)
                        if (d.photoUrl.isNotBlank()) Text("Photo URL saved", fontSize = 11.sp, color = Color.Gray)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TextButton(onClick = { onToggle(d) }) { Text(if (d.available) "Mark unavailable" else "Mark available") }
                            TextButton(onClick = { onDelete(d) }) { Text("Delete", color = Color.Red) }
                        }
                    }
                }
            }
        }
    }
}
@Composable
private fun DesignDialog(onDismiss: () -> Unit, onSave: (Design) -> Unit) {
    var productId by remember { mutableStateOf("") }; var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Necklaces") }; var metal by remember { mutableStateOf("Gold") }
    var purity by remember { mutableStateOf("22K") }; var weight by remember { mutableStateOf("") }
    var photo by remember { mutableStateOf("") }; var description by remember { mutableStateOf("") }
    var available by remember { mutableStateOf(true) }; var error by remember { mutableStateOf("") }
    AlertDialog(onDismissRequest = onDismiss, title = { Text("Add design") }, text = {
        Column(Modifier.heightIn(max = 520.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            OutlinedTextField(productId, { productId = it }, label = { Text("Unique product ID *") }, singleLine = true)
            OutlinedTextField(name, { name = it }, label = { Text("Design name *") }, singleLine = true)
            OutlinedTextField(category, { category = it }, label = { Text("Category") }, singleLine = true)
            OutlinedTextField(metal, { metal = it }, label = { Text("Metal (Gold/Silver)") }, singleLine = true)
            OutlinedTextField(purity, { purity = it }, label = { Text("Purity/type") }, singleLine = true)
            OutlinedTextField(weight, { weight = it }, label = { Text("Weight, e.g. 12.50 g") }, singleLine = true)
            OutlinedTextField(photo, { photo = it }, label = { Text("Photo URL (optional)") }, singleLine = true)
            OutlinedTextField(description, { description = it }, label = { Text("Description") })
            Row(verticalAlignment = Alignment.CenterVertically) { Checkbox(available, { available = it }); Text("Available on website") }
            if (error.isNotBlank()) Text(error, color = Color.Red)
        }
    }, confirmButton = { TextButton(onClick = {
        if (productId.isBlank() || name.isBlank()) error = "Product ID and name are required"
        else onSave(Design(productId = productId.trim(), name = name.trim(), category = category.trim(), metal = metal.trim(), purity = purity.trim(), weight = weight.trim(), photoUrl = photo.trim(), description = description.trim(), available = available))
    }) { Text("Save") } }, dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } })
}
@Composable
private fun BillsScreen(data: AppData, query: String, onQuery: (String) -> Unit, onNew: () -> Unit, onEdit: (Bill) -> Unit, onPayment: (Bill) -> Unit, onCancel: (Bill) -> Unit, onPdf: (Bill) -> Unit) {
    val filtered = data.bills.filter { listOf(it.number, it.customerName, it.mobile).any { s -> s.contains(query, true) } }.sortedByDescending { it.createdAt }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            ScreenTitle("Bills", "${data.bills.size} records")
            Button(onClick = onNew, colors = ButtonDefaults.buttonColors(containerColor = Burgundy)) { Icon(Icons.Default.Add, null); Text("New") }
        }
        OutlinedTextField(query, onQuery, Modifier.fillMaxWidth(), label = { Text("Search bill number, customer or mobile") }, singleLine = true)
        Spacer(Modifier.height(10.dp))
        if (filtered.isEmpty()) Text("No bills yet. Create your first bill.", color = Color.Gray)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(filtered, key = { it.id }) { b ->
                Card(colors = CardDefaults.cardColors(containerColor = if (b.cancelled) Color(0xFFF0E8E8) else Cream)) {
                    Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(b.number, fontWeight = FontWeight.Bold, color = Burgundy)
                            Text(if (b.cancelled) "CANCELLED" else if (b.balance <= 0) "PAID" else if (b.paid > 0) "PART PAID" else "UNPAID", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(b.customerName.ifBlank { "Walk-in customer" })
                        Text("${date(b.createdAt)} · ${b.items.size} item(s)", fontSize = 12.sp, color = Color.Gray)
                        Text("Total ${money(b.total)} · Paid ${money(b.paid)} · Due ${money(b.balance)}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Row {
                            TextButton(onClick = { onEdit(b) }, enabled = !b.cancelled) { Text("Edit") }
                            TextButton(onClick = { onPayment(b) }, enabled = !b.cancelled && b.balance > 0) { Text("Add payment") }
                            TextButton(onClick = { onPdf(b) }) { Text("PDF") }
                            if (!b.cancelled) TextButton(onClick = { onCancel(b) }) { Text("Cancel", color = Color.Red) }
                        }
                    }
                }
            }
        }
    }
}
@Composable
private fun BillDialog(nextNumber: Int, onDismiss: () -> Unit, onSave: (Bill) -> Unit) {
    var customer by remember { mutableStateOf("") }; var mobile by remember { mutableStateOf("") }; var address by remember { mutableStateOf("") }
    var invoiceType by remember { mutableStateOf("Simple receipt") }
    var itemText by remember { mutableStateOf("") }; var quantity by remember { mutableStateOf("1") }; var weight by remember { mutableStateOf("") }; var amountText by remember { mutableStateOf("") }
    var items by remember { mutableStateOf(listOf<LineItem>()) }; var error by remember { mutableStateOf("") }
    val total = items.sumOf { it.amount }
    AlertDialog(onDismissRequest = onDismiss, title = { Text("New bill") }, text = {
        Column(Modifier.heightIn(max = 570.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Text("Invoice no. AJ-${SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())}-${nextNumber.toString().padStart(4,'0')}", fontWeight = FontWeight.Bold, color = Burgundy)
            OutlinedTextField(customer, { customer = it }, label = { Text("Customer name") }, singleLine = true)
            OutlinedTextField(mobile, { mobile = it }, label = { Text("Mobile") }, singleLine = true)
            OutlinedTextField(address, { address = it }, label = { Text("Address") })
            Text("Invoice type", fontWeight = FontWeight.SemiBold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(invoiceType == "Simple receipt", { invoiceType = "Simple receipt" }); Text("Receipt")
                Spacer(Modifier.width(8.dp))
                RadioButton(invoiceType == "GST invoice", { invoiceType = "GST invoice" }); Text("GST invoice")
            }
            Divider()
            Text("Add items (amounts are entered manually)", fontWeight = FontWeight.SemiBold)
            OutlinedTextField(itemText, { itemText = it }, label = { Text("Item description") }, singleLine = true)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(quantity, { quantity = it }, Modifier.weight(1f), label = { Text("Quantity") }, singleLine = true)
                OutlinedTextField(weight, { weight = it }, Modifier.weight(1f), label = { Text("Weight") }, singleLine = true)
            }
            OutlinedTextField(amountText, { amountText = it }, label = { Text("Entered amount ₹") }, singleLine = true)
            OutlinedButton(onClick = {
                val amount = amountText.toDoubleOrNull()
                if (itemText.isBlank() || amount == null || amount < 0) error = "Enter item description and valid amount"
                else { items = items + LineItem(itemText.trim(), quantity.trim(), weight.trim(), amount); itemText = ""; amountText = ""; error = "" }
            }, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Default.Add, null); Text("Add item") }
            items.forEachIndexed { i, item ->
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("${i+1}. ${item.description} · ${money(item.amount)}", Modifier.weight(1f), fontSize = 12.sp)
                    IconButton(onClick = { items = items.filterIndexed { ix, _ -> ix != i } }) { Icon(Icons.Default.Delete, null, tint = Color.Red) }
                }
            }
            Text("Bill total: ${money(total)}", fontWeight = FontWeight.Bold, color = Burgundy)
            if (error.isNotBlank()) Text(error, color = Color.Red, fontSize = 12.sp)
        }
    }, confirmButton = { TextButton(onClick = {
        if (items.isEmpty()) error = "Add at least one item"
        else onSave(Bill(number = "AJ-${SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())}-${nextNumber.toString().padStart(4,'0')}", customerName = customer.trim(), mobile = mobile.trim(), address = address.trim(), invoiceType = invoiceType, items = items, total = total))
    }) { Text("Save bill") } }, dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } })
}
@Composable
private fun EditBillDialog(bill: Bill, onDismiss: () -> Unit, onSave: (Bill) -> Unit) {
    var customer by remember { mutableStateOf(bill.customerName) }
    var mobile by remember { mutableStateOf(bill.mobile) }
    var address by remember { mutableStateOf(bill.address) }
    var total by remember { mutableStateOf(bill.total.toString()) }
    var reason by remember { mutableStateOf("") }
    AlertDialog(onDismissRequest = onDismiss, title = { Text("Edit ${bill.number}") }, text = {
        Column(Modifier.heightIn(max = 420.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(customer, { customer = it }, label = { Text("Customer name") })
            OutlinedTextField(mobile, { mobile = it }, label = { Text("Mobile") })
            OutlinedTextField(address, { address = it }, label = { Text("Address") })
            OutlinedTextField(total, { total = it }, label = { Text("Corrected total ₹") })
            OutlinedTextField(reason, { reason = it }, label = { Text("Reason for change") })
            Text("Payment history is preserved. Review the new balance after saving.", color = Color.Gray, fontSize = 12.sp)
        }
    }, confirmButton = { TextButton(onClick = {
        val amount = total.toDoubleOrNull()
        if (amount != null && amount >= 0) onSave(bill.copy(customerName = customer, mobile = mobile, address = address, total = amount, changeHistory = bill.changeHistory + "Edited ${now()}: ${reason.ifBlank { "No reason entered" }}"))
    }) { Text("Save changes") } }, dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } })
}
@Composable
private fun PaymentDialog(bill: Bill, onDismiss: () -> Unit, onSave: (Payment) -> Unit) {
    var amount by remember { mutableStateOf(bill.balance.coerceAtLeast(0.0).toString()) }
    var method by remember { mutableStateOf("Cash") }
    var reference by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    val methods = listOf("Cash","UPI","Bank transfer","Card","Other")
    AlertDialog(onDismissRequest = onDismiss, title = { Text("Record payment") }, text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Outstanding: ${money(bill.balance)}")
            OutlinedTextField(amount, { amount = it }, label = { Text("Payment amount ₹") }, singleLine = true)
            methods.forEach { m -> Row(verticalAlignment = Alignment.CenterVertically) { RadioButton(method == m, { method = m }); Text(m) } }
            OutlinedTextField(reference, { reference = it }, label = { Text("Reference / note (optional)") })
            if (error.isNotBlank()) Text(error, color = Color.Red)
        }
    }, confirmButton = { TextButton(onClick = {
        val value = amount.toDoubleOrNull()
        if (value == null || value <= 0) error = "Enter a valid payment amount"
        else if (value > bill.balance + .001) error = "Payment exceeds the outstanding balance"
        else onSave(Payment(amount = value, method = method, reference = reference.trim()))
    }) { Text("Save payment") } }, dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } })
}
@Composable
private fun ReportsScreen(data: AppData) {
    val active = data.bills.filter { !it.cancelled }
    val paid = active.sumOf { it.payments.sumOf { p -> p.amount } }
    val due = active.sumOf { it.balance.coerceAtLeast(0.0) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ScreenTitle("Reports", "Based on records saved on this phone")
        SummaryCard("Total active bill value", money(active.sumOf { it.total }), Modifier.fillMaxWidth())
        SummaryCard("Total collections", money(paid), Modifier.fillMaxWidth())
        SummaryCard("Outstanding balance", money(due), Modifier.fillMaxWidth())
        SummaryCard("Cancelled bills", data.bills.count { it.cancelled }.toString(), Modifier.fillMaxWidth())
        Text("This MVP does not calculate profit or automatically apply GST calculations.", color = Color.Gray, fontSize = 12.sp)
    }
}
@Composable
private fun SettingsScreen(data: AppData, onSaveRates: (List<MetalRate>) -> Unit, onExport: () -> Unit) {
    var r22 by remember(data.rates) { mutableStateOf(data.rates.firstOrNull { it.name.startsWith("22K") }?.amount.orEmpty()) }
    var r24 by remember(data.rates) { mutableStateOf(data.rates.firstOrNull { it.name.startsWith("24K") }?.amount.orEmpty()) }
    var silver by remember(data.rates) { mutableStateOf(data.rates.firstOrNull { it.name.startsWith("Silver") }?.amount.orEmpty()) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ScreenTitle("Settings", "Rates, shop setup and local data")
        Text("Daily metal rates", fontWeight = FontWeight.Bold, color = Burgundy)
        OutlinedTextField(r22, { r22 = it }, Modifier.fillMaxWidth(), label = { Text("22K gold per gram ₹") }, singleLine = true)
        OutlinedTextField(r24, { r24 = it }, Modifier.fillMaxWidth(), label = { Text("24K gold per gram ₹") }, singleLine = true)
        OutlinedTextField(silver, { silver = it }, Modifier.fillMaxWidth(), label = { Text("Silver per gram ₹") }, singleLine = true)
        Button(onClick = {
            onSaveRates(listOf(MetalRate("22K Gold / g", r22), MetalRate("24K Gold / g", r24), MetalRate("Silver / g", silver)))
        }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Burgundy)) { Text("Save rates") }
        Divider()
        Text("Cloud sync and website publishing are not connected in this starter.", color = Color.Gray, fontSize = 12.sp)
        Text("Local data: ${data.designs.size} designs · ${data.bills.size} bills · ${data.rateHistory.size} rate records", color = Color.Gray, fontSize = 12.sp)
        OutlinedButton(onClick = onExport, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Default.Share, null); Spacer(Modifier.width(8.dp)); Text("Export local backup JSON") }
    }
}
private fun money(v: Double) = "₹" + String.format(Locale("en","IN"), "%,.2f", v)
private fun date(v: Long) = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(v))
private fun now() = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

private fun exportBackup(context: Context, data: AppData, callback: (String) -> Unit) {
    try {
        val dir = File(context.getExternalFilesDir(null), "backups")
        dir.mkdirs()
        val file = File(dir, "ashok-backup-${System.currentTimeMillis()}.json")
        file.writeText(gson.toJson(data))
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/json"
            putExtra(Intent.EXTRA_SUBJECT, "Ashok Jewellers local backup")
            putExtra(Intent.EXTRA_TEXT, "Keep this backup in a safe place.")
            putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context, context.packageName + ".fileprovider", file))
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Export backup"))
        callback("Backup export opened")
    } catch (e: Exception) { callback("Backup export failed: ${e.message}") }
}
private fun shareBillPdf(context: Context, bill: Bill, data: AppData, callback: (String) -> Unit) {
    try {
        val doc = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = doc.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint().apply { color = android.graphics.Color.rgb(109,20,37); textSize = 22f; isFakeBoldText = true }
        canvas.drawText("ASHOK JEWELLERS", 40f, 55f, paint)
        paint.color = android.graphics.Color.DKGRAY; paint.textSize = 11f; paint.isFakeBoldText = false
        canvas.drawText(if (bill.invoiceType == "GST invoice") "GST INVOICE — TEMPLATE (VERIFY BEFORE USE)" else "PAYMENT RECEIPT", 40f, 78f, paint)
        canvas.drawText("Invoice: ${bill.number}", 40f, 105f, paint)
        canvas.drawText("Date: ${date(bill.createdAt)}", 40f, 122f, paint)
        canvas.drawText("Customer: ${bill.customerName.ifBlank { "Walk-in customer" }}", 40f, 150f, paint)
        canvas.drawText("Mobile: ${bill.mobile}", 40f, 168f, paint)
        canvas.drawText("Address: ${bill.address.take(70)}", 40f, 186f, paint)
        canvas.drawLine(40f, 205f, 555f, 205f, paint)
        var y = 228f
        canvas.drawText("ITEM", 40f, y, paint); canvas.drawText("QTY", 350f, y, paint); canvas.drawText("WEIGHT", 405f, y, paint); canvas.drawText("AMOUNT", 490f, y, paint)
        y += 20f
        bill.items.forEach { item ->
            if (y > 690f) return@forEach
            canvas.drawText(item.description.take(38), 40f, y, paint)
            canvas.drawText(item.quantity.take(8), 350f, y, paint)
            canvas.drawText(item.weight.take(10), 405f, y, paint)
            canvas.drawText(String.format(Locale.US,"%.2f",item.amount), 490f, y, paint)
            y += 20f
        }
        canvas.drawLine(40f, y + 5f, 555f, y + 5f, paint)
        y += 30f
        paint.isFakeBoldText = true; paint.textSize = 14f
        canvas.drawText("TOTAL: ${money(bill.total)}", 365f, y, paint)
        y += 22f; paint.isFakeBoldText = false; paint.textSize = 11f
        canvas.drawText("Paid: ${money(bill.paid)}", 365f, y, paint)
        y += 18f; canvas.drawText("Balance due: ${money(bill.balance)}", 365f, y, paint)
        y += 35f
        canvas.drawText("Payment history:", 40f, y, paint); y += 18f
        bill.payments.forEach { p ->
            if (y < 790f) { canvas.drawText("${date(p.time)} · ${p.method} · ${money(p.amount)} ${p.reference}", 40f, y, paint); y += 16f }
        }
        if (bill.invoiceType == "GST invoice") {
            y = (y + 20f).coerceAtMost(815f)
            paint.color = android.graphics.Color.RED
            canvas.drawText("DRAFT TEMPLATE: verify GSTIN, HSN, tax values and required fields before issuing.", 40f, y, paint)
        }
        doc.finishPage(page)
        val dir = File(context.cacheDir, "invoices"); dir.mkdirs()
        val file = File(dir, "${bill.number}.pdf")
        file.outputStream().use { doc.writeTo(it) }; doc.close()
        val uri = FileProvider.getUriForFile(context, context.packageName + ".fileprovider", file)
        val send = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"; putExtra(Intent.EXTRA_SUBJECT, bill.number)
            putExtra(Intent.EXTRA_TEXT, "Ashok Jewellers invoice ${bill.number}")
            putExtra(Intent.EXTRA_STREAM, uri); addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(send, "Share invoice PDF"))
        callback("PDF ready")
    } catch (e: Exception) { callback("PDF error: ${e.message}") }
}
