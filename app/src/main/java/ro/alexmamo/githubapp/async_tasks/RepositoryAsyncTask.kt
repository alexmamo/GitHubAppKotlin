package ro.alexmamo.githubapp.async_tasks

import android.os.AsyncTask
import android.view.View
import android.widget.ProgressBar
import org.json.JSONArray
import ro.alexmamo.githubapp.Constants.REPOSITORIES_PER_PAGE
import ro.alexmamo.githubapp.R
import ro.alexmamo.githubapp.activities.RepositoriesActivity
import ro.alexmamo.githubapp.adapters.RepositoryAdapter
import ro.alexmamo.githubapp.models.Repository
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

class RepositoryAsyncTask(repositoriesActivity: RepositoriesActivity,
                          private val repositoryList: MutableList<Repository>,
                          private val repositoryAdapter: RepositoryAdapter) : AsyncTask<String, Void, List<Repository>>() {
    private val weakReference: WeakReference<RepositoriesActivity> = WeakReference(repositoriesActivity)

    override fun onPreExecute() {
        showProgressBar()
    }

    override fun doInBackground(vararg urls: String): List<Repository>? {
        if (urls.isEmpty()) {
            return null
        }

        val connection = URL(urls[0]).openConnection() as HttpURLConnection
        val inputStreamReader = InputStreamReader(connection.inputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        val result = StringBuilder()
        var line : String? = ""
        while (line != null) {
            line = bufferedReader.readLine()
            result.append("\n").append(line)
        }

        val jsonArray = JSONArray(result.toString())
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val id = jsonObject.getString("id")
            val name = jsonObject.getString("name")
            val fullName = jsonObject.getString("full_name")
            val url = jsonObject.getString("url")
            val repository = Repository(id, name, fullName, url)
            repositoryList.add(repository)

            if (i == REPOSITORIES_PER_PAGE - 1) {
                break
            }
        }

        return repositoryList
    }

    override fun onPostExecute(repositoryList: List<Repository>) {
        repositoryAdapter.notifyDataSetChanged()
        hideProgressBar()
    }

    private fun showProgressBar() {
        val repositoriesActivity = weakReference.get()
        val progressBar = repositoriesActivity!!.findViewById<ProgressBar>(R.id.progress_bar)
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        val repositoriesActivity = weakReference.get()
        val progressBar = repositoriesActivity!!.findViewById<ProgressBar>(R.id.progress_bar)
        progressBar.visibility = View.GONE
    }
}