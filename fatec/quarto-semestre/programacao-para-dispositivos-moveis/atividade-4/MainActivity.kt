package com.example.jetpackbasics

import Pessoa
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jetpackbasics.data.repository.DatabaseHelper
import com.example.jetpackbasics.ui.theme.JetpackBasicsTheme
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackBasicsTheme {
                val database = DatabaseHelper(this)
                val list = remember { mutableStateListOf<Pessoa>() }
                if (list.isEmpty())
                    list.addAll(database.readPessoas())


                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AddName(database, list)
                    ShowList(list)
                }
            }
        }
    }
}


@Composable
fun AddName(database: DatabaseHelper, list: SnapshotStateList<Pessoa>) {
    var name by remember { mutableStateOf("") }
    var idade by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.5f),
            shape = RoundedCornerShape(25.dp),
            value = name,
            placeholder = { Text("Nome") },
            onValueChange = { newText ->
                name = newText
            })

        Spacer(modifier = Modifier.height(5.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.5f),
            shape = RoundedCornerShape(25.dp),
            value = idade,
            placeholder = { Text("18") },
            onValueChange = { newText ->
                idade = newText
            })

        Spacer(modifier = Modifier.height(5.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.5f),
            shape = RoundedCornerShape(25.dp),
            value = altura,
            placeholder = { Text("1.80") },
            onValueChange = { newText ->
                altura = newText
            })

        Spacer(modifier = Modifier.height(5.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.5f),
            shape = RoundedCornerShape(25.dp),
            value = peso,
            placeholder = { Text("80.5") },
            onValueChange = { newText ->
                peso = newText
            })

        Spacer(modifier = Modifier.height(5.dp))

        Button(
            modifier = Modifier.fillMaxWidth(0.5f),
            onClick = {
                val pessoa = Pessoa(name, idade.toInt(), altura.toDouble(), peso.toDouble())
                database.createPessoa(pessoa)
                list.add(pessoa)

                name = ""
                idade = ""
                altura = ""
                peso = ""
            }) {
            Text("Adicionar")
        }
    }
}

@Composable
fun ShowList(list: List<Pessoa>) {


    Spacer(modifier = Modifier.height(50.dp))
    HorizontalDivider()
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        items(list) { item ->
            Text(
                """${item.nome}, ${item.idade} anos, ${item.altura}m, ${item.peso}kg.
                    IMC = ${
                    BigDecimal(item.peso / (item.altura * item.altura)).setScale(
                        2,
                        RoundingMode.HALF_EVEN
                    )
                }""".trim(),
                modifier = Modifier.padding(5.dp)
            )
            HorizontalDivider()
        }
    }
}
