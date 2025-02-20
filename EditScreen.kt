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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TabPosition
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun EditScreen(navController: NavHostController) {
    val data =
        navController.previousBackStackEntry?.savedStateHandle?.get<Employee>("data")
            ?: Employee("", "", 0,""  )

    ///ให้นศ.เขียนคำสั่งเอง
    var textFieldID by remember { mutableStateOf(data.emp_id) }
    val contextForToast = LocalContext.current
    var positionValue by remember { mutableStateOf(data.emp_position) }

    ////ให้นศ.เพิ่ม
    var textFieldName by remember { mutableStateOf(data.emp_name) }
    var textFieldSalary by remember { mutableStateOf(data.emp_salary.toString()) }

    // For Alert Dialog: Confirm to delete Student
    var deleteDialog by remember { mutableStateOf(false) }

    val dbHandler= DatabaseHelper.getInstance(contextForToast)
    dbHandler.writableDatabase

    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "Edit a employee",
            fontSize = 25.sp
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldID,
            onValueChange = { textFieldID = it },
            label = { Text("Employee ID") },
            enabled = false,
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldName,
            onValueChange = { textFieldName = it },
            label = { Text("Employee name") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )

        positionValue  = EditMyDropdown(data.emp_position)

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldSalary,
            onValueChange = { textFieldSalary = it },
            label = { Text("Salary") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done),
        )
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        )
        {
            Button(modifier = Modifier
                .width(100.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                onClick = {
                    deleteDialog = true
                }) {
                Text("Delete")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(modifier = Modifier
                .width(100.dp),
                onClick = {
                    //Update Lab 2
                    val result = dbHandler.updateEmployee(
                        Employee(textFieldID,textFieldName , textFieldSalary.toInt(),positionValue))

                    if (result > -1) {
                        Toast.makeText(contextForToast, "Updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(contextForToast, "Update Failure", Toast.LENGTH_LONG).show()  }
                    navController.navigate(Screen.Home.route)
                }) {
                Text("Update")
            }
            Spacer(modifier = Modifier.width(10.dp))

            Button(modifier = Modifier
                .width(100.dp),
                onClick = {
                    navController.navigate(Screen.Home.route)
                }) {
                Text("Cancel")
            }
        }


        ///////

        if (deleteDialog) {
            AlertDialog(
                onDismissRequest = {
                    deleteDialog = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            deleteDialog = false

                            val result = dbHandler.deleteEmployee(textFieldID)
                            if (result > -1) {
                                Toast.makeText(contextForToast, "Deleted successfully",
                                    Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(contextForToast, "Delete Failure",
                                    Toast.LENGTH_LONG).show()
                            }

                            Toast.makeText(contextForToast,
                                " $textFieldID is deleted",
                                Toast.LENGTH_SHORT).show()
                            navController.navigate(Screen.Home.route)
                        }
                    ) {
                        Text(text = "Yes")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            deleteDialog = false
                            Toast.makeText(contextForToast,
                                "Click on No", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text(text = "No")
                    }
                },
                title = { Text(text = "Warning") },
                text = { Text(text = "Do you want to delete a student: ${textFieldID}?") },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }/// End delete

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMyDropdown(
    position:String
):String{
    val positions = listOf("Front end developer", "Back end developer","Full Stack developer" ,"DevOps")
    var expanded by remember { mutableStateOf(false) }
    var selectedPosition by remember { mutableStateOf(position) }
    val contextForToast = LocalContext.current.applicationContext
    Text(
        text = "Employee Position :",
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
            .padding(start = 16.dp, top = 10.dp),
    )
    ExposedDropdownMenuBox(
        expanded = true,
        modifier = Modifier,
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
            }
        ){
            positions.forEachIndexed{ index,selectedOption ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = selectedOption,
                        )
                           },
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



//@Composable
//fun EditRadioGroupUsage(s:String):String {
//    val kinds = listOf("Male", "Female", "Other")
//    val (selected, setSelected) = remember { mutableStateOf(s) }
//    Text(
//        text = "Student Gender :",
//        textAlign = TextAlign.Start,
//        modifier = Modifier.fillMaxWidth()
//            .padding(start = 16.dp, top = 10.dp),
//    )
//    Row (modifier = Modifier.fillMaxWidth()
//        .padding(start = 16.dp)){
//        EditeRadioGroup(
//            mItems = kinds,
//            selected, setSelected
//        )
//    }
//    return selected
//}

//@Composable
//fun EditeRadioGroup(
//    mItems: List<String>,
//    selected: String,
//    setSelected: (selected: String) -> Unit,
//) {
//    Row( modifier = Modifier.fillMaxWidth(),
//    ) {
//        mItems.forEach { item ->
//            Row(
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                RadioButton(
//                    selected = selected == item,
//                    onClick = {
//                        setSelected(item)
//                    },
//                    enabled = true,
//                    colors = RadioButtonDefaults.colors(
//                        selectedColor = Color.Green )
//                )
//                Text(text = item, modifier = Modifier.padding(start = 5.dp))
//            }
//        }
//    }
//}