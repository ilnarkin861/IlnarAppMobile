package ru.ilnarkin.ilnarapp.routes


sealed class NavRoutes(val route: String) {
	object NotesScreen : NavRoutes("notes")
	object TagsScreen : NavRoutes("tags")
	object ArchiveScreen : NavRoutes("archive")
	object SettingsScreen : NavRoutes("settings")
	object WelcomeScreen : NavRoutes("welcome")
	object LoginScreen : NavRoutes("welcome/login")
	object PinLockScreen : NavRoutes("welcome/pin")
	object PinResetScreen : NavRoutes("welcome/pin_reset")
	object OverlayScreen : NavRoutes("welcome/overlay")
}