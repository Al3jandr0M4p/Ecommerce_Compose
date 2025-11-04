package com.clay.ecommerce_compose.ui.components.auth.business

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.clay.ecommerce_compose.R

@Composable
fun ImageInput(
    modifier: Modifier = Modifier,
    onImageSelected: (Uri?) -> Unit
) {
    var selectedImage by remember { mutableStateOf<Uri?>(value = null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        selectedImage = uri
        onImageSelected(uri)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 200.dp)
            .clip(shape = RoundedCornerShape(size = 16.dp))
            .border(
                width = 1.dp,
                color = colorResource(id = R.color.coolGrey).copy(alpha = 0.3f),
                shape = RoundedCornerShape(size = 16.dp)
            )
            .background(color = colorResource(id = R.color.coolGrey).copy(alpha = 0.1f))
            .clickable {
                photoPickerLauncher.launch(
                    input = PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
            .aspectRatio(ratio = 1.6f),
        contentAlignment = Alignment.Center
    ) {
        if (selectedImage != null) {
            AsyncImage(
                model = selectedImage,
                contentDescription = "Imagen seleccionada",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(size = 16.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = "Logo del negocio",
                color = colorResource(id = R.color.coolGrey),
                fontSize = 16.sp,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}
