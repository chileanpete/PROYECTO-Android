package com.example.proyecto_droid.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.data.model.Post
import com.example.proyecto_droid.data.model.PostCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

data class MainFeedUiState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedTab: Int = 0,
    val isRefreshing: Boolean = false
)

sealed class MainFeedEvent {
    object LoadPosts : MainFeedEvent()
    object RefreshPosts : MainFeedEvent()
    data class SelectTab(val tabIndex: Int) : MainFeedEvent()
    data class LikePost(val postId: String) : MainFeedEvent()
    data class SharePost(val postId: String) : MainFeedEvent()
    object CreateNewPost : MainFeedEvent()
}

class MainFeedViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainFeedUiState())
    val uiState: StateFlow<MainFeedUiState> = _uiState.asStateFlow()

    init {
        loadPosts()
    }

    fun handleEvent(event: MainFeedEvent) {
        when (event) {
            is MainFeedEvent.LoadPosts -> {
                loadPosts()
            }
            is MainFeedEvent.RefreshPosts -> {
                refreshPosts()
            }
            is MainFeedEvent.SelectTab -> {
                _uiState.value = _uiState.value.copy(selectedTab = event.tabIndex)
            }
            is MainFeedEvent.LikePost -> {
                likePost(event.postId)
            }
            is MainFeedEvent.SharePost -> {
                sharePost(event.postId)
            }
            is MainFeedEvent.CreateNewPost -> {
                // Navegar a pantalla de crear post
            }
        }
    }

    private fun loadPosts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                // Simular carga de datos
                kotlinx.coroutines.delay(1000)
                
                val mockPosts = generateMockPosts()
                _uiState.value = _uiState.value.copy(
                    posts = mockPosts,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al cargar posts"
                )
            }
        }
    }

    private fun refreshPosts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            
            try {
                kotlinx.coroutines.delay(500)
                val mockPosts = generateMockPosts()
                _uiState.value = _uiState.value.copy(
                    posts = mockPosts,
                    isRefreshing = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isRefreshing = false,
                    error = e.message ?: "Error al actualizar posts"
                )
            }
        }
    }

    private fun likePost(postId: String) {
        val currentPosts = _uiState.value.posts.toMutableList()
        val postIndex = currentPosts.indexOfFirst { it.id == postId }
        
        if (postIndex != -1) {
            val post = currentPosts[postIndex]
            val updatedPost = post.copy(
                likes = if (post.isLiked) post.likes - 1 else post.likes + 1,
                isLiked = !post.isLiked
            )
            currentPosts[postIndex] = updatedPost
            
            _uiState.value = _uiState.value.copy(posts = currentPosts)
        }
    }

    private fun sharePost(postId: String) {
        val currentPosts = _uiState.value.posts.toMutableList()
        val postIndex = currentPosts.indexOfFirst { it.id == postId }
        
        if (postIndex != -1) {
            val post = currentPosts[postIndex]
            val updatedPost = post.copy(shares = post.shares + 1)
            currentPosts[postIndex] = updatedPost
            
            _uiState.value = _uiState.value.copy(posts = currentPosts)
        }
    }

    private fun generateMockPosts(): List<Post> {
        return listOf(
            Post(
                id = "1",
                userId = "user1",
                userName = "María García",
                title = "Ensalada Quinoa Saludable",
                description = "Una deliciosa ensalada de quinoa con vegetales frescos y aderezo casero. Perfecta para el almuerzo o cena. #saludable #quinoa #ensalada",
                imageUrl = null,
                category = PostCategory.RECIPE,
                likes = 124,
                comments = 18,
                shares = 5,
                createdAt = Date(System.currentTimeMillis() - 3600000) // 1 hora atrás
            ),
            Post(
                id = "2",
                userId = "user2",
                userName = "Carlos Fitness",
                title = "Rutina de Cardio en Casa",
                description = "No necesitas gimnasio para hacer cardio efectivo. Esta rutina de 20 minutos te ayudará a quemar calorías y mejorar tu resistencia. #cardio #ejercicio #casa",
                imageUrl = null,
                category = PostCategory.EXERCISE,
                likes = 89,
                comments = 12,
                shares = 8,
                createdAt = Date(System.currentTimeMillis() - 7200000) // 2 horas atrás
            ),
            Post(
                id = "3",
                userId = "user3",
                userName = "NutriVida",
                title = "Beneficios del Agua con Limón",
                description = "Empezar el día con agua tibia y limón tiene múltiples beneficios para la salud. Te explico por qué deberías incluirlo en tu rutina matutina. #salud #limon #hidratacion",
                imageUrl = null,
                category = PostCategory.TIP,
                likes = 156,
                comments = 23,
                shares = 15,
                createdAt = Date(System.currentTimeMillis() - 10800000) // 3 horas atrás
            ),
            Post(
                id = "4",
                userId = "user4",
                userName = "Eventos Saludables",
                title = "Maratón Verde 2024",
                description = "¡Inscripciones abiertas para la Maratón Verde 2024! 5K y 10K por las calles de la ciudad. Incluye kit de corredor y medalla finisher. #maraton #running #evento",
                imageUrl = null,
                category = PostCategory.EVENT,
                likes = 203,
                comments = 31,
                shares = 42,
                createdAt = Date(System.currentTimeMillis() - 14400000) // 4 horas atrás
            ),
            Post(
                id = "5",
                userId = "user5",
                userName = "Motivación Diaria",
                title = "Cada paso cuenta",
                description = "Recuerda que cada pequeño paso hacia una vida más saludable cuenta. No te enfoques en la perfección, enfócate en el progreso. ¡Tú puedes! 💪 #motivacion #progreso #salud",
                imageUrl = null,
                category = PostCategory.MOTIVATION,
                likes = 267,
                comments = 45,
                shares = 28,
                createdAt = Date(System.currentTimeMillis() - 18000000) // 5 horas atrás
            )
        )
    }
} 