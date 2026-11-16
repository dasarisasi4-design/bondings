package com.bondings.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import coil.compose.AsyncImage

private val Bg = Color(0xFF100B19)
private val Panel = Color(0xFF1D1528)
private val Pink = Color(0xFFFF4F9A)
private val Purple = Color(0xFF9B5CFF)
private val Muted = Color(0xFFB7AFC2)

data class Person(val name:String,val age:Int,val distance:String,val online:Boolean,val image:String,val tag:String)
private val people = listOf(
    Person("Aarav",25,"1 km away",true,"https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=600","New here"),
    Person("Rohan",27,"2 km away",true,"https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=600","Looking for friends"),
    Person("Kiran",24,"3 km away",false,"https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=600","Online recently"),
    Person("Dev",29,"4 km away",true,"https://images.unsplash.com/photo-1535713875002-d1d0cf3771d0?w=600","Open to chat"),
    Person("Sam",26,"5 km away",false,"https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=600","New here"),
    Person("Aditya",28,"6 km away",true,"https://images.unsplash.com/photo-1504593811423-6dd665756598?w=600","Looking for dates")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContent { BondingsApp() } }
}

@Composable fun BondingsApp() {
    var tab by remember { mutableStateOf("Discover") }
    var selected by remember { mutableStateOf<Person?>(null) }
    var filter by remember { mutableStateOf(false) }
    var liked by remember { mutableStateOf(setOf<String>()) }
    var search by remember { mutableStateOf("") }
    var onlineOnly by remember { mutableStateOf(false) }
    val visiblePeople = people.filter { p ->
        (!onlineOnly || p.online) && (search.isBlank() || p.name.contains(search, true) || p.tag.contains(search, true))
    }
    BackHandler(enabled = tab != "Discover" || selected != null || filter) {
        when { selected != null -> selected = null; filter -> filter = false; else -> tab = "Discover" }
    }
    MaterialTheme(colorScheme = darkColorScheme(background=Bg, surface=Panel, primary=Pink)) {
        Surface(Modifier.fillMaxSize(), color=Bg) {
            Column {
                if (tab == "Discover") {
                    Row(Modifier.fillMaxWidth().padding(start=18.dp, end=12.dp, top=15.dp, bottom=10.dp), verticalAlignment=Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text("BONDINGS", color=Color.White, fontSize=25.sp, fontWeight=FontWeight.Black, letterSpacing=1.7.sp)
                            Text("Find your kind of connection", color=Muted, fontSize=11.sp)
                        }
                        IconButton(onClick={filter=true}) { Icon(Icons.Default.Tune, "Filters", tint=Color.White) }
                        IconButton(onClick={tab="Likes"}) { Icon(Icons.Default.Favorite, "Likes", tint=Pink) }
                    }
                    OutlinedTextField(
                        value=search, onValueChange={search=it}, singleLine=true,
                        placeholder={Text("Search people or interests", color=Muted, fontSize=13.sp)},
                        leadingIcon={Icon(Icons.Default.Search, null, tint=Muted)},
                        trailingIcon={if(search.isNotEmpty()) IconButton(onClick={search=""}) { Icon(Icons.Default.Close, "Clear", tint=Muted) } else null},
                        modifier=Modifier.fillMaxWidth().padding(horizontal=16.dp),
                        shape=RoundedCornerShape(15.dp),
                        colors=OutlinedTextFieldDefaults.colors(focusedBorderColor=Purple, unfocusedBorderColor=Color(0xFF3A2B49), focusedTextColor=Color.White, unfocusedTextColor=Color.White, cursorColor=Pink)
                    )
                    Row(Modifier.fillMaxWidth().padding(horizontal=16.dp, vertical=12.dp), verticalAlignment=Alignment.CenterVertically) {
                        Surface(onClick={onlineOnly=!onlineOnly}, shape=RoundedCornerShape(50), color=if(onlineOnly) Color(0xFF3A2144) else Panel, border=BorderStroke(1.dp, if(onlineOnly) Pink else Color(0xFF3A2B49))) {
                            Row(Modifier.padding(horizontal=13.dp, vertical=8.dp), verticalAlignment=Alignment.CenterVertically) {
                                Box(Modifier.size(7.dp).background(Color(0xFF42E69A), CircleShape)); Spacer(Modifier.width(7.dp)); Text("Online now", color=Color.White, fontSize=11.sp, fontWeight=FontWeight.SemiBold)
                            }
                        }
                        Spacer(Modifier.weight(1f))
                        Text("${visiblePeople.size} profiles", color=Muted, fontSize=11.sp)
                    }
                    if (visiblePeople.isEmpty()) {
                        Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment=Alignment.Center) {
                            Column(horizontalAlignment=Alignment.CenterHorizontally) {
                                Icon(Icons.Default.SearchOff, null, tint=Muted, modifier=Modifier.size(42.dp))
                                Spacer(Modifier.height(8.dp)); Text("No profiles found", color=Color.White, fontWeight=FontWeight.Bold)
                                Text("Try another name or turn off the online filter", color=Muted, fontSize=11.sp)
                            }
                        }
                    } else LazyVerticalGrid(
                        columns=GridCells.Fixed(3), modifier=Modifier.weight(1f),
                        contentPadding=PaddingValues(start=10.dp, end=10.dp, top=2.dp, bottom=12.dp),
                        horizontalArrangement=Arrangement.spacedBy(8.dp), verticalArrangement=Arrangement.spacedBy(9.dp)
                    ) {
                        items(visiblePeople, key={it.name}) { p ->
                            Box(Modifier.fillMaxWidth().aspectRatio(.72f).clip(RoundedCornerShape(15.dp)).clickable { selected=p }) {
                                AsyncImage(p.image, p.name, Modifier.fillMaxSize(), contentScale=ContentScale.Crop)
                                Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color(0x1008060C), Color(0xE808060C)), startY=130f)))
                                if(p.online) Row(Modifier.align(Alignment.TopStart).padding(7.dp).clip(RoundedCornerShape(50)).background(Color(0xD9000000)).padding(horizontal=6.dp, vertical=4.dp), verticalAlignment=Alignment.CenterVertically) {
                                    Box(Modifier.size(6.dp).background(Color(0xFF43E69A), CircleShape)); Spacer(Modifier.width(4.dp)); Text("ONLINE", color=Color.White, fontSize=7.sp, fontWeight=FontWeight.Bold)
                                }
                                Column(Modifier.align(Alignment.BottomStart).padding(start=7.dp, end=4.dp, bottom=8.dp)) {
                                    Text("${p.name}, ${p.age}", color=Color.White, fontSize=12.sp, fontWeight=FontWeight.Bold, maxLines=1, overflow=TextOverflow.Ellipsis)
                                    Text(p.distance, color=Color(0xFFE4DDEB), fontSize=9.sp, maxLines=1)
                                }
                                IconButton(onClick={liked=if(p.name in liked) liked-p.name else liked+p.name}, modifier=Modifier.align(Alignment.TopEnd).size(32.dp)) {
                                    Icon(if(p.name in liked) Icons.Default.Favorite else Icons.Default.FavoriteBorder, null, tint=if(p.name in liked) Pink else Color.White, modifier=Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                } else if (tab == "Reels") {
                    Column {
                        Row(Modifier.fillMaxWidth().padding(horizontal=12.dp, vertical=6.dp), verticalAlignment=Alignment.CenterVertically) {
                            IconButton(onClick={tab="Discover"}) { Icon(Icons.Default.ArrowBack, "Back", tint=Color.White) }
                            Text("BONDINGS REELS", color=Color.White, fontWeight=FontWeight.Black, fontSize=18.sp)
                        }
                        Box(Modifier.weight(1f).fillMaxWidth()) { ReelsPage() }
                    }
                } else {
                    Column(Modifier.fillMaxSize()) {
                        Row(Modifier.fillMaxWidth().padding(horizontal=12.dp, vertical=6.dp), verticalAlignment=Alignment.CenterVertically) {
                            IconButton(onClick={tab="Discover"}) { Icon(Icons.Default.ArrowBack, "Back", tint=Color.White) }
                            Text(tab, color=Color.White, fontWeight=FontWeight.Bold, fontSize=20.sp)
                        }
                        Box(Modifier.weight(1f).fillMaxWidth()) { OtherPage(tab, liked.size) { tab="Discover" } }
                    }
                }
                NavigationBar(containerColor=Color(0xFF17101F), contentColor=Muted, tonalElevation=0.dp) {
                    listOf("Discover","Reels","Likes","Chat","Premium","Profile").forEach { item ->
                        NavigationBarItem(selected=tab==item, onClick={tab=item}, icon={ Icon(when(item){"Discover"->Icons.Default.GridView;"Reels"->Icons.Default.PlayCircle;"Likes"->Icons.Default.FavoriteBorder;"Chat"->Icons.Default.ChatBubbleOutline;"Premium"->Icons.Default.WorkspacePremium;else->Icons.Default.Person}, null) }, label={Text(item, fontSize=9.sp)}, colors=NavigationBarItemDefaults.colors(selectedIconColor=Pink, selectedTextColor=Pink, indicatorColor=Color(0xFF38213B), unselectedIconColor=Muted, unselectedTextColor=Muted))
                    }
                }
            }
            if(selected!=null) AlertDialog(onDismissRequest={selected=null}, containerColor=Panel, title={Text("${selected!!.name}, ${selected!!.age}", color=Color.White)}, text={Column { AsyncImage(selected!!.image, null, Modifier.fillMaxWidth().height(210.dp).clip(RoundedCornerShape(12.dp)), contentScale=ContentScale.Crop); Spacer(Modifier.height(10.dp)); Text("${selected!!.distance} • ${selected!!.tag}", color=Muted); Text("Profile preview", color=Color.White) }}, confirmButton={TextButton(onClick={selected=null}){Text("Close", color=Pink)}}, dismissButton={TextButton(onClick={liked=liked+selected!!.name;selected=null}){Text("Like", color=Pink)}})
            if(filter) AlertDialog(onDismissRequest={filter=false}, containerColor=Panel, title={Text("Discover filters", color=Color.White)}, text={Column { Text("Distance", color=Muted); Text("Up to 25 km", color=Color.White); Spacer(Modifier.height(10.dp)); Text("Age range", color=Muted); Text("18–35", color=Color.White); Spacer(Modifier.height(10.dp)); Text("Advanced filters are demo controls in this prototype.", color=Muted, fontSize=12.sp) }}, confirmButton={TextButton(onClick={filter=false}){Text("Apply", color=Pink)}})
        }
    }
}

