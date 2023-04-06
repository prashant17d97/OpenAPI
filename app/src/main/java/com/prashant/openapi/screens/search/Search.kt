package com.prashant.openapi.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.prashant.openapi.R
import com.prashant.openapi.commonfunction.CommonFunctions.convertUtcDateToNormal
import com.prashant.openapi.screens.search.model.GithubItem


/**
 *A composable function that displays a search screen with an [OutlinedTextField] for query input and a LazyColumn to display search results.
 *@param navHostController The NavController to handle navigation events.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Search(navHostController: NavHostController) {

    // Initializing the query with empty string
    var query by remember {
        mutableStateOf("")
    }

    val searchVM: SearchVM = hiltViewModel()

    // Get the current focus manager to handle focus events
    val focusManager = LocalFocusManager.current

    // Get the current software keyboard controller to handle keyboard events
    val keyBoardControl = LocalSoftwareKeyboardController.current

    // Column composable that contains search input field and search results
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .padding(10.dp)
    ) {

        // OutlinedTextField to take input from user
        OutlinedTextField(
            value = query,
            shape = RoundedCornerShape(10.dp),
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search")
            },
            placeholder = { Text(text = "Search...") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onSearch = {
                focusManager.clearFocus(true)
                keyBoardControl?.hide()
                searchVM.query(query)
                query = ""
            })
        )
        if (searchVM.items.isEmpty()) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text(text = "Nothing available to show.  😞")
            }
        }

        // LazyColumn to display search results
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            items(searchVM.items) { item ->
                if (item != null) {
                    UserCard(item)
                }
            }
        }
    }
}

/**

 *A composable that displays a user card with details like the user's name and description, star count,

 *watchers count, score, created and updated dates.

 *@param expand a [Boolean] that indicates whether to expand the additional details of the user card

 *@param modifier optional [Modifier] to modify the layout behavior

 *@throws [Exception] if the user card details are not provided or loaded properly

 *@return a [Card] composable representing the user card with details like name, description, and other

 *details like star count, watchers count, score, created and updated dates
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserCard(item: GithubItem) {
    var expand by rememberSaveable {
        mutableStateOf(false)
    }
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                GlideImage(
                    model = item.owner?.avatarUrl ?: R.drawable.placeholder,
                    contentDescription = "",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(50.dp),
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = item.fullName ?: "",
                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(imageVector = Icons.Filled.Star, contentDescription = "Star")
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "${item.stargazersCount ?: 0}", style = MaterialTheme.typography.body1
                )
            }

            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Description:",
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = item.description ?: "N/A",
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Start
            )

            /*Stars, watchers count, score, name,created_at, updated_at.*/
            AnimatedVisibility(visible = expand) {
                Box(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colors.primary,
                            shape = RoundedCornerShape(10.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        /**Name*/
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = stringResource(id = R.string.name),
                                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = item.name ?: "", style = MaterialTheme.typography.body1
                            )

                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        /**Stars*/
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = stringResource(id = R.string.stars),
                                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "${item.stargazersCount ?: 0}",
                                style = MaterialTheme.typography.body1
                            )

                        }
                        Spacer(modifier = Modifier.height(5.dp))

                        /**Watchers*/
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = stringResource(id = R.string.watchers),
                                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "${item.watchersCount ?: 0}",
                                style = MaterialTheme.typography.body1
                            )

                        }
                        Spacer(modifier = Modifier.height(5.dp))

                        /**Score*/
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = stringResource(id = R.string.score),
                                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "${item.score ?: 0}", style = MaterialTheme.typography.body1
                            )

                        }
                        Spacer(modifier = Modifier.height(5.dp))

                        /**Created At*/
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = stringResource(id = R.string.created_at),
                                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = convertUtcDateToNormal(item.createdAt),
                                style = MaterialTheme.typography.body1
                            )

                        }
                        Spacer(modifier = Modifier.height(5.dp))

                        /**Updated At*/
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = stringResource(id = R.string.updated_at),
                                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = convertUtcDateToNormal(item.updatedAt),
                                style = MaterialTheme.typography.body1
                            )

                        }
                    }
                }
            }
            Row(verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expand = !expand }) {
                Icon(imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Star",
                    modifier = Modifier.rotate(180f.takeIf { expand } ?: 0f))
            }
        }
    }
}

@Preview
@Composable
fun SearchPrev() {
    Search(navHostController = rememberNavController())
//    UserCard()
}