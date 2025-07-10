package com.example.proyecto_droid.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.data.model.*
import com.example.proyecto_droid.data.network.UnifiedApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Estados UI para menús favoritos del usuario
 */
sealed class FavoritosUiState {
    data object Loading : FavoritosUiState()
    data object Idle : FavoritosUiState()
    data class Success(val menusFavoritos: List<MenuFavoritoUsuario>) : FavoritosUiState()
    data class Error(val message: String) : FavoritosUiState()
}

/**
 * Estados UI para menús diarios
 */
sealed class MenuUiState {
    data object Loading : MenuUiState()
    data object Idle : MenuUiState()
    data class Success(val menus: List<MenuDiario>) : MenuUiState()
    data class Error(val message: String) : MenuUiState()
}

/**
 * Estados UI para recomendaciones diarias
 */
sealed class RecomendacionesUiState {
    data object Loading : RecomendacionesUiState()
    data object Idle : RecomendacionesUiState()
    data class Success(val recomendaciones: List<Plato>) : RecomendacionesUiState()
    data class Error(val message: String) : RecomendacionesUiState()
}

/**
 * ViewModel para la gestión de favoritos y menús diarios
 */
class FavoritosMenuViewModel(
    private val apiService: UnifiedApiService
) : ViewModel() {

    private val _favoritosUiState = MutableStateFlow<FavoritosUiState>(FavoritosUiState.Idle)
    val favoritosUiState: StateFlow<FavoritosUiState> = _favoritosUiState.asStateFlow()

    private val _menuUiState = MutableStateFlow<MenuUiState>(MenuUiState.Idle)
    val menuUiState: StateFlow<MenuUiState> = _menuUiState.asStateFlow()

    private val _recomendacionesUiState = MutableStateFlow<RecomendacionesUiState>(RecomendacionesUiState.Idle)
    val recomendacionesUiState: StateFlow<RecomendacionesUiState> = _recomendacionesUiState.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _isAddingToMenu = MutableStateFlow(false)
    val isAddingToMenu: StateFlow<Boolean> = _isAddingToMenu.asStateFlow()

    /**
     * Carga los menús favoritos del usuario
     */
    fun loadFavoritos(userId: Int) {
        viewModelScope.launch {
            _favoritosUiState.value = FavoritosUiState.Loading
            try {
                val response = apiService.getMenusFavoritosByUser(userId)
                if (response.success) {
                    _favoritosUiState.value = FavoritosUiState.Success(response.data ?: emptyList())
                } else {
                    _favoritosUiState.value = FavoritosUiState.Error(response.message ?: "Error al cargar menús favoritos")
                }
            } catch (e: Exception) {
                _favoritosUiState.value = FavoritosUiState.Error(e.message ?: "Error de conexión")
            }
        }
    }

    /**
     * Crea un nuevo menú favorito
     */
    fun createMenuFavorito(
        userId: Int, 
        nombreMenu: String, 
        descripcion: String? = null,
        tipoComida: String? = null,
        platos: List<PlatoMenuFavoritoRequest>
    ) {
        viewModelScope.launch {
            try {
                val request = CreateMenuFavoritoRequest(
                    idUsuario = userId,
                    nombreMenu = nombreMenu,
                    descripcion = descripcion,
                    tipoComida = tipoComida,
                    platos = platos
                )
                val response = apiService.createMenuFavorito(request)
                if (response.success) {
                    loadFavoritos(userId) // Recargar menús favoritos
                }
            } catch (e: Exception) {
                _favoritosUiState.value = FavoritosUiState.Error(e.message ?: "Error al crear menú favorito")
            }
        }
    }

    /**
     * Agregar un plato a un menú favorito existente
     */
    fun agregarPlatoAMenuFavorito(menuId: Int, platoId: Int, cantidad: Double = 1.0, notas: String? = null, userId: Int) {
        viewModelScope.launch {
            try {
                val request = AgregarPlatoMenuRequest(
                    idPlato = platoId,
                    cantidad = cantidad,
                    notas = notas
                )
                val response = apiService.agregarPlatoAMenuFavorito(menuId, request)
                if (response.success) {
                    loadFavoritos(userId) // Recargar menús favoritos
                }
            } catch (e: Exception) {
                _favoritosUiState.value = FavoritosUiState.Error(e.message ?: "Error al agregar plato al menú favorito")
            }
        }
    }

    /**
     * Elimina un menú favorito
     */
    fun removeFromFavoritos(menuFavoritoId: Int, userId: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.deleteMenuFavorito(menuFavoritoId)
                if (response.success) {
                    loadFavoritos(userId) // Recargar menús favoritos
                }
            } catch (e: Exception) {
                _favoritosUiState.value = FavoritosUiState.Error(e.message ?: "Error al eliminar menú favorito")
            }
        }
    }

    /**
     * Usar un menú favorito (aplicarlo como menú del día)
     */
    fun usarMenuFavorito(menuFavoritoId: Int, userId: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.usarMenuFavorito(menuFavoritoId)
                if (response.success) {
                    // Opcional: también recargar menús diarios si se quiere mostrar el menú aplicado
                    loadMenusForDate(userId, _selectedDate.value)
                }
            } catch (e: Exception) {
                _favoritosUiState.value = FavoritosUiState.Error(e.message ?: "Error al usar menú favorito")
            }
        }
    }

    /**
     * Función temporal para agregar platos individuales como menús favoritos de un solo plato
     */
    fun addToFavoritos(userId: Int, platoId: Int) {
        viewModelScope.launch {
            try {
                // Crear un menú favorito con un solo plato
                val platosRequest = listOf(
                    PlatoMenuFavoritoRequest(
                        idPlato = platoId,
                        cantidad = 1.0,
                        unidad = "porcion"
                    )
                )
                
                val request = CreateMenuFavoritoRequest(
                    idUsuario = userId,
                    nombreMenu = "Plato Favorito", // Nombre genérico, el usuario puede cambiarlo después
                    descripcion = "Menú creado automáticamente",
                    tipoComida = null,
                    platos = platosRequest
                )
                
                val response = apiService.createMenuFavorito(request)
                if (response.success) {
                    loadFavoritos(userId) // Recargar menús favoritos
                }
            } catch (e: Exception) {
                _favoritosUiState.value = FavoritosUiState.Error(e.message ?: "Error al agregar a favoritos")
            }
        }
    }

    /**
     * Agregar un plato individual a favoritos (no como menú favorito)
     */
    fun addPlatoToFavoritos(userId: Int, platoId: Int) {
        viewModelScope.launch {
            try {
                val favorito = FavoritoPlato(
                    idUsuario = userId,
                    idPlato = platoId,
                    fechaAgregado = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                )
                val response = apiService.addFavorito(favorito)
                if (response.success) {
                    // Mostrar mensaje de éxito o actualizar UI según sea necesario
                    println("Plato agregado a favoritos exitosamente")
                }
            } catch (e: Exception) {
                _favoritosUiState.value = FavoritosUiState.Error(e.message ?: "Error al agregar plato a favoritos")
            }
        }
    }

    /**
     * Reintentar cargar favoritos
     */
    fun retryLoadFavoritos(userId: Int) {
        loadFavoritos(userId)
    }

    /**
     * Reintentar cargar menús
     */
    fun retryLoadMenus(userId: Int) {
        loadMenusForDate(userId, _selectedDate.value)
    }

    /**
     * Carga los menús del usuario para la fecha seleccionada
     */
    fun loadMenusForDate(userId: Int, date: LocalDate = _selectedDate.value) {
        viewModelScope.launch {
            _menuUiState.value = MenuUiState.Loading
            try {
                val dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                val dateRange = mapOf("fecha" to dateString)
                println("Cargando menús para usuario $userId, fecha: $dateString") // Debug
                val response = apiService.getMenusPorFecha(userId, dateRange)
                if (response.success) {
                    _menuUiState.value = MenuUiState.Success(response.data ?: emptyList())
                } else {
                    val errorMsg = when (response.statusCode) {
                        500 -> "Error del servidor. Revisa la configuración del backend."
                        404 -> "Endpoint no encontrado en el servidor."
                        else -> response.message ?: "Error al cargar menús"
                    }
                    println("Error al cargar menús: ${response.statusCode} - $errorMsg") // Debug
                    _menuUiState.value = MenuUiState.Error(errorMsg)
                }
            } catch (e: retrofit2.HttpException) {
                val errorMsg = when (e.code()) {
                    500 -> "Error del servidor. El servidor está experimentando problemas temporales."
                    404 -> "Servicio no disponible en este momento."
                    422 -> "Datos de fecha inválidos."
                    else -> "Error de conexión: ${e.message()}"
                }
                println("Error HTTP al cargar menús: ${e.code()} - $errorMsg") // Debug
                _menuUiState.value = MenuUiState.Error(errorMsg)
            } catch (e: java.net.SocketTimeoutException) {
                val errorMsg = "La conexión tardó demasiado. Verifica tu conexión a internet."
                println("Timeout al cargar menús: $errorMsg") // Debug
                _menuUiState.value = MenuUiState.Error(errorMsg)
            } catch (e: java.net.ConnectException) {
                val errorMsg = "No se puede conectar al servidor. Verifica tu conexión."
                println("Error de conexión al cargar menús: $errorMsg") // Debug
                _menuUiState.value = MenuUiState.Error(errorMsg)
            } catch (e: Exception) {
                val errorMsg = "Error inesperado: ${e.message ?: "Problema desconocido"}"
                println("Excepción al cargar menús: $errorMsg") // Debug
                _menuUiState.value = MenuUiState.Error(errorMsg)
            }
        }
    }

    /**
     * Añade un plato al menú del día
     */
    fun addPlatoToMenu(userId: Int, platoId: Int, tipoComida: String, cantidad: Double = 1.0) {
        viewModelScope.launch {
            _isAddingToMenu.value = true
            try {
                val dateString = _selectedDate.value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                val menu = MenuDiario(
                    idUsuario = userId,
                    fecha = dateString,
                    tipoComida = tipoComida,
                    idPlato = platoId,
                    cantidad = cantidad,
                    unidad = "porcion"
                )
                val response = apiService.createMenu(menu)
                if (response.success) {
                    loadMenusForDate(userId, _selectedDate.value) // Recargar menús
                }
            } catch (e: Exception) {
                _menuUiState.value = MenuUiState.Error(e.message ?: "Error al añadir al menú")
            } finally {
                _isAddingToMenu.value = false
            }
        }
    }

    /**
     * Quita un plato del menú
     */
    fun removePlatoFromMenu(menuId: Int, userId: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.deleteMenu(menuId)
                if (response.success) {
                    loadMenusForDate(userId, _selectedDate.value) // Recargar menús
                }
            } catch (e: Exception) {
                _menuUiState.value = MenuUiState.Error(e.message ?: "Error al quitar del menú")
            }
        }
    }

    /**
     * Marca un menú como completado
     */
    fun marcarMenuCompletado(menuId: Int, userId: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.marcarMenuCompletado(menuId)
                if (response.success) {
                    loadMenusForDate(userId, _selectedDate.value) // Recargar menús
                }
            } catch (e: Exception) {
                _menuUiState.value = MenuUiState.Error(e.message ?: "Error al marcar como completado")
            }
        }
    }

    /**
     * Cambia la fecha seleccionada
     */
    fun setSelectedDate(date: LocalDate) {
        _selectedDate.value = date
    }

    /**
     * Genera recomendaciones diarias automáticas (3 comidas aleatorias)
     */
    fun generateDailyRecommendations() {
        viewModelScope.launch {
            _recomendacionesUiState.value = RecomendacionesUiState.Loading
            try {
                val response = apiService.getPlatosSimple()
                if (response.success && response.data?.data != null) {
                    val allPlatos = response.data.data
                    if (allPlatos.isNotEmpty()) {
                        // Seleccionar 3 platos aleatorios
                        val recommendations = allPlatos.shuffled().take(3)
                        _recomendacionesUiState.value = RecomendacionesUiState.Success(recommendations)
                    } else {
                        _recomendacionesUiState.value = RecomendacionesUiState.Error("No hay platos disponibles para recomendaciones")
                    }
                } else {
                    _recomendacionesUiState.value = RecomendacionesUiState.Error(response.message ?: "Error al cargar platos para recomendaciones")
                }
            } catch (e: Exception) {
                _recomendacionesUiState.value = RecomendacionesUiState.Error(e.message ?: "Error de conexión")
            }
        }
    }

    /**
     * Acepta una recomendación y la añade al menú del día
     */
    fun acceptRecommendation(userId: Int, plato: Plato, tipoComida: String) {
        addPlatoToMenu(userId, plato.id, tipoComida)
    }

    /**
     * Refresca los datos
     */
    fun refresh(userId: Int) {
        loadFavoritos(userId)
        loadMenusForDate(userId)
    }

    /**
     * Reinicia el estado
     */
    fun resetState() {
        _favoritosUiState.value = FavoritosUiState.Idle
        _menuUiState.value = MenuUiState.Idle
        _recomendacionesUiState.value = RecomendacionesUiState.Idle
    }

} 