@Composable private fun ReelsPage() {
    var showUpload by remember { mutableStateOf(false) }
    var caption by remember { mutableStateOf("") }
    var selectedVideo by remember { mutableStateOf<android.net.Uri?>(null) }
    val picker = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri -> selectedVideo = uri }
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement=Arrangement.spacedBy(12.dp)) {
        Text("BONDINGS Reels", color=Color.White, fontSize=25.sp, fontWeight=FontWeight.Bold)
        Text("Members-only video community", color=Muted, fontSize=12.sp)
        Button(onClick={showUpload=true}, colors=ButtonDefaults.buttonColors(containerColor=Pink), modifier=Modifier.fillMaxWidth()) {
            Icon(Icons.Default.Add, null); Spacer(Modifier.width(8.dp)); Text("Create Reel")
        }
        if (showUpload) {
            Card(colors=CardDefaults.cardColors(containerColor=Panel), shape=RoundedCornerShape(18.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(10.dp)) {
                    Text("Upload a Reel", color=Color.White, fontWeight=FontWeight.Bold, fontSize=18.sp)
                    OutlinedButton(onClick={picker.launch("video/*")}, modifier=Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.VideoLibrary, null); Spacer(Modifier.width(8.dp)); Text(if(selectedVideo==null) "Choose video" else "Video selected")
                    }
                    OutlinedTextField(value=caption,onValueChange={caption=it.take(2200)},label={Text("Caption")},modifier=Modifier.fillMaxWidth(),minLines=2)
                    Text("Uploads will require member sign-in and moderation approval.", color=Muted, fontSize=11.sp)
                    Text("Android Supabase upload connection is not enabled in this build yet.", color=Color(0xFFFFD27B), fontSize=11.sp)
                    Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                        TextButton(onClick={showUpload=false;selectedVideo=null;caption=""}) { Text("Cancel", color=Muted) }
                        Button(onClick={showUpload=false}, enabled=selectedVideo!=null, colors=ButtonDefaults.buttonColors(containerColor=Pink)) { Text("Done") }
                    }
                }
            }
        }
        Box(Modifier.fillMaxWidth().weight(1f).clip(RoundedCornerShape(18.dp)).background(Panel), contentAlignment=Alignment.Center) {
            Column(horizontalAlignment=Alignment.CenterHorizontally, verticalArrangement=Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.PlayCircle, null, tint=Pink, modifier=Modifier.size(58.dp))
                Text("Your Reels feed", color=Color.White, fontWeight=FontWeight.Bold)
                Text("Approved member videos will appear here.", color=Muted, fontSize=12.sp)
                Text("Sign-in, upload and playback backend integration is the next build step.", color=Muted, fontSize=10.sp)
            }
        }
    }
}

