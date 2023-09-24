package com.codepath.bestsellerlistapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.codepath.asynchttpclient.callback.TextHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject

// --------------------------------//
// CHANGE THIS TO BE YOUR API KEY  //
// --------------------------------//
private const val API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed"

class MovieFragment : Fragment(), OnListFragmentInteractionListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.movie_fragment_list, container, false)
        val progressBar = view.findViewById<View>(R.id.progress) as ContentLoadingProgressBar
        val recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
        val context = view.context
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        updateAdapter(progressBar, recyclerView)

        return view
    }

    private fun updateAdapter(progressBar: ContentLoadingProgressBar, recyclerView: RecyclerView) {
        progressBar.show()

        // Create and set up an AsyncHTTPClient() here
        val client = AsyncHttpClient()
        val apiUrl = "https://api.themoviedb.org/3/movie/11?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"
        val params = RequestParams()
        params["api-key"] = API_KEY

        // Using the client, perform the HTTP request
        client[
            apiUrl,
            params,
            object : JsonHttpResponseHandler()

            {

                override fun onSuccess(
                    statusCode: Int,
                    headers: Headers,
                    response: JSON
                ) {
                    //Log.d("DEBUG ARRAY", json.jsonArray.toString())
                    //Log.d("DEBUG OBJECT", json.jsonObject.toString())

                    progressBar.hide()  // The wait for a response is over

                    //TODO - Parse JSON into Models
                    val movie = Gson().fromJson(response.toString(), Movie::class.java)
                    val movieList = listOf(movie)
                    recyclerView.adapter = MovieRecyclerViewAdapter(movieList, this@MovieFragment)

                    /*Help with Debugging to try to find the fatal exception cause
                    try {
                        // Parse the JSON response into a Movie object
                        val movie = Gson().fromJson(response.toString(), Movie::class.java)

                        // Now you have a single Movie object, you can do something with it
                        // For example, you can display it in the RecyclerView adapter
                        val movieList = listOf(movie)
                        recyclerView.adapter = MovieRecyclerViewAdapter(movieList, this@MovieFragment)
                    } catch (e: Exception) {
                        // Handle parsing exception
                        Log.e("MovieFragment", "Error parsing JSON: ${e.message}")
                        Log.e("MovieFragment", "Raw JSON response: $response")
                        // Handle the error gracefully, e.g., display an error message to the user.
                    }*/

                    //val resultsJSON: JSONObject = json.jsonObject.getJSONObject("results")
                    //val moviesRawJSON: String = resultsJSON.getJSONArray("movies").toString()

                    /*val gson = Gson() //Step 2c
                    val arrayMovieType = object : TypeToken<List<Movie>>() {}.type
                    val models: List<Movie> =
                            gson.fromJson(json.jsonArray.toString(), arrayMovieType)
                        recyclerView.adapter =
                            MovieRecyclerViewAdapter(models, this@MovieFragment)*/
                }
             //The onFailure function gets called when HTTP response status is "4XX" (eg. 401, 403, 404)

                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    errorResponse: String,
                    t: Throwable?
                ) {
                    // The wait for a response is over
                    progressBar.hide()
                    Log.e(
                        "MovieFragment",
                        "API request failed with status code $statusCode"
                    )
                    // If the error is not null, log it!
                    t?.message?.let {
                        Log.e("MovieFragment", it)
                    }
                }
            }]

        /*
        * What happens when a particular book is clicked.
        */
    }
    override fun onItemClick(item: Movie) {
        Toast.makeText(context, "Title: ${item.title}\nDescription: ${item.description}", Toast.LENGTH_LONG).show()
    }
}


