package com.example.lab_11_sec2version2

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController

@Composable
fun HomeScreen(navController: NavHostController) {
    var employeeItemList = remember { mutableStateListOf<Employee>() }
    val contextForToast = LocalContext.current.applicationContext
    var textFieldID by remember { mutableStateOf("") }

    ////// Check Lifecycle State
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()
    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.DESTROYED -> {}
            Lifecycle.State.INITIALIZED -> {}
            Lifecycle.State.CREATED -> {}
            Lifecycle.State.STARTED -> {}
            Lifecycle.State.RESUMED -> {
                showAllData(employeeItemList, contextForToast)
            } /// End RESUMED
        }
    }
    ///////
    Column {
        Row (
            Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            //Row
            Text(text = "Search:",
                fontSize = 20.sp
            )
            OutlinedTextField(
                modifier = Modifier
                    .width(230.dp)
                    .padding(10.dp),
                value = textFieldID,
                onValueChange = { textFieldID = it },
                label = { Text("Employee ID") }
            )
            Button(onClick = {}) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
            //Row
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //Row
            Column(modifier = Modifier.weight(0.85f))
            {
                Text(
                    text = "Employee Lists: ${employeeItemList.size} ",
                    fontSize = 25.sp
                )
            }
            Button(onClick = {
                navController.navigate(Screen.Insert.route)
            }) {
                Text("Add Employee")
            }
            //Row
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            var itemClick = Employee("","",0,"")
            itemsIndexed(
                items = employeeItemList,
            ) { index, item ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .height(130.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    ),
                    shape = RoundedCornerShape(corner = CornerSize(16.dp)),
                    onClick = {
                        Toast.makeText(
                            contextForToast, "Click on ${item.emp_name}.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                ) {
                    Row(
                        Modifier.fillMaxWidth()
                            .height(Dp(130f))
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "ID: ${item.emp_id}\n" +
                                "Name: ${item.emp_name}\n" +
                                "Salary: ${item.emp_salary}\n" +
                                "Position: ${item.emp_position}",
                            Modifier.weight(0.85f)
                        )
                        TextButton(onClick = {
                            itemClick = item
                            navController.currentBackStackEntry?.savedStateHandle?.set( "data",
                                Employee(item.emp_id, item.emp_name,item.emp_salary,item.emp_position)
                            )
                            navController.navigate(Screen.Edit.route)
                        }  )
                        { Text("Edit/Delete") }
                    }
                }
            }
        }
    }
}

fun showAllData(employeeItemLists:MutableList<Employee>, context: Context){
    val dbHandler= DatabaseHelper.getInstance(context)
    dbHandler.writableDatabase
    employeeItemLists.clear()
    employeeItemLists.addAll(dbHandler.getAllEmployee())
}