@Composable private fun OtherPage(tab:String, likes:Int, back:()->Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(22.dp), verticalArrangement=Arrangement.spacedBy(14.dp)) {
        Text(tab, color=Color.White, fontSize=26.sp, fontWeight=FontWeight.Bold)
        when(tab) {
            "Likes" -> { Text("$likes profiles liked", color=Muted); Text("Your liked profiles will appear here.", color=Muted) }
            "Chat" -> MessagesDesign()
            "Premium" -> {
                Text("More ways to connect", color=Muted)
                PlanCard("BONDINGS PLUS", "₹199 / month", listOf("See who likes you","More profile filters","Ad-free experience"))
                PlanCard("BONDINGS PREMIUM", "₹399 / month", listOf("All Plus features","Profile boosts","Priority discovery"))
                Text("Demo pricing only — payments are not connected.", color=Muted, fontSize=11.sp)
            }
            else -> {
                Text("Your profile", color=Muted)
                Card(colors=CardDefaults.cardColors(containerColor=Panel), shape=RoundedCornerShape(18.dp)) {
                    Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(10.dp)) {
                        Text("Build your introduction", color=Color.White, fontWeight=FontWeight.Bold)
                        Text("Your photos, bio, interests and preferences will be managed here when profile storage is connected.", color=Muted, fontSize=12.sp)
                        Button(onClick=back, colors=ButtonDefaults.buttonColors(containerColor=Pink)) { Text("Back to Discover") }
                    }
                }
            }
        }
    }
}
@Composable private fun PlanCard(title:String, price:String, features:List<String>) {
    Card(colors=CardDefaults.cardColors(containerColor=Panel), shape=RoundedCornerShape(18.dp)) { Column(Modifier.padding(17.dp), verticalArrangement=Arrangement.spacedBy(8.dp)) { Text(title, color=Pink, fontWeight=FontWeight.Bold); Text(price, color=Color.White, fontSize=22.sp, fontWeight=FontWeight.Bold); features.forEach { Text("✓  $it", color=Muted, fontSize=13.sp) }; OutlinedButton(onClick={}, enabled=false){Text("Coming soon")} } }
}


