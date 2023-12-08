package com.example.newsroom.placeholder

import android.util.Log
import com.example.newsroom.data.NewsData
import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object PlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    var ITEMS: MutableList<NewsData> = ArrayList()

    /**
     * A map of sample (placeholder) items, by ID.
     */
    var ITEM_MAP: MutableMap<String, NewsData> = HashMap()

    private var nextPosition = 0;

//    private val COUNT = 25

    init {
        // Add some sample items.
//        for (i in 1..COUNT) {
//            addItem(createPlaceholderItem(i))
//        }
    }
    public fun itemToId(item: NewsData) : String {
        var id  = item.id
        if (item.id.isNullOrEmpty())
            id = item._id
        return id
    }

    public fun clearAll() {
        nextPosition=0
        ITEM_MAP = HashMap()
        ITEMS = ArrayList()
    }

    public fun addItem(item: NewsData) {
        val position = nextPosition
        val id = itemToId(item)
//        Log.d("News", "News id: $id")
        ITEMS.add(item)
        ITEM_MAP.put(id, item)

//        Log.d("News", "News addItem Position: $position")
//        Log.d("News", "News Data: " + item.toString())
        nextPosition++
    }

    public fun updateItem(position: Int, item: NewsData) {
        val id = itemToId(item)
        ITEMS.removeAt(position)
        ITEMS.add(position, item)
        ITEM_MAP.put(id, item)
    }

    fun getItemData(position: Int) : NewsData? {
        val item = ITEMS[position]
        val id = itemToId(item)
        return ITEM_MAP.get(id)
    }
    fun removeItem(position: Int) : Boolean{
        val item = ITEMS[position]
        val id = itemToId(item)
        ITEMS.removeAt(position);
        ITEM_MAP.remove(id)
        return true
    }
    public fun createPlaceholderItem(position: Int, item: NewsData): PlaceholderItem {
        val id = itemToId(item)
        return PlaceholderItem((position).toString(), id, item.title, makeDetails(position, item))
    }

    private fun makeDetails(position: Int, item: NewsData): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0..position - 1) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    /**
     * A placeholder item representing a piece of content.
     */
    data class PlaceholderItem(val id: String, val itemId: String, val content: String, val details: String) {
        override fun toString(): String = content
    }
}