package com.clay.ecommerce_compose.screens.cliente

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.clay.ecommerce_compose.Tabs
import com.clay.ecommerce_compose.data.getCategoriesList
import com.clay.ecommerce_compose.data.getChips
import com.clay.ecommerce_compose.data.getRestaurants


/**
 *
 *
 * Composable del header el cual me permite renderizar
 * y crear un Header adaptable al usuario y especifico
 * para mi aplicacion
 *
 * @author Alejandro
 * */
@Composable
fun HeaderUserHome() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        Text(
            text = "Tu ubicacion",
            style = MaterialTheme.typography.labelMedium,
            fontSize = 20.sp,
            modifier = Modifier.clickable {
                /* TODO not yet implemented */
            })

        Row {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "User Perfil",
                    modifier = Modifier
                        .size(size = 30.dp)
                )
            }
            IconButton(onClick = {}) {
                Box(
                    modifier = Modifier.size(size = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ShoppingCart,
                        contentDescription = "Cart Shopping",
                        modifier = Modifier.size(size = 60.dp)
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 6.dp, y = (-6).dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(size = 18.dp)
                                .background(
                                    color = Color(color = 0xff0e8244),
                                    shape = RoundedCornerShape(size = 12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "1",
                                color = Color.White,
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 *
 * Composable de la SearchBar maneja la Searchbar
 * sus estilos y comming soon will be managging the
 * logic about all
 *
 * */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    var query by remember { mutableStateOf(value = "") }

    TextField(
        value = query,
        onValueChange = { query = it },
        placeholder = {
            Text(
                text = "Buscar...",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 18.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search, contentDescription = null
            )
        },
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(color = 0xFFF2F2F2),
            unfocusedContainerColor = Color(color = 0xFFF2F2F2),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(height = 56.dp),
        shape = CircleShape
    )
}


/**
 *
 * Composable de categorias para manejar las categorias
 * y la logica de ellas
 *
 * */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Categories() {
    val categories = getCategoriesList().take(4)
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    var showSheet by remember { mutableStateOf(value = false) }

    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        items(items = categories) { item ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable {
                        /* TODO not yet implemented */
                    },
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 20.dp)
                ) {
                    Image(
                        painter = painterResource(id = item.img),
                        contentDescription = item.title,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(size = 58.dp)
                            .clip(shape = CircleShape)
                            .align(Alignment.BottomEnd)
                    )
                }

                Text(
                    text = item.title,
                    fontSize = 12.sp,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center
                )
            }

        }

        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clickable {
                        /* TODO not yet implemented */
                    },
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 20.dp)
                ) {
                    IconButton(
                        onClick = { showSheet = true },
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = "Categorias",
                            modifier = Modifier
                                .size(size = 48.dp)
                        )
                    }
                }
                Text(
                    text = "Categorias",
                    fontSize = 14.sp,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleSmall
                )
            }

        }

        item {
            if (showSheet) {
                LaunchedEffect(Unit) {
                    sheetState.expand()
                }

                ModalBottomSheet(
                    onDismissRequest = { showSheet = false },
                    sheetState = sheetState,
                    dragHandle = { BottomSheetDefaults.DragHandle() }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(fraction = 0.9f)
                            .padding(all = 24.dp)
                    ) {
                        Column {
                            Text(
                                text = "Mas categorias",
                                fontSize = 28.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp, horizontal = 10.dp)
                            )
                            /*
                            *
                            * si decido crear un boton para que el usuario pueda cerrar
                            * el modal desde el mismo le paso a AllCategories(onClose: () -> Unit = {})
                            *
                            * y al llamarla
                            *
                            * AllCategories() {
                            *     showSheet = false
                            * }
                            *
                            * */
                            AllCategories()
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun AllCategories() {
    val categoryList = getCategoriesList()

    LazyVerticalGrid(
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
        columns = GridCells.Adaptive(minSize = 80.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(all = 8.dp)
    ) {
        items(items = categoryList) { item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(size = 16.dp))
                    .clickable {
                        /* TODO not yet implemented */
                    }
                    .padding(all = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = item.img),
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(size = 50.dp)
                        .clip(shape = RoundedCornerShape(size = 8.dp))
                )
                Text(
                    text = item.title,
                    fontSize = 14.sp,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

/** Composable de Chips  */
@Composable
fun Chips() {
    val chips = getChips()

    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(items = chips) { chip ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(width = 100.dp)
                    .height(height = 36.dp)
                    .clickable {
                        /* TODO not yet implemented */
                    }
                    .background(color = Color(color = 0xfff2f2f2), shape = CircleShape)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = chip.icon,
                        contentDescription = null
                    )
                    Text(
                        text = chip.title,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedCardTransition(navController: NavHostController) {
    val getRestaurants = getRestaurants()

    getRestaurants.forEach { restaurant ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
        ) {
            Image(
                painter = painterResource(id = restaurant.imgUrl),
                contentDescription = restaurant.description,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 200.dp)
                    .clip(shape = RoundedCornerShape(size = 14.dp))
                    .clickable {
                        navController.navigate(route = "details/${restaurant.id}")
                    }
            )

            Text(
                text = restaurant.title,
                style = MaterialTheme.typography.labelMedium,
                fontSize = 20.sp
            )

        }
    }

}

@Composable
fun Home(navController: NavHostController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item { HeaderUserHome() }

        item { SearchBar() }

        item { Categories() }

        item { Spacer(modifier = Modifier.height(height = 16.dp)) }

        item { Chips() }

        item { SharedCardTransition(navController = navController) }
    }
}


/**
 *
 * Composable de la UserHomeScreen
 *
 * @param modifier para manejar el estilo de los composable
 *
 * @author Alejandro
 *
 * */
@Composable
fun UserHomeScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    var selectedTab by remember { mutableStateOf<Tabs>(value = Tabs.Home) }
    val tabs = listOf<Tabs>(Tabs.Home)

    Scaffold(
        bottomBar = {
            BottomAppBar(
                contentPadding = PaddingValues()
            ) {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        icon = {
                            Icon(imageVector = tab.icon, contentDescription = tab.title)
                        },
                        label = {
                            Text(
                                text = tab.title,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 16.sp
                            )
                        },
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab }
                    )
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                is Tabs.Home -> Home(navController = navController)
            }
        }
    }
}
