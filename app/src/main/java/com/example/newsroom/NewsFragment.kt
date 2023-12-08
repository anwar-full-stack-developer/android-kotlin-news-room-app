package com.example.newsroom

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.newsroom.NewsRecyclerViewAdapter.*
import com.example.newsroom.data.ApiInterface
import com.example.newsroom.data.NewsData
import com.example.newsroom.data.RetrofitClient
import com.example.newsroom.placeholder.PlaceholderContent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * A fragment representing a list of Items.
 */
class NewsFragment : Fragment() {

    private var columnCount = 1
    private var editBack = 0
    private var editPosition = 0

    lateinit var rvAdapter: NewsRecyclerViewAdapter
    lateinit var rv: RecyclerView
    lateinit var rvData: PlaceholderContent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rvData = PlaceholderContent

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            editBack = it.getInt(ARG_EDIT_BACK)
            editPosition = it.getInt(ARG_EDIT_POSITION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news_list, container, false)


        // Set the adapter
        if (view is RecyclerView) {
            rv = view
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = NewsRecyclerViewAdapter(PlaceholderContent.ITEMS)


            }
            rvAdapter = rv.adapter as NewsRecyclerViewAdapter

            // Applying OnClickListener to our Adapter
            rvAdapter.setOnClickListener(object : OnClickListener {
                override fun onClick(position: Int, model: NewsData) {
                    val newsItemData: NewsData? = rvData.getItemData(position)
                    Log.d("News", "Item on clicked")
                }

                override fun onLongClick(
                    position: Int,
                    model: NewsData
                ) {
                    Log.d("News", "Item on Kong Clicked / Press")
                }

                override fun onClickToEdit(
                    position: Int,
                    model: NewsData
                ) {
                    val newsItemData: NewsData? = rvData.getItemData(position)
                    val actionType = "EDIT" // EDIT | NEW

                    Log.d("News", "News list EDIT btn clicked on clicked")
                    Log.d("News", newsItemData.toString())
                    val fr = parentFragmentManager.beginTransaction()
                    fr.replace(R.id.nav_host_fragment_content_main, NewsEditFragment.newInstance(
                        actionType,
                        position
                    ))
                    fr.commit()
                }

                override fun onClickToDelete(
                    position: Int,
                    model: NewsData
                ) {
                    val newsItemData: NewsData? = rvData.getItemData(position)
                    rvData.removeItem(position)
                    rvAdapter.submitList(PlaceholderContent.ITEMS)
//                        rvAdapter.notifyDataSetChanged()
//                        TODO: server-side api implementation
                    if (newsItemData != null) {
                        deleteNews(newsItemData)
                    }
                }
            })
            rvAdapter.notifyDataSetChanged()

        }
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getNewsList()
//        rvAdapter.notifyDataSetChanged()
        Log.i("News", "Adapter Created")
        Log.i("News", "Adapter Created, view Created")
        Log.i("News", "Adapter Data Size: " + rvAdapter.getItemCount().toString())
    }

    private fun notifyDataChanged(){

        rvAdapter.submitList(PlaceholderContent.ITEMS)
//        Log.d("News: ", "Adapter List Count: " + rvAdapter.getItemCount().toString())
        rvAdapter.notifyDataSetChanged()
//        Log.d("News: ", "Adapter List Count: = " + rvAdapter.getItemCount().toString())
    }
    private fun getNewsList() {
        val retrofit = RetrofitClient.getInstance()
        val apiInterface = retrofit.create(ApiInterface::class.java)
        GlobalScope.launch {
            try {
                val response = apiInterface.getAllNews()
                if (response.isSuccessful()) {
                    //your code for handling success response
//                    Log.d("News: ", response.body().toString())

                    val newsList: List<NewsData>? = response.body()?.data
                    if (newsList != null) {
                        PlaceholderContent.clearAll()
//                        Log.d("News: ", "Adapter List Count: " + rvAdapter.getItemCount().toString())
                        for (i in 0.. newsList.size - 1) {
//                            Log.d("News: ", "AdapterItem: " + newsList[i].toString())
                            PlaceholderContent.addItem(newsList[i])
                        }
//                        if (! rv.isComputingLayout())
//                        rvAdapter.submitList(PlaceholderContent.ITEMS)
                        notifyDataChanged()
                        Log.d("News: ", "Adapter List Count:= " + rvAdapter.getItemCount().toString())
//                        rvAdapter.notifyDataSetChanged()
                    }


                } else {
                    Log.e("Error", response.errorBody().toString())
//                    Toast.makeText(
//                        this@NewsFragment.context,
//                        response.errorBody().toString(),
//                        Toast.LENGTH_LONG
//                    ).show()
                }
            }catch (Ex:Exception){
                Ex.localizedMessage?.let { Log.e("Error", it) }
            }
        }
    }

    fun deleteNews(newsItemData: NewsData) {
        val retrofit = RetrofitClient.getInstance()
        val apiInterface = retrofit.create(ApiInterface::class.java)
        GlobalScope.launch {
            try {
                val id = PlaceholderContent.itemToId(newsItemData)
                val response = apiInterface.deleteNews(id)
                if (response.isSuccessful()) {
                    //your code for handling success response
                    Log.d("News: ", response.body().toString())

//                    val news: NewsData? = response.body()?.data
//                    if (news != null) {
////                        PlaceholderContent.removeItem(position)
//                    }

                } else {
                    Log.e("Error", response.errorBody().toString())
//                    Toast.makeText(
//                        this@NewsEditFragment.context,
//                        response.errorBody().toString(),
//                        Toast.LENGTH_LONG
//                    ).show()
                }
            }catch (Ex:Exception){
                Ex.localizedMessage?.let { Log.e("Error", it) }
            }
        }
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"
        const val ARG_EDIT_BACK = "edit-back"
        const val ARG_EDIT_POSITION = "edit-position"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int, editBack: Int, editPosition: Int) =
            NewsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                    putInt(ARG_EDIT_BACK, editBack)
                    putInt(ARG_EDIT_POSITION, editPosition)
                }
            }
    }
}