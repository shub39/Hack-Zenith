package shub39.hackzenith.findin.presentation.main_page

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.coil.CoilImage
import shub39.hackzenith.findin.R
import shub39.hackzenith.findin.domain.Location
import shub39.hackzenith.findin.domain.LocationDto
import shub39.hackzenith.findin.domain.Post
import shub39.hackzenith.findin.presentation.auth.MainPageAction

sealed interface Routes {
    data object Home : Routes
    data object Profile : Routes
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(
    modifier: Modifier = Modifier,
    state: MainPageState,
    onAction: (MainPageAction) -> Unit
) {
    var currentScreen by remember { mutableStateOf<Routes>(Routes.Home) }
    var showPostMaker by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = { TopBar() },
        bottomBar = {
            BottomNavBar(
                currentRoute = currentScreen,
                onRouteChange = { currentScreen = it }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showPostMaker = true },
                shape = CircleShape
            ) {
                Icon(painterResource(R.drawable.add), null)
            }
        }
    ) { paddingValues ->
        AnimatedContent(targetState = currentScreen) {
            Column(modifier = Modifier.padding(paddingValues)) {
                when (it) {
                    Routes.Home -> PostList(posts = state.posts)
                    Routes.Profile -> {}
                }
            }
        }
    }

    if (showPostMaker) {
        ModalBottomSheet(onDismissRequest = { showPostMaker = false }) {
            CreatePostForm(
                onDismiss = { showPostMaker = false },
                onSubmit = { type, title, description, location, tags, images ->
                    onAction(
                        MainPageAction.PostContent(
                            types = type,
                            title = title,
                            description = description,
                            location = LocationDto(
                                place = location.place,
                                area = location.area
                            ),
                            tags = tags,
                            images = images
                        )
                    )
                    showPostMaker = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        title = {
            Text(
                text = "FindIn",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(painterResource(R.drawable.notifications), null)
            }
        }
    )
}

@Composable
fun PostList(posts: List<Post>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(posts) { post ->
            PostCard(post)
        }
    }
}

@Composable
fun PostCard(post: Post) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CoilImage(
                    imageModel = { post.user.avatar },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )

                Spacer(Modifier.width(8.dp))

                Column {
                    Text(
                        text = post.user.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = post.location.area,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Spacer(Modifier.weight(1f))

                Text(
                    text = post.types.uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (post.types == "lost") Color(0xFFD32F2F) else Color(0xFF1976D2),
                    modifier = Modifier
                        .background(
                            if (post.types == "lost")
                                Color(0xFFFDECEA)
                            else
                                Color(0xFFE3F2FD),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            post.images.firstOrNull()?.let {
                CoilImage(
                    imageModel = { it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp)),
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = post.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = post.location.place,
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Spacer(Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    post.tags.forEach { tag ->
                        Text(
                            text = "#$tag",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier
                                .background(
                                    Color(0xFFF2F2F2),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painterResource(R.drawable.share),
                        contentDescription = null,
                        tint = Color.Gray
                    )

                    Spacer(Modifier.weight(1f))

                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(if (post.types == "lost") "Contact" else "Details")
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavBar(
    currentRoute: Routes,
    onRouteChange: (Routes) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(painterResource(R.drawable.home), null) },
            label = { Text("Home", fontSize = 11.sp) },
            selected = currentRoute is Routes.Home,
            onClick = { onRouteChange(Routes.Home) }
        )
        NavigationBarItem(
            icon = { Icon(painterResource(R.drawable.settings), null) },
            label = { Text("Settings", fontSize = 11.sp) },
            selected = currentRoute is Routes.Profile,
            onClick = { onRouteChange(Routes.Profile) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostForm(
    onDismiss: () -> Unit,
    onSubmit: (type: String, title: String, description: String, location: Location, tags: List<String>, images: List<String>) -> Unit
) {
    var postType by remember { mutableStateOf("Lost") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var specificPlace by remember { mutableStateOf("") }
    var areaCity by remember { mutableStateOf("") }
    val availableTags = remember {
        mutableStateListOf(
            "Electronics",
            "Clothing",
            "Wallet",
            "Bag",
            "Pets",
            "Keys",
            "Documents",
            "Jewelry"
        )
    }
    val selectedTags = remember { mutableStateListOf<String>() }
    var customTag by remember { mutableStateOf("") }
    val imageUris = remember { mutableStateListOf<Uri>() }
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris ->
            imageUris.clear()
            imageUris.addAll(uris.take(5))
        }
    )


    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Create New Post", style = MaterialTheme.typography.titleLarge)
            IconButton(onClick = onDismiss) {
                Icon(painterResource(id = R.drawable.close), contentDescription = "Close")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text("Type *")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                onClick = { postType = "Lost" },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (postType == "Lost") MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                )
            ) { Text("Lost") }
            OutlinedButton(
                onClick = { postType = "Found" },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (postType == "Found") MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                )
            ) { Text("Found") }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text("Title *")
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Brief description of the item") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text("Description *")
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Provide additional details...") },
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text("Location *")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = specificPlace,
                onValueChange = { specificPlace = it },
                label = { Text("Specific place") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = areaCity,
                onValueChange = { areaCity = it },
                label = { Text("Area/City") },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text("Tags")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            availableTags.forEach { tag ->
                FilterChip(
                    selected = tag in selectedTags,
                    onClick = {
                        if (tag in selectedTags) selectedTags.remove(tag) else selectedTags.add(tag)
                    },
                    label = { Text(tag) }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = customTag,
                onValueChange = { customTag = it },
                label = { Text("Add custom tag") },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {
                if (customTag.isNotBlank()) {
                    if (!availableTags.contains(customTag)) {
                        availableTags.add(customTag)
                    }
                    if (!selectedTags.contains(customTag)) {
                        selectedTags.add(customTag)
                    }
                    customTag = ""
                }
            }) {
                Text("Add")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text("Images (Optional, max 5)")
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                        .clickable { imagePicker.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(painterResource(id = R.drawable.upload), contentDescription = "Upload")
                }
            }
            items(imageUris) { uri ->
                CoilImage(
                    imageModel = { uri },
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp)),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                val location = Location(place = specificPlace, area = areaCity)
                onSubmit(
                    postType.lowercase(),
                    title,
                    description,
                    location,
                    selectedTags.toList(),
                    imageUris.map { it.toString() } // Pass image URIs as strings
                )
                onDismiss()
            }) { Text("Submit Post") }
        }
    }
}