@Composable private fun MessagesDesign() {
    var selected by remember { mutableStateOf("ravi.teja") }
    var draft by remember { mutableStateOf("") }
    var sent by remember { mutableStateOf(listOf("Pretty good! Nice to hear from you 😊")) }
    val chats = listOf(
        Triple("ravi.teja", "Hey! How's your day going?", "Now"),
        Triple("Aarav", "Nice to connect with you ✨", "12m"),
        Triple("Kiran", "Sent you a photo", "1h"),
        Triple("Dev", "See you soon!", "Yesterday")
    )
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal=16.dp, vertical=10.dp), verticalArrangement=Arrangement.spacedBy(14.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment=Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("YOUR CONNECTIONS", color=Color(0xFFD58BC0), fontSize=10.sp, fontWeight=FontWeight.ExtraBold, letterSpacing=2.sp)
                Text("Messages", color=Color.White, fontSize=27.sp, fontWeight=FontWeight.ExtraBold)
                Text("Stay close to the people you connect with.", color=Muted, fontSize=12.sp)
            }
            Surface(shape=RoundedCornerShape(14.dp), color=Color(0xFF2A2038), border=BorderStroke(1.dp, Color(0xFF4B385A))) {
                Icon(Icons.Default.Search, "Search chats", tint=Color.White, modifier=Modifier.padding(12.dp).size(20.dp))
            }
        }
        Card(colors=CardDefaults.cardColors(containerColor=Panel), shape=RoundedCornerShape(20.dp), border=BorderStroke(1.dp, Color(0xFF3B2A4A))) {
            Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment=Alignment.CenterVertically) {
                    Text("Active now", color=Color.White, fontWeight=FontWeight.Bold, fontSize=15.sp)
                    Spacer(Modifier.weight(1f)); Text("● 4 online", color=Color(0xFF59E5A4), fontSize=11.sp)
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween) {
                    people.take(4).forEachIndexed { i, person ->
                        Column(horizontalAlignment=Alignment.CenterHorizontally, modifier=Modifier.clickable { selected=if(i==0) "ravi.teja" else person.name }) {
                            Box(Modifier.size(52.dp)) {
                                AsyncImage(person.image, person.name, Modifier.fillMaxSize().clip(CircleShape).border(2.dp, Purple, CircleShape), contentScale=ContentScale.Crop)
                                Box(Modifier.align(Alignment.BottomEnd).size(13.dp).background(Color(0xFF59E5A4), CircleShape).border(2.dp, Panel, CircleShape))
                            }
                            Spacer(Modifier.height(5.dp)); Text(if(i==0) "ravi.teja" else person.name, color=Muted, fontSize=10.sp, maxLines=1)
                        }
                    }
                }
            }
        }
        Row(verticalAlignment=Alignment.Bottom) {
            Text("Chats", color=Color.White, fontWeight=FontWeight.Bold, fontSize=20.sp)
            Spacer(Modifier.weight(1f)); Text("Recent conversations", color=Muted, fontSize=11.sp)
        }
        chats.forEach { item ->
            val active = selected == item.first
            Surface(onClick={selected=item.first}, color=if(active) Color(0xFF48213F) else Panel, shape=RoundedCornerShape(16.dp), border=BorderStroke(1.dp, if(active) Color(0xFF9C4D83) else Color(0x22FFFFFF)), modifier=Modifier.fillMaxWidth()) {
                Row(Modifier.padding(12.dp), verticalAlignment=Alignment.CenterVertically) {
                    val person = people.firstOrNull { it.name == item.first } ?: people.first()
                    AsyncImage(person.image, item.first, Modifier.size(48.dp).clip(CircleShape).border(1.dp, Purple, CircleShape), contentScale=ContentScale.Crop)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f), verticalArrangement=Arrangement.spacedBy(4.dp)) {
                        Text(item.first, color=Color.White, fontWeight=FontWeight.Bold, fontSize=13.sp)
                        Text(item.second, color=Muted, fontSize=11.sp, maxLines=1)
                    }
                    Column(horizontalAlignment=Alignment.End, verticalArrangement=Arrangement.spacedBy(5.dp)) {
                        Text(item.third, color=Muted, fontSize=10.sp)
                        if(item.first=="ravi.teja") Box(Modifier.size(19.dp).background(Pink, CircleShape), contentAlignment=Alignment.Center) { Text("2", color=Color.White, fontSize=10.sp, fontWeight=FontWeight.Bold) }
                    }
                }
            }
        }
        Card(colors=CardDefaults.cardColors(containerColor=Color(0xFF100B19)), shape=RoundedCornerShape(18.dp), border=BorderStroke(1.dp, Color(0xFF503657))) {
            Column(Modifier.padding(14.dp), verticalArrangement=Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment=Alignment.CenterVertically) {
                    AsyncImage(people.first().image, null, Modifier.size(40.dp).clip(CircleShape), contentScale=ContentScale.Crop)
                    Spacer(Modifier.width(10.dp)); Column { Text(selected, color=Color.White, fontWeight=FontWeight.Bold); Text("● Active now", color=Color(0xFF59E5A4), fontSize=10.sp) }
                }
                Divider(color=Color(0x22FFFFFF))
                Text("Hey! How's your day going?", color=Color.White, fontSize=12.sp, modifier=Modifier.clip(RoundedCornerShape(14.dp)).background(Color(0xFF2B2039)).padding(12.dp))
                sent.forEach { msg -> Text(msg, color=Color.White, fontSize=12.sp, modifier=Modifier.fillMaxWidth().wrapContentWidth(Alignment.End).clip(RoundedCornerShape(14.dp)).background(Brush.horizontalGradient(listOf(Pink, Purple))).padding(12.dp)) }
                Row(verticalAlignment=Alignment.CenterVertically, horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value=draft, onValueChange={draft=it}, placeholder={Text("Write a message…", color=Muted, fontSize=12.sp)}, modifier=Modifier.weight(1f), singleLine=true, shape=RoundedCornerShape(14.dp), colors=OutlinedTextFieldDefaults.colors(focusedBorderColor=Purple, unfocusedBorderColor=Color(0xFF3A2B49), focusedTextColor=Color.White, unfocusedTextColor=Color.White))
                    IconButton(onClick={if(draft.isNotBlank()){sent=sent+draft.trim();draft=""}}, modifier=Modifier.background(Pink, CircleShape)) { Icon(Icons.Default.Send, "Send", tint=Color.White) }
                }
                Text("Preview only — live chat backend is not connected yet.", color=Muted, fontSize=10.sp)
            }
        }
    }
}
