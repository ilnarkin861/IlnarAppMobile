package ru.ilnarkin.ilnarapp.routes


sealed class NavRoutes(val route: String) {
	object NotesScreen : NavRoutes("notes")
	object TagsScreen : NavRoutes("tags")
	object ArchiveScreen : NavRoutes("archive")
	object SearchScreen : NavRoutes("search")
	object SettingsScreen : NavRoutes("settings")
	object WelcomeScreen : NavRoutes("welcome")
}