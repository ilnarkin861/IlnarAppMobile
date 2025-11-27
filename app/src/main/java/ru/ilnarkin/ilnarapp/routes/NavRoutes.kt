package ru.ilnarkin.ilnarapp.routes


sealed class NavRoutes(val route: String) {
	object NotesScreen : NavRoutes("notes")
	object TagsScreen : NavRoutes("tags")
	object ArchiveScreen : NavRoutes("archive")
	object SearchScreen : NavRoutes("search")
	object SettingsScreen : NavRoutes("settings")
	object WelcomeScreen : NavRoutes("welcome")
	object LoginScreen : NavRoutes("login")
	object PinLockScreen : NavRoutes("pin")
	object PinResetScreen : NavRoutes("pin_reset")
}