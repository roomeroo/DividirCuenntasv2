package es.adrian.dividircuenta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.adrian.dividircuenta.ui.theme.DividirCuentaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DividirCuentaTheme {
                Surface{
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.padding(top = 50.dp)
                    ) {
                        var cantidad by remember { mutableStateOf("") }
                        var comensales by remember { mutableStateOf("") }
                        var resultado by remember { mutableStateOf(0f) }
                        var datosCorrectos by remember { mutableStateOf(false) }
                        var hayPropina by remember { mutableStateOf(false) }
                        var valorSlider by remember {  mutableStateOf(0f)}
                        var mostrarDatos by remember { mutableStateOf(false) }


                        if(comensales != "" && cantidad != ""){
                            datosCorrectos = true;
                        }else {
                            datosCorrectos = false;
                            resultado = 0f;
                        }

                        crearInput(
                            name = "Cantidad",
                            valor = cantidad,
                            funcion = {
                                introducido -> cantidad = introducido
                                mostrarDatos = false
                            }
                        );

                        crearInput(
                            name = "Comensales",
                            valor = comensales,
                            funcion = {
                                introducido -> comensales = introducido
                                mostrarDatos = false
                            }
                        );

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(50.dp  )

                            ) {

                                Text(
                                    "Redondear propina",
                                    fontSize = 24.sp
                                )

                                crearSwitch(
                                    checked = hayPropina,
                                    listener = { checked -> hayPropina = checked }
                                )
                            }
                        }

                        crearSlider(
                            enabled = hayPropina,
                            value = valorSlider,
                            listener = {
                                valorSlider = it
                                mostrarDatos = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        crearBoton(
                            listener = {
                                resultado = cantidad.toFloat()
                                if(hayPropina){
                                    resultado += resultado * (valorSlider * 0.05f)
                                }

                                mostrarDatos = true
                            },
                            enabled = datosCorrectos,
                            modifier = Modifier.fillMaxWidth()
                        )

                        if(mostrarDatos){
                            mostrarResultado(resultado, comensales.toInt())
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun crearInput(name:String, valor: String, funcion: (String)->Unit){
    OutlinedTextField(
        value = valor,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(name) },
        onValueChange = {introducido -> funcion(introducido)},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun crearBoton(
    listener :()->Unit = {},
    texto :String = "Calcular",
    enabled : Boolean = false,
    modifier: Modifier = Modifier,

){
    Button(
        onClick = { listener() },
        enabled = enabled,
        content = {Text(texto)},
        modifier = modifier
    );
}

@Composable
fun crearSwitch(
    checked: Boolean,
    listener: (Boolean) -> Unit
) {
    Switch(
        checked = checked,
        onCheckedChange = listener
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun crearSlider(
    enabled :Boolean = false,
    value: Float = 0f,
    listener: (Float) -> Unit,
    modifier: Modifier = Modifier
){
    Slider(
        value = value,
        onValueChange = listener,
        enabled = enabled,
        modifier = modifier,
        valueRange = 0f..5f,
        steps = 4

    )
}

@Composable
fun mostrarResultado(total: Float, comensales : Int){
    var repartido = total.div(comensales)
    Text(
        text = "Total a pagar: $total",
        fontSize = 35.sp
    )
    Text(
        text="Por persona: $repartido",
        fontSize = 35.sp
    )
}