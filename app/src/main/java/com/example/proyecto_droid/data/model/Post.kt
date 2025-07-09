package com.example.proyecto_droid.data.model

import java.util.Date

data class Post(
    val id: String,
    val userId: String,
    val userName: String,
    val userAvatar: String? = null,
    val title: String,
    val description: String,
    val imageUrl: String? = null,
    val category: PostCategory,
    val likes: Int = 0,
    val comments: Int = 0,
    val shares: Int = 0,
    val isLiked: Boolean = false,
    val createdAt: Date,
    val tags: List<String> = emptyList()
)

enum class PostCategory {
    NUTRITION,    // Alimentación
    EXERCISE,     // Ejercicio
    EVENT,        // Eventos
    TIP,          // Consejos
    RECIPE,       // Recetas
    MOTIVATION    // Motivación
}

data class PostInteraction(
    val postId: String,
    val userId: String,
    val type: InteractionType,
    val timestamp: Date
)

enum class InteractionType {
    LIKE,
    COMMENT,
    SHARE
} 