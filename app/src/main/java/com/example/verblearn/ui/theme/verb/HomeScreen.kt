package com.example.verblearn.ui.theme.verb

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.verblearn.R
import com.example.verblearn.data.remote.dto.VerbsDTO
import com.example.verblearn.ui.theme.navigation.Destination
import com.example.verblearn.ui.theme.viewModel.VerbViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@SuppressLint("UnrememberedMutableState", "UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: VerbViewModel = hiltViewModel()) {
    var searchedVerb by mutableStateOf("")
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val verbNotFound = remember { mutableStateOf(false) }
    var active by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        )
        {
            HeaderScreen()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            {
                Row {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    )
                    {
                        SearchBar(
                            query = searchedVerb,
                            onQueryChange = { searchedVerb = it },
                            onSearch = {
                                active = false
                                val verb = uiState.verbs.singleOrNull {
                                    it.baseForm.lowercase() == searchedVerb.lowercase() ||
                                            it.pastParticiple.lowercase() == searchedVerb.lowercase() ||
                                            it.simplePast.lowercase() == searchedVerb.lowercase() ||
                                            it.spanishBaseForm.lowercase() == searchedVerb.lowercase() ||
                                            it.spanishPastParticiple.lowercase() == searchedVerb.lowercase() ||
                                            it.spanishSimplePast.lowercase() == searchedVerb.lowercase()
                                }

                                handleSearch(verb,navController, verbNotFound)
                            },
                            active = active,
                            onActiveChange = { active = it },
                            placeholder = { Text(text = "Type the verb") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = "Icono de buscar",
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.ArrowForward,
                                    contentDescription = "Icono hacia la derecha",
                                    Modifier.clickable(onClick = {
                                        val verb = uiState.verbs.singleOrNull {
                                            it.baseForm.lowercase() == searchedVerb.lowercase() ||
                                                    it.pastParticiple.lowercase() == searchedVerb.lowercase() ||
                                                    it.simplePast.lowercase() == searchedVerb.lowercase() ||
                                                    it.spanishBaseForm.lowercase() == searchedVerb.lowercase() ||
                                                    it.spanishPastParticiple.lowercase() == searchedVerb.lowercase() ||
                                                    it.spanishSimplePast.lowercase() == searchedVerb.lowercase()
                                        }

                                        handleSearch(verb, navController, verbNotFound)
                                    })
                                )
                            },
                        ) {
                            if (searchedVerb.isNotEmpty()) {
                                val filterVerbs = uiState.verbs.filter {
                                    it.baseForm.contains(searchedVerb, true) ||
                                            it.pastParticiple.contains(searchedVerb, true) ||
                                            it.simplePast.contains(searchedVerb, true) ||
                                            it.spanishBaseForm.contains(searchedVerb, true) ||
                                            it.spanishPastParticiple.contains(searchedVerb, true) ||
                                            it.spanishSimplePast.contains(searchedVerb, true)
                                }

                                LazyColumn {
                                    items(filterVerbs) { verb ->
                                        if(!verb.verbProposal){
                                            val matchingForm = when {
                                                verb.baseForm.contains(searchedVerb, true) -> verb.baseForm
                                                verb.pastParticiple.contains(searchedVerb, true) -> verb.pastParticiple
                                                verb.simplePast.contains(searchedVerb, true) -> verb.simplePast
                                                verb.spanishBaseForm.contains(searchedVerb, true) -> verb.spanishBaseForm
                                                verb.spanishPastParticiple.contains(searchedVerb, true) -> verb.spanishPastParticiple
                                                verb.spanishSimplePast.contains(searchedVerb, true) -> verb.spanishSimplePast
                                                else -> "" // Add other cases for Spanish forms if needed
                                            }

                                            Text(
                                                text = matchingForm,
                                                modifier = Modifier
                                                    .padding(10.dp)
                                                    .clickable {
                                                        handleSearch(verb, navController, verbNotFound)
                                                        active = false
                                                    }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        if (verbNotFound.value) {
                            Text(
                                text = "The verb to search is misspelled or is not found in the database",
                                color = Color.Red
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(60.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable._223),
                        contentDescription = "Descripción de la imagen",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.size(220.dp)

                    )
                }

            }
        }
    }
}


fun handleSearch(verb: VerbsDTO?, navController: NavController, verbNotFound: MutableState<Boolean>) {
    if (verb != null && !verb.verbProposal) {
        navController.navigate("${Destination.Translate.route}/${verb.id}")
        verbNotFound.value = false
    } else {
        verbNotFound.value = true
    }
}