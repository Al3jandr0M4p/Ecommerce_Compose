package com.clay.ecommerce_compose.ui.components.client.cart

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CartStepper(steps: List<String>, currentStep: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        steps.forEachIndexed { index, step ->
            StepItem(
                number = index + 1,
                label = step,
                isActive = index == currentStep,
                isCompleted = index < currentStep
            )

            if (index < steps.lastIndex) {
                StepLine(isCompleted = index < currentStep)
            }
        }
    }
}
