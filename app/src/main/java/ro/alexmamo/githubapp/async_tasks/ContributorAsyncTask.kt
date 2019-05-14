package ro.alexmamo.githubapp.async_tasks

import android.os.AsyncTask
import android.view.View
import android.widget.ProgressBar
import org.json.JSONArray
import ro.alexmamo.githubapp.R
import ro.alexmamo.githubapp.activities.RepositoryActivity
import ro.alexmamo.githubapp.adapters.ContributorAdapter
import ro.alexmamo.githubapp.models.Contributor
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

class ContributorAsyncTask(repositoryActivity: RepositoryActivity,
                           private val contributorList: MutableList<Contributor>,
                           private val contributorAdapter: ContributorAdapter) : AsyncTask<String, Void, List<Contributor>>() {
    private val weakReference: WeakReference<RepositoryActivity> = WeakReference(repositoryActivity)

    override fun onPreExecute() {
        showProgressBar()
    }

    override fun doInBackground(vararg urls: String): List<Contributor>? {
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
            val login = jsonObject.getString("login")
            val avatarUrl = jsonObject.getString("avatar_url")
            val contributor = Contributor(login, avatarUrl)
            contributorList.add(contributor)
        }

        return contributorList
    }

    override fun onPostExecute(contributorList: List<Contributor>) {
        contributorAdapter.notifyDataSetChanged()
        hideProgressBar()
    }

    private fun showProgressBar() {
        val repositoryActivity = weakReference.get()
        val progressBar = repositoryActivity!!.findViewById<ProgressBar>(R.id.progress_bar)
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        val repositoryActivity = weakReference.get()
        val progressBar = repositoryActivity!!.findViewById<ProgressBar>(R.id.progress_bar)
        progressBar.visibility = View.GONE
    }
}