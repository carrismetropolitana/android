package pt.carrismetropolitana.mobile.managers
import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.carrismetropolitana.mobile.services.favorites.FavoriteDao
import pt.carrismetropolitana.mobile.services.favorites.FavoriteItem
import pt.carrismetropolitana.mobile.services.favorites.FavoriteType

class FavoritesManager(private val favoriteDao: FavoriteDao) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    val favorites = mutableStateListOf<FavoriteItem>()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        coroutineScope.launch {
            favoriteDao.getAll().collect { loadedFavorites ->
                withContext(Dispatchers.Main) {
                    favorites.clear()
                    favorites.addAll(loadedFavorites)
                }
            }
        }
    }

    fun addFavorite(item: FavoriteItem) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                favoriteDao.insert(item)
            }
        }
    }

    fun removeFavorite(item: FavoriteItem) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                favoriteDao.delete(item)
            }
        }
    }

    fun isFavorited(itemId: String, itemType: FavoriteType): Boolean {
        return favorites.any {
            when (itemType) {
                FavoriteType.STOP -> it.stopId == itemId
                FavoriteType.PATTERN -> it.lineId == itemId
            }
        }
    }

    fun fuzzyRemove(itemId: String, itemType: FavoriteType) {
        val itemToRemove = favorites.find {
            when (itemType) {
                FavoriteType.STOP -> it.stopId == itemId
                FavoriteType.PATTERN -> it.lineId == itemId
            }
        }
        itemToRemove?.let { removeFavorite(it) }
    }

    fun wipeFavorites() {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                favoriteDao.deleteAll()
            }
        }
    }
}