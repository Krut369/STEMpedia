package com.example.contentsharingapp.repository

import android.util.Log
import com.example.contentsharingapp.model.TileItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class TileRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
) {
    companion object {
        private const val TAG = "TileRepository"
        private const val COLLECTION_HOME_TILES = "home_tiles"
        private const val MAX_VISIBLE_TILES = 6

        private const val FIELD_TITLE = "title"
        private const val FIELD_TYPE = "type"
        private const val FIELD_VISIBLE = "visible"
        private const val FIELD_ORDER = "order"
        private const val FIELD_TARGET_URL = "targetUrl"
        private const val FIELD_IMAGE_URL = "imageUrl"
        private const val FIELD_IMAGE_PATH = "imagePath"
        private const val FIELD_YOUTUBE_ID = "youtubeId"
    }

    /**
     * Fetches visible tiles from Firestore, applying necessary fixes
     */
    suspend fun getTiles(): List<TileItem> {
        return try {
            Log.d(TAG, "Fetching tiles from Firestore...")

            val documents = firestore.collection(COLLECTION_HOME_TILES)
                .orderBy(FIELD_ORDER, Query.Direction.ASCENDING)
                .get()
                .await()

            Log.d(TAG, "Query successful: ${documents.size()} documents found")

            if (documents.isEmpty) {
                Log.d(TAG, "No tiles found, initializing with sample data...")
                insertSampleTiles()
                return emptyList()
            }

            val tiles = documents.documents.mapNotNull { doc ->
                parseTileDocument(doc)
            }

            tiles.filter { it.visible }
                .sortedBy { it.order }
                .take(MAX_VISIBLE_TILES)
                .also { Log.d(TAG, "Loaded ${it.size} visible tiles") }

        } catch (e: Exception) {
            Log.e(TAG, "Error fetching tiles", e)
            throw TileRepositoryException("Failed to fetch tiles", e)
        }
    }

    /**
     * Inserts sample tiles for initial setup
     */
    suspend fun insertSampleTiles() {
        try {
            Log.d(TAG, "Inserting sample tiles...")

            val sampleTiles = createSampleTiles()
            val batch = firestore.batch()

            sampleTiles.forEachIndexed { index, tileData ->
                val docRef = firestore.collection(COLLECTION_HOME_TILES)
                    .document("tile${index + 1}")
                batch.set(docRef, tileData)
            }

            batch.commit().await()
            Log.d(TAG, "Successfully inserted ${sampleTiles.size} sample tiles")

        } catch (e: Exception) {
            Log.e(TAG, "Error inserting sample tiles", e)
            throw TileRepositoryException("Failed to insert sample tiles", e)
        }
    }

    /**
     * Parses a Firestore document into a TileItem
     */
    private suspend fun parseTileDocument(document: com.google.firebase.firestore.DocumentSnapshot): TileItem? {
        return try {
            val id = document.id
            val title = document.getString(FIELD_TITLE) ?: return null
            val type = document.getString(FIELD_TYPE) ?: "content"
            val imagePath = document.getString(FIELD_IMAGE_PATH)
            val targetUrl = document.getString(FIELD_TARGET_URL)
            val youtubeId = document.getString(FIELD_YOUTUBE_ID)
            val order = document.getLong(FIELD_ORDER) ?: 0L
            val visible = document.getBoolean(FIELD_VISIBLE) ?: true

            val imageUrl = resolveImageUrl(
                documentImageUrl = document.getString(FIELD_IMAGE_URL),
                imagePath = imagePath,
                title = title
            )

            TileItem(
                id = id,
                title = title,
                type = type,
                imagePath = imagePath,
                targetUrl = targetUrl,
                youtubeId = youtubeId,
                order = order,
                visible = visible,
                imageUrl = imageUrl
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing document ${document.id}", e)
            null
        }
    }

    /**
     * Resolves the image URL from various sources in priority order
     */
    private suspend fun resolveImageUrl(
        documentImageUrl: String?,
        imagePath: String?,
        title: String
    ): String {
        // Priority 1: Use existing imageUrl if available
        if (!documentImageUrl.isNullOrBlank()) {
            return documentImageUrl
        }

        // Priority 2: Try to load from Firebase Storage
        if (!imagePath.isNullOrBlank()) {
            try {
                val storageRef = storage.reference.child(imagePath)
                val url = storageRef.downloadUrl.await().toString()
                Log.d(TAG, "Loaded image from Storage: $imagePath")
                return url
            } catch (e: Exception) {
                Log.w(TAG, "Failed to load from Storage: $imagePath", e)
            }
        }

        // Priority 3: Fallback to placeholder image
        return generatePlaceholderImageUrl(title)
    }

    /**
     * Generates a consistent placeholder image URL based on title
     */
    private fun generatePlaceholderImageUrl(title: String): String {
        val seed = title.lowercase().replace(" ", "")
        return "https://picsum.photos/seed/$seed/600/400"
    }

    /**
     * Creates sample tile data
     */
    private fun createSampleTiles(): List<Map<String, Any?>> {
        return listOf(
            createTileData(
                title = "Robotics",
                order = 1,
                targetUrl = "https://www.youtube.com/watch?v=UObzWjPb6XM"
            ),
            createTileData(
                title = "Science",
                order = 2,
                targetUrl = "https://www.sciencedaily.com"
            ),
            createTileData(
                title = "Math",
                order = 3,
                targetUrl = "https://www.khanacademy.org/math"
            ),
            createTileData(
                title = "Art",
                order = 4,
                targetUrl = "https://artsandculture.google.com"
            ),
            createTileData(
                title = "Coding",
                order = 5,
                targetUrl = "https://www.freecodecamp.org"
            ),
            createTileData(
                title = "Intro to AI",
                order = 6,
                type = "youtube",
                targetUrl = null,
                youtubeId = "mOYN9HlfTgo"
            )

        )
    }

    /**
     * Helper to create tile data map
     */
    private fun createTileData(
        title: String,
        order: Int,
        type: String = "content",
        targetUrl: String? = null,
        youtubeId: String? = null
    ): Map<String, Any?> {
        return mapOf(
            FIELD_TITLE to title,
            FIELD_TYPE to type,
            FIELD_VISIBLE to true,
            FIELD_ORDER to order.toLong(),
            FIELD_TARGET_URL to targetUrl,
            FIELD_IMAGE_URL to generatePlaceholderImageUrl(title),
            FIELD_YOUTUBE_ID to youtubeId
        )
    }
}

/**
 * Custom exception for repository errors
 */
class TileRepositoryException(message: String, cause: Throwable? = null) : Exception(message, cause)