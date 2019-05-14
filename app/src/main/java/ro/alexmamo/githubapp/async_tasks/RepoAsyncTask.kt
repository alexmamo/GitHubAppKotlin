package ro.alexmamo.githubapp.async_tasks

import android.os.AsyncTask
import android.widget.TextView
import org.json.JSONObject
import ro.alexmamo.githubapp.R
import ro.alexmamo.githubapp.activities.RepositoryActivity
import ro.alexmamo.githubapp.models.Repo
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

class RepoAsyncTask(repositoriesActivity: RepositoryActivity) : AsyncTask<String, Void, Repo>() {
    private val weakReference: WeakReference<RepositoryActivity> = WeakReference(repositoriesActivity)

    override fun doInBackground(vararg urls: String): Repo? {
        val repo: Repo?

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

        val jsonObject = JSONObject(result.toString())
        val size = jsonObject.getInt("size")
        val stargazersCount = jsonObject.getInt("stargazers_count")
        val forksCount = jsonObject.getInt("forks_count")
        repo = Repo(size, stargazersCount, forksCount)

        return repo
    }

    override fun onPostExecute(repo: Repo) {
        setSizeTextView(repo)
        setStargazersCountTextView(repo)
        setForksCountTextView(repo)
    }

    private fun setSizeTextView(repo: Repo) {
        val repositoryActivity = weakReference.get()
        val sizeTextView = repositoryActivity!!.findViewById<TextView>(R.id.size_text_view)
        val size = "Size of the repository: " + repo.size
        sizeTextView.text = size
    }

    private fun setStargazersCountTextView(repo: Repo) {
        val repositoryActivity = weakReference.get()
        val sizeTextView = repositoryActivity!!.findViewById<TextView>(R.id.stargazers_count_text_view)
        val stargazersCount = "Stargazers Count: " + repo.stargazersCount
        sizeTextView.text = stargazersCount
    }

    private fun setForksCountTextView(repo: Repo) {
        val repositoryActivity = weakReference.get()
        val sizeTextView = repositoryActivity!!.findViewById<TextView>(R.id.forks_count_text_view)
        val forksCount = "Forks Count: " + repo.forksCount
        sizeTextView.text = forksCount
    }
}