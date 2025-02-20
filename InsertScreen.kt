package com.example.lab_11_sec2version2

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlin.math.exp

@Composable
fun InsertScreen(navController: NavHostController) {
    var textFieldID by remember { mutableStateOf("") }
    var textFieldName by remember { mutableStateOf("") }
    var textFieldSalary by remember { mutableStateOf("") }
    val contextForToast = LocalContext.current
    var selectedPosition by remember { mutableStateOf("") }
    var isButtonEnabled by remember { mutableStateOf(false) }

    val dbHandler= DatabaseHelper.getInstance(contextForToast)
    dbHandler.writableDatabase

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "Insert New Employee",
            fontSize = 25.sp
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            value = textFieldID,
            onValueChange = { textFieldID = it
                isButtonEnabled = validateInput(textFieldID, textFieldName,
                    selectedPosition, textFieldSalary)},
            label = { Text("Employee ID") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            value = textFieldName,
            onValueChange = { textFieldName = it },
            label = { Text("Employee Name") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )
        selectedPosition =  MyDropdown()
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldSalary,
            onValueChange = { textFieldSalary = it
                isButtonEnabled = validateInput(textFieldID, textFieldName,
                    selectedPosition, textFieldSalary)},
            label = { Text("Employee Salary") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done)
        )


        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(modifier = Modifier
                .width(130.dp),
                onClick = {
                    val result = dbHandler.insertEmployee(
                        Employee(textFieldID, textFieldName,
                            textFieldSalary.toInt(), selectedPosition)
                    )
                    if(result > -1){
                        Toast.makeText(contextForToast,"The student is inserted successfully",
                            Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(contextForToast,"Insert Failure",
                            Toast.LENGTH_LONG).show()
                    }
                    navController.navigateUp()
                },
                enabled = isButtonEnabled,
            ) {
                Text("Save")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(modifier = Modifier
                .width(130.dp),
                onClick = {
                    textFieldID=""
                    textFieldName = ""
                    navController.navigate(Screen.Home.route)
                },
            ) {
                Text("Cancel")

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDropdown():String {
    val positions = listOf("Front end developer", "Back end developer","Full Stack developer" ,"DevOps")
    var expanded by remember { mutableStateOf(false) }
    var selectedPosition by remember { mutableStateOf(positions[0]) }
    val contextForToast = LocalContext.current.applicationContext
    Text(
        text = "Employee Position:",
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
            .padding(start = 16.dp, top = 10.dp),
    )
    ExposedDropdownMenuBox(
        expanded = true,
        modifier = Modifier
            .padding(top = 8.dp),
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor(),
            readOnly = true,
            value = selectedPosition,
            onValueChange = {},
            label = { Text("Position")},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)},
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        //Menu
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier.fillMaxWidth()
        ){
            positions.forEach{ selectedOption ->
                DropdownMenuItem(
                    text = {Text(selectedOption)},
                    onClick = {
                        selectedPosition = selectedOption
                        expanded = false
                        Toast.makeText(contextForToast, "Item is $selectedPosition", Toast.LENGTH_LONG).show()
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )

            }
        }
    }
    return selectedPosition
}

fun validateInput(emp_id: String, emp_name: String, emp_salary: String, emp_position:String)
        : Boolean  {
    return emp_id.isNotEmpty() && emp_name.isNotEmpty()
            && emp_salary.isNotEmpty() && emp_position.isNotEmpty()
}