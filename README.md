# BONDINGS 3.4 — Website + Android source

Inclusive social discovery prototype combining profile discovery and short-video community concepts.

## Included
- `website/index.html` — responsive website prototype
- `app/` — Android Jetpack Compose source
- Android Discover includes profile search and an online-only filter.
- Purple/pink visual theme, profile grid, likes, Reels, Chat, Premium and Profile prototype screens.

## Important status
This is a frontend prototype. Demo profiles and distances are sample content. Android Reels upload/playback, live profile storage/discovery, real-time chat, location, authentication and payments still require backend integration and testing. The Android APK has not been built or verified in this environment.

## Android build
Open this folder in Android Studio, allow Gradle sync, then use **Build > Build Bundle(s) / APK(s) > Build APK(s)**. Requires JDK 17 and Android SDK 35.

## Website
Open `website/index.html` in a browser for a local preview. Configure and verify Supabase settings before production use.


## BONDINGS unified redesign
The visual direction now uses the supplied Messages reference structure across the app: a clear Messages heading, Active now presence row, Chats list, and a selected conversation preview for ravi.teja. The same purple/pink dark theme, rounded panels, online indicators, and compact navigation are carried through Discover, Reels, Likes, Premium, Profile, and Messages on web and Android.

The Messages conversation is a front-end preview only. Live chat delivery is not connected. Android authentication is also not connected in this source package; do not treat the preview chat as a production messaging service.
