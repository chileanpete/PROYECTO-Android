package com.example.proyecto_droid.data.network

import com.example.proyecto_droid.data.model.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Servicio API unificado que incluye todos los endpoints del backend Laravel.
 * Organizado por módulos funcionales para mejor mantenibilidad.
 */
interface UnifiedApiService {
    
    // ===== AUTENTICACIÓN =====
    
    @POST("usuarios/login")
    suspend fun login(@Body loginRequest: LoginRequest): UnifiedApiResponse<LoginResponse>
    
    @POST("usuarios/registro")
    suspend fun register(@Body registerRequest: RegisterRequest): UnifiedApiResponse<RegisterResponse>
    
    // ===== USUARIOS =====
    
    @GET("usuarios")
    suspend fun getUsers(): UnifiedApiResponse<List<User>>
    
    @GET("usuarios/{id}")
    suspend fun getUser(@Path("id") id: Int): UnifiedApiResponse<User>
    
    @PUT("usuarios/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: User): UnifiedApiResponse<User>
    
    @DELETE("usuarios/{id}")
    suspend fun deleteUser(@Path("id") id: Int): UnifiedApiResponse<Unit>
    
    @POST("usuarios/{id}/cambiar-password")
    suspend fun changePassword(
        @Path("id") userId: Int,
        @Body requestBody: Map<String, String>
    ): UnifiedApiResponse<Unit>
    
    @GET("usuarios/{id}/estadisticas")
    suspend fun getUserStatistics(@Path("id") id: Int): UnifiedApiResponse<Map<String, Any>>
    
    // ===== PLATOS =====
    
    @GET("platos")
    suspend fun getPlatos(@Query("page") page: Int? = null): UnifiedApiResponse<PaginatedResponse<Plato>>
    
    @GET("platos/simple")
    suspend fun getPlatosSimple(): UnifiedApiResponse<PaginatedResponse<Plato>>
    
    @GET("platos/{id}")
    suspend fun getPlato(@Path("id") id: Int): UnifiedApiResponse<Plato>
    
    @POST("platos")
    suspend fun createPlato(@Body plato: Plato): UnifiedApiResponse<Plato>
    
    @PUT("platos/{id}")
    suspend fun updatePlato(@Path("id") id: Int, @Body plato: Plato): UnifiedApiResponse<Plato>
    
    @DELETE("platos/{id}")
    suspend fun deletePlato(@Path("id") id: Int): UnifiedApiResponse<Unit>
    
    @GET("platos/recomendados/{idUsuario}")
    suspend fun getRecommendedPlatos(@Path("idUsuario") userId: Int): UnifiedApiResponse<List<Plato>>
    
    @GET("platos/buscar")
    suspend fun searchPlatos(@Query("q") query: String): UnifiedApiResponse<List<Plato>>
    
    @GET("platos/categoria/{idCategoria}")
    suspend fun getPlatosByCategory(@Path("idCategoria") categoryId: Int): UnifiedApiResponse<List<Plato>>
    
    @GET("platos/lugar/{idLugar}")
    suspend fun getPlatosByPlace(@Path("idLugar") placeId: Int): UnifiedApiResponse<List<Plato>>
    
    // ===== ACTIVIDAD FÍSICA =====
    
    @GET("actividad-fisica")
    suspend fun getActividadesFisicas(
        @Query("id_usuario") userId: Int? = null,
        @Query("page") page: Int? = null
    ): UnifiedApiResponse<PaginatedResponse<RegistroActividad>>
    
    @GET("actividad-fisica/{id}")
    suspend fun getActividadFisica(@Path("id") id: Int): UnifiedApiResponse<RegistroActividad>
    
    @POST("actividad-fisica")
    suspend fun createActividadFisica(@Body registro: RegistroActividad): UnifiedApiResponse<RegistroActividad>
    
    @PUT("actividad-fisica/{id}")
    suspend fun updateActividadFisica(
        @Path("id") id: Int,
        @Body registro: RegistroActividad
    ): UnifiedApiResponse<RegistroActividad>
    
    @DELETE("actividad-fisica/{id}")
    suspend fun deleteActividadFisica(@Path("id") id: Int): UnifiedApiResponse<Unit>
    
    @GET("actividad-fisica/estadisticas/{idUsuario}")
    suspend fun getEstadisticasActividad(@Path("idUsuario") userId: Int): UnifiedApiResponse<Map<String, Any>>
    
    @POST("actividad-fisica/por-fecha/{idUsuario}")
    suspend fun getActividadesPorFecha(
        @Path("idUsuario") userId: Int,
        @Body dateRange: Map<String, String>
    ): UnifiedApiResponse<List<RegistroActividad>>
    
    @PATCH("actividad-fisica/{id}/completar")
    suspend fun marcarActividadCompletada(@Path("id") id: Int): UnifiedApiResponse<RegistroActividad>
    
    @GET("actividad-fisica/exportar-pdf")
    suspend fun exportarPDFActividad(): UnifiedApiResponse<ExportResponse>
    
    // ===== REGISTRO DE CONSUMO =====
    
    @GET("consumos")
    suspend fun getConsumos(
        @Query("id_usuario") userId: Int? = null,
        @Query("page") page: Int? = null
    ): UnifiedApiResponse<PaginatedResponse<RegistroConsumo>>
    
    @GET("consumos/{id}")
    suspend fun getConsumo(@Path("id") id: Int): UnifiedApiResponse<RegistroConsumo>
    
    @POST("consumos")
    suspend fun createConsumo(@Body consumo: RegistroConsumo): UnifiedApiResponse<RegistroConsumo>
    
    @PUT("consumos/{id}")
    suspend fun updateConsumo(
        @Path("id") id: Int,
        @Body consumo: RegistroConsumo
    ): UnifiedApiResponse<RegistroConsumo>
    
    @DELETE("consumos/{id}")
    suspend fun deleteConsumo(@Path("id") id: Int): UnifiedApiResponse<Unit>
    
    @GET("consumos/estadisticas/{idUsuario}")
    suspend fun getEstadisticasConsumo(@Path("idUsuario") userId: Int): UnifiedApiResponse<Map<String, Any>>
    
    @GET("consumos/por-fecha/{idUsuario}")
    suspend fun getConsumosPorFecha(
        @Path("idUsuario") userId: Int,
        @Query("fecha") fecha: String
    ): UnifiedApiResponse<List<RegistroConsumo>>
    
    // ===== FAVORITOS =====
    
    @GET("favoritos/usuario/{idUsuario}")
    suspend fun getFavoritosByUser(@Path("idUsuario") userId: Int): UnifiedApiResponse<List<FavoritoPlato>>
    
    @POST("favoritos")
    suspend fun addFavorito(@Body favorito: FavoritoPlato): UnifiedApiResponse<FavoritoPlato>
    
    @DELETE("favoritos/{id}")
    suspend fun removeFavorito(@Path("id") id: Int): UnifiedApiResponse<Unit>
    
    @GET("favoritos/verificar/{idUsuario}/{idPlato}")
    suspend fun verificarFavorito(
        @Path("idUsuario") userId: Int,
        @Path("idPlato") platoId: Int
    ): UnifiedApiResponse<Boolean>
    
    // ===== DESAFÍOS =====
    
    @GET("desafios")
    suspend fun getDesafios(@Query("page") page: Int? = null): UnifiedApiResponse<PaginatedResponse<Desafio>>
    
    @GET("desafios/{id}")
    suspend fun getDesafio(@Path("id") id: Int): UnifiedApiResponse<Desafio>
    
    @POST("desafios")
    suspend fun createDesafio(@Body desafio: Desafio): UnifiedApiResponse<Desafio>
    
    @PUT("desafios/{id}")
    suspend fun updateDesafio(@Path("id") id: Int, @Body desafio: Desafio): UnifiedApiResponse<Desafio>
    
    @DELETE("desafios/{id}")
    suspend fun deleteDesafio(@Path("id") id: Int): UnifiedApiResponse<Unit>
    
    @GET("desafios/activos")
    suspend fun getDesafiosActivos(): UnifiedApiResponse<List<Desafio>>
    
    @GET("desafios/usuario/{idUsuario}")
    suspend fun getDesafiosByUser(@Path("idUsuario") userId: Int): UnifiedApiResponse<List<Desafio>>
    
    @GET("desafios/tipo/{tipo}")
    suspend fun getDesafiosByType(@Path("tipo") tipo: String): UnifiedApiResponse<List<Desafio>>
    
    @GET("desafios/dificultad/{dificultad}")
    suspend fun getDesafiosByDifficulty(@Path("dificultad") dificultad: String): UnifiedApiResponse<List<Desafio>>
    
    @GET("desafios/estadisticas")
    suspend fun getEstadisticasDesafios(): UnifiedApiResponse<Map<String, Any>>
    
    // ===== TALLERES RECREATIVOS =====
    
    @GET("talleres")
    suspend fun getTalleres(@Query("page") page: Int? = null): UnifiedApiResponse<PaginatedResponse<TallerRecreativo>>
    
    @GET("talleres/{id}")
    suspend fun getTaller(@Path("id") id: Int): UnifiedApiResponse<TallerRecreativo>
    
    @POST("talleres")
    suspend fun createTaller(@Body taller: TallerRecreativo): UnifiedApiResponse<TallerRecreativo>
    
    @PUT("talleres/{id}")
    suspend fun updateTaller(@Path("id") id: Int, @Body taller: TallerRecreativo): UnifiedApiResponse<TallerRecreativo>
    
    @DELETE("talleres/{id}")
    suspend fun deleteTaller(@Path("id") id: Int): UnifiedApiResponse<Unit>
    
    @GET("talleres-activos-temp")
    suspend fun getTalleresActivos(): UnifiedApiResponse<List<TallerRecreativo>>
    
    @GET("talleres/tipo/{tipo}")
    suspend fun getTalleresByType(@Path("tipo") tipo: String): UnifiedApiResponse<List<TallerRecreativo>>
    
    @GET("talleres/instructor/{instructor}")
    suspend fun getTalleresByInstructor(@Path("instructor") instructor: String): UnifiedApiResponse<List<TallerRecreativo>>
    
    @GET("talleres/con-cupos")
    suspend fun getTalleresConCupos(): UnifiedApiResponse<List<TallerRecreativo>>
    
    @GET("talleres/gratuitos")
    suspend fun getTalleresGratuitos(): UnifiedApiResponse<List<TallerRecreativo>>
    
    @GET("talleres/usuario/{idUsuario}")
    suspend fun getTalleresByUser(@Path("idUsuario") userId: Int): UnifiedApiResponse<List<TallerRecreativo>>
    
    @GET("talleres/estadisticas")
    suspend fun getEstadisticasTalleres(): UnifiedApiResponse<Map<String, Any>>
    
    // ===== MENÚS DIARIOS =====
    
    @GET("menus-diarios/usuario/{idUsuario}")
    suspend fun getMenusByUser(@Path("idUsuario") userId: Int): UnifiedApiResponse<List<MenuDiario>>
    
    @GET("menus-diarios/{id}")
    suspend fun getMenu(@Path("id") id: Int): UnifiedApiResponse<MenuDiario>
    
    @POST("menus-diarios")
    suspend fun createMenu(@Body menu: MenuDiario): UnifiedApiResponse<MenuDiario>
    
    @PUT("menus-diarios/{id}")
    suspend fun updateMenu(@Path("id") id: Int, @Body menu: MenuDiario): UnifiedApiResponse<MenuDiario>
    
    @DELETE("menus-diarios/{id}")
    suspend fun deleteMenu(@Path("id") id: Int): UnifiedApiResponse<Unit>
    
    @POST("menus-diarios/por-fecha/{idUsuario}")
    suspend fun getMenusPorFecha(
        @Path("idUsuario") userId: Int,
        @Body dateRange: Map<String, String>
    ): UnifiedApiResponse<List<MenuDiario>>
    
    @PATCH("menus-diarios/{id}/completar")
    suspend fun marcarMenuCompletado(@Path("id") id: Int): UnifiedApiResponse<MenuDiario>
    
    @POST("menus-diarios/resumen-semanal/{idUsuario}")
    suspend fun getResumenSemanal(
        @Path("idUsuario") userId: Int,
        @Body weekData: Map<String, String>
    ): UnifiedApiResponse<Map<String, Any>>
    
    // ===== CATEGORÍAS Y LUGARES =====
    
    @GET("categorias-comida")
    suspend fun getCategorias(): UnifiedApiResponse<List<CategoriaComida>>
    
    @GET("lugares-comida")
    suspend fun getLugares(): UnifiedApiResponse<List<LugarComida>>
    
    @GET("tipos-ejercicio")
    suspend fun getTiposEjercicio(): UnifiedApiResponse<List<TipoEjercicio>>
    
    // ===== ENDPOINTS DE PRUEBA =====
    
    @GET("test")
    suspend fun testConnection(): UnifiedApiResponse<Map<String, String>>
    
    @GET("test-auth")
    suspend fun testAuth(): UnifiedApiResponse<Map<String, Any>>
} 