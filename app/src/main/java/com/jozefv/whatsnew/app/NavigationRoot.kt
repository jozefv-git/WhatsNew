package com.jozefv.whatsnew.app

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.jozefv.whatsnew.feat_auth.presentation.intro.IntroScreenRoot
import com.jozefv.whatsnew.feat_auth.presentation.login.LoginScreenRoot
import com.jozefv.whatsnew.feat_auth.presentation.register.RegisterScreenRoot
import com.jozefv.whatsnew.feat_articles.presentation.filter.FilterScreenRoot
import com.jozefv.whatsnew.feat_articles.presentation.articles.NewsScreenRoot
import com.jozefv.whatsnew.feat_articles.presentation.search.SearchScreenRoot
import com.jozefv.whatsnew.feat_profile.presentation.ProfileScreenRoot
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
private object AuthGraph {
    @Serializable
    object Intro

    @Serializable
    object Register

    @Serializable
    object Login
}

@Serializable
private object LoggedGraph {
    @Serializable
    object News

    @Serializable
    object Search

    @Serializable
    object Filter

    @Serializable
    object Profile
}

@Composable
fun NavigationRoot(
    navHostController: NavHostController,
    isFirstSession: Boolean,
    isLoggedIn: Boolean
) {
    NavHost(
        navController = navHostController,
        startDestination = if (isLoggedIn) LoggedGraph else AuthGraph
    ) {
        authGraph(navHostController, isFirstSession)
        loggedGraph(navHostController)
    }
}

private fun NavGraphBuilder.authGraph(navController: NavHostController, isFirstSession: Boolean) {
    // If we have been logged before - show Login instead
    navigation<AuthGraph>(startDestination = if (isFirstSession) AuthGraph.Intro else AuthGraph.Login) {
        composable<AuthGraph.Intro> {
            IntroScreenRoot(
                onSignUpClick = { navController.navigate(AuthGraph.Register) },
                // Navigate to Articles - but still can go back to AuthGraph - so don't pop it up
                onSkipClick = { navController.navigate(LoggedGraph.News) }
            )
        }
        composable<AuthGraph.Register> {
            RegisterScreenRoot(
                onSuccessfulRegistration = {
                    navController.navigate(LoggedGraph.News) {
                        popUpTo<AuthGraph> {
                            // Pop whole AuthGraph as we don't need it anymore
                            inclusive = true
                        }
                    }
                },
                onSkipClick = {
                    navController.navigate(LoggedGraph.News)
                }
            )
        }
        composable<AuthGraph.Login> {
            LoginScreenRoot(
                onLoginSuccess = {
                    navController.navigate(LoggedGraph) {
                        popUpTo<AuthGraph> {
                            inclusive = true
                        }
                    }
                },
                onSkipClick = {
                    navController.navigate(LoggedGraph.News)
                }
            )
        }
    }
}

private fun NavGraphBuilder.loggedGraph(navController: NavHostController) {
    navigation<LoggedGraph>(startDestination = LoggedGraph.News) {
        composable<LoggedGraph.News> {
            // Return same instance of the koinViewModel because we use same backStackEntry
            // route of our LoggedGraph
            // ViewModel will persists until LoggedGraph exists - so until its destroyed from the memory
            val backStackEntry = remember {
                navController.getBackStackEntry(LoggedGraph)
            }
            NewsScreenRoot(
                viewModel = koinViewModel(viewModelStoreOwner = backStackEntry),
                onSearchClick = {
                    navController.navigate(LoggedGraph.Search)
                },
                onFilterClick = {
                    navController.navigate(LoggedGraph.Filter)
                },
                onLoginClick = {
                    navController.navigate(AuthGraph) {
                        // Don't let user go back
                        popUpTo<AuthGraph> {
                            inclusive = true
                        }
                    }
                },
                onProfileClick = {
                    navController.navigate(LoggedGraph.Profile)
                }
            )
        }
        composable<LoggedGraph.Search> {
            val backStackEntry = remember {
                navController.getBackStackEntry(LoggedGraph)
            }
            SearchScreenRoot(
                viewModel = koinViewModel(viewModelStoreOwner = backStackEntry),
                onBackClick = { navController.popBackStack() },
                onLoginClick = {
                    navController.navigate(AuthGraph) {
                        // Don't let user go back
                        popUpTo<AuthGraph> {
                            inclusive = true
                        }
                    }
                })
        }
        composable<LoggedGraph.Filter> {
            val backStackEntry = remember {
                navController.getBackStackEntry(LoggedGraph)
            }
            FilterScreenRoot(
                viewModel = koinViewModel(viewModelStoreOwner = backStackEntry),
                onBackClick = {
                    navController.navigate(LoggedGraph.News) {
                        popUpTo<LoggedGraph.News> {
                            inclusive = true
                        }
                    }
                },
                onLoginClick = {
                    navController.navigate(AuthGraph) {
                        // Don't let user go back
                        popUpTo<AuthGraph> {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<LoggedGraph.Profile> {
            val localActivity = LocalActivity.current
            ProfileScreenRoot(
                viewModel = koinViewModel(),
                onBackClick = {
                    navController.popBackStack()
                },
                onLogoutClick = {
                    localActivity?.finish()
                }
            )
        }
    }
}
