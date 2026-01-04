package com.clay.ecommerce_compose.ui.components.business.stockConfig

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.clay.ecommerce_compose.R

@Composable
fun BasicField(
    value: String?,
    onValueChange: (String?) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    errorLabel: String? = null,
    minLines: Int = 1
) {
    if (value != null) {
        Column {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                minLines = minLines,
                cursorBrush = SolidColor(value = colorResource(id = R.color.black)),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontSize = 16.sp,
                            style = MaterialTheme.typography.labelSmall,
                            color = colorResource(id = R.color.coolGrey),
                            softWrap = false,
                            maxLines = 1,
                            minLines = 1,
                            lineHeight = 16.sp
                        )
                    }
                    innerTextField()
                },
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                textStyle = MaterialTheme.typography.labelSmall,
            )

            if (errorLabel != null) {
                Text(
                    text = errorLabel,
                    color = colorResource(id = R.color.tintRed),
                    fontSize = 12.sp
                )
            }
        }
    }
}
