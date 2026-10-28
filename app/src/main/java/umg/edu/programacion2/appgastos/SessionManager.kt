package umg.edu.programacion2.appgastos

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión para inicializar DataStore a nivel de contexto
val Context.dataStore by preferencesDataStore(name = "sesion_prefs")

class SessionManager(private val context: Context) {
    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }

    // Flujo para saber si ya inició sesión
    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_LOGGED_IN] ?: false
        }

    // Función para guardar que ya inició sesión
    suspend fun setLoggedIn(logged: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = logged
        }
    }
}