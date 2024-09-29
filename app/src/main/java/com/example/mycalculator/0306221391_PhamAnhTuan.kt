package com.example.mycalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mycalculator.ui.theme.MyCalculatorTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyCalculatorTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "My Calculator",
                                    color = Color.White
                                )
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = Color.Blue,
                                titleContentColor = Color.White
                            )
                        )
                    }
                ) { innerPadding ->
                    GiaoDienMayTinh(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun GiaoDienMayTinh(modifier: Modifier = Modifier) {
    var soThuNhat by remember { mutableStateOf("") }
    var soThuHai by remember { mutableStateOf("") }
    var phepToan by remember { mutableStateOf("") }
    var ketQua by remember { mutableStateOf("") }
    var errorMessageSoThuNhat by remember { mutableStateOf("") }
    var errorMessageSoThuHai by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var globalErrorMessage by remember { mutableStateOf("") }


    fun isValidNumber(input: String): Boolean {
        return input.toDoubleOrNull() != null
    }


    fun clearInputs() {
        soThuNhat = ""
        soThuHai = ""
        phepToan = ""
        ketQua = ""
        errorMessageSoThuNhat = ""
        errorMessageSoThuHai = ""
        globalErrorMessage = ""
    }


    fun tinhToan() {
        val num1 = soThuNhat.toDoubleOrNull()
        val num2 = soThuHai.toDoubleOrNull()


        if (num1 == null || num2 == null || phepToan.isEmpty()) {
            globalErrorMessage = "Lỗi: Số thứ nhất và số thứ hai phải là số hợp lệ"
            showErrorDialog = true
            return
        }

        ketQua = when (phepToan) {
            "+" -> "${num1 + num2}"
            "-" -> "${num1 - num2}"
            "*" -> "${num1 * num2}"
            "/" -> {
                if (num2 == 0.0) {
                    globalErrorMessage = "Lỗi: không thể chia cho 0"
                    showErrorDialog = true
                    return
                } else {
                    "${num1 / num2}"
                }
            }
            else -> ""
        }

        ketQua = "$num1 $phepToan $num2 = $ketQua"
    }


    fun onPhepToanClicked(phepToanMoi: String) {
        if (soThuNhat.isEmpty()) {
            errorMessageSoThuNhat = "Số thứ nhất không được để trống"
        } else if (!isValidNumber(soThuNhat)) {
            errorMessageSoThuNhat = "Số thứ nhất phải là số hợp lệ"
        } else {
            errorMessageSoThuNhat = ""
        }


        if (soThuHai.isEmpty()) {
            errorMessageSoThuHai = "Số thứ hai không được để trống"
        } else if (!isValidNumber(soThuHai)) {
            errorMessageSoThuHai = "Số thứ hai phải là số hợp lệ"
        } else {
            errorMessageSoThuHai = ""
        }


        if (errorMessageSoThuNhat.isEmpty() && errorMessageSoThuHai.isEmpty()) {
            phepToan = phepToanMoi
            tinhToan()
        } else {
            ketQua = "Kết quả: "
        }
    }

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(

        ) {
            // Nhập số thứ nhất
            TextField(
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp,8.dp),
                label = { Text("Hãy nhập số thứ nhất") },
                value = soThuNhat,
                onValueChange = {
                    soThuNhat = it
                    errorMessageSoThuNhat = ""
                }
            )
            // Hiển thị thông báo lỗi cho số thứ nhất
            if (errorMessageSoThuNhat.isNotEmpty()) {
                Text(
                    text = errorMessageSoThuNhat,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Nhập số thứ hai
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp,8.dp),
                label = { Text("Hãy nhập số thứ hai") },
                value = soThuHai,
                onValueChange = {
                    soThuHai = it
                    errorMessageSoThuHai = ""
                }
            )
            // Hiển thị thông báo lỗi cho số thứ hai
            if (errorMessageSoThuHai.isNotEmpty()) {
                Text(
                    text = errorMessageSoThuHai,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Các nút phép toán
            Row(
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(onClick = { onPhepToanClicked("+") }) {
                    Text("+")
                }
                TextButton(onClick = { onPhepToanClicked("-") }) {
                    Text("-")
                }
                TextButton(onClick = { onPhepToanClicked("*") }) {
                    Text("*")
                }
                TextButton(onClick = { onPhepToanClicked("/") }) {
                    Text("/")
                }
            }

            // Nút Clear
            Button(
                onClick = { clearInputs() },
                modifier = Modifier.align(CenterHorizontally)
            ) {
                Text("Clear")
            }

            // Hiển thị kết quả (luôn hiển thị)
            Text(
                text = "Kết quả: $ketQua",
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(top = 16.dp)
            )

            // Popup lỗi khi có lỗi tổng quát
            if (showErrorDialog) {
                AlertDialog(
                    onDismissRequest = { showErrorDialog = false },
                    confirmButton = {
                        TextButton(
                            onClick = { showErrorDialog = false }
                        ) {
                            Text("OK")
                        }
                    },
                    title = { Text("Thông báo lỗi") },
                    text = { Text(globalErrorMessage) }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GiaoDienMayTinhPreview() {
    MyCalculatorTheme {
        GiaoDienMayTinh()
    }
}
