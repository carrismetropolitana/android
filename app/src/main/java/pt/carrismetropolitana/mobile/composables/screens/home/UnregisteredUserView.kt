package pt.carrismetropolitana.mobile.composables.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UnregisteredUserView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "A Carris Metropolitana está mais próxima",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Black,
            fontSize = 24.sp,
            modifier = Modifier.padding(horizontal = 25.dp)
        )
        Text(
            "Crie uma conta para personalizar a app com as suas linhas e paragens favoritas.",
            modifier = Modifier.padding(horizontal = 12.dp),
            textAlign = TextAlign.Center,
        )
        Text(
            "Saiba exatamente onde andam todos os autocarros e exatamente quando chegam à sua paragem.",
            modifier = Modifier.padding(24.dp),
            textAlign = TextAlign.Center
        )
    }
}