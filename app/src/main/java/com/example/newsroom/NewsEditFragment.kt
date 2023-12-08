package com.example.newsroom

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.newsroom.data.ApiInterface
import com.example.newsroom.data.NewsData
import com.example.newsroom.data.NewsNewRequestData
import com.example.newsroom.data.ResponseNews
import com.example.newsroom.data.RetrofitClient
import com.example.newsroom.placeholder.PlaceholderContent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_NEWS_ACTION_TYPE = "ARG_NEWS_ACTION_TYPE"
private const val ARG_NEWS_POSITION = "ARG_NEWS_POSITION"
private const val ARG_NEWS_ITEM_DATA = "ARG_NEWS_ITEM_DATA"

/**
 * A simple [Fragment] subclass.
 * Use the [NewsEditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsEditFragment : Fragment() {
    private var actionType: String? = null // EDIT | NEW
    private var position: Int = -1

    lateinit var newsItemData: NewsData
    lateinit var rvData: PlaceholderContent

    lateinit var editNewsTitle: EditText
    lateinit var editNewsStatus: EditText
    lateinit var editNewsDetails: EditText
    lateinit var editNewsSaveBtn : Button
    lateinit var editNewsCancelBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rvData = PlaceholderContent

        arguments?.let {
            actionType = it.getString(ARG_NEWS_ACTION_TYPE)
            position = it.getInt(ARG_NEWS_POSITION)
//            newsItemData = it.getSerializable(ARG_NEWS_ITEM_DATA)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("News", "POSITION "+ position)

        if (position > -1)
            newsItemData = rvData.getItemData(position)!!


        editNewsTitle = view.findViewById(R.id.editNewsTitle)
        editNewsStatus = view.findViewById(R.id.editNewsStatus)
        editNewsDetails = view.findViewById(R.id.editNewsDetails)
        editNewsSaveBtn = view.findViewById(R.id.editNewsSaveBtn)
        editNewsCancelBtn = view.findViewById(R.id.editNewsCancelBtn)

        if (actionType == "EDIT") {
            editNewsTitle.setText(newsItemData!!.title)
            editNewsStatus.setText(newsItemData!!.status)
            editNewsDetails.setText(newsItemData!!.details)
            editNewsSaveBtn.setText("Update")
        }
        else {
            editNewsSaveBtn.setText("Save")
        }
        editNewsSaveBtn.setOnClickListener(View.OnClickListener {
            Log.d("News", "Submit Button Clicked. position: "+ position)

//            newsItemData.copy()
            if (actionType == "EDIT") {
                newsItemData?.title  = editNewsTitle.getText().toString()
                newsItemData?.status  = editNewsStatus.getText().toString()
                newsItemData?.details  = editNewsDetails.getText().toString()
                PlaceholderContent.updateItem(position, newsItemData)
                updateNews(newsItemData)
            } else {
                val newsItemData : NewsNewRequestData = NewsNewRequestData("","","")
                newsItemData?.title  = editNewsTitle.getText().toString()
                newsItemData?.status  = editNewsStatus.getText().toString()
                newsItemData?.details  = editNewsDetails.getText().toString()
                saveNews(newsItemData)
            }
        })

        editNewsCancelBtn.setOnClickListener(View.OnClickListener {
            goBackToList()
        })

    }
    private fun goBackToList(){

        parentFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment_content_main, NewsFragment.newInstance(1,1, position))
            .commit()
    }

    private fun saveNews(newsItemData: NewsNewRequestData) {
        val retrofit = RetrofitClient.getInstance()
        val apiInterface = retrofit.create(ApiInterface::class.java)
        GlobalScope.launch {
            try {

                val response = apiInterface.saveNews(newsItemData)
                if (response.isSuccessful()) {
                    //your code for handling success response
                    Log.d("News: ", "SAve Success: " +response.body().toString())

                    val news: NewsData? = response.body()?.data
                    if (news != null) {
                        PlaceholderContent.addItem(news)
                    }
                    goBackToList()
                } else {
                    Log.e("Error", "Save Error" + response.errorBody().toString())
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

    private fun updateNews(newsItemData: NewsData) {

        val retrofit = RetrofitClient.getInstance()
        val apiInterface = retrofit.create(ApiInterface::class.java)
        GlobalScope.launch {
            try {
                val id = PlaceholderContent.itemToId(newsItemData)
                val response = apiInterface.updateNews(id, newsItemData)
                if (response.isSuccessful()) {
                    //your code for handling success response
                    Log.d("News: ", response.body().toString())

                    val news: NewsData? = response.body()?.data
//                    if (news != null) {
////                        PlaceholderContent.addItem(emp)
//                    }
                    goBackToList()
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewsEditFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(actionType: String, position: Int) =
            NewsEditFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_NEWS_ACTION_TYPE, actionType)
                    putInt(ARG_NEWS_POSITION, position)
                }
            }
    }
}