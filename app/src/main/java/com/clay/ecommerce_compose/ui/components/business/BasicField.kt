package com.clay.ecommerce_compose.ui.components.business

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import com.clay.ecommerce_compose.R

@Composable
fun BasicField(value: String?, onValueChange: (String?) -> Unit, placeholder: String) {
    if (value != null) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            cursorBrush = SolidColor(colorResource(id = R.color.black)),
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
            textStyle = MaterialTheme.typography.labelSmall,
        )
    }
}
