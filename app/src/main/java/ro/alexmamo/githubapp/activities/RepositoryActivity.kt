package ro.alexmamo.githubapp.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_repository.*
import ro.alexmamo.githubapp.Constants.CONTRIBUTORS
import ro.alexmamo.githubapp.Constants.SECURITY
import ro.alexmamo.githubapp.R
import ro.alexmamo.githubapp.adapters.ContributorAdapter
import ro.alexmamo.githubapp.async_tasks.ContributorAsyncTask
import ro.alexmamo.githubapp.async_tasks.RepoAsyncTask
import ro.alexmamo.githubapp.models.Contributor
import ro.alexmamo.githubapp.models.Repository
import java.util.ArrayList

class RepositoryActivity : AppCompatActivity() {
    private val contributorList = ArrayList<Contributor>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository)
        enableHomeButton()
        val repository = repositoryFromIntent
        title = repository.name
        getRepo(repository.url)
        initViews(repository.url)
    }

    private fun enableHomeButton() {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private val repositoryFromIntent: Repository get() = intent.getSerializableExtra("repository") as Repository

    private fun getRepo(url: String) {
        val repoUrl = url + SECURITY
        val repoAsyncTask = RepoAsyncTask(this@RepositoryActivity)
        repoAsyncTask.execute(repoUrl)
    }

    private fun initViews(url: String) {
        contributors_recycler_view.layoutManager = GridLayoutManager(applicationContext, 2)
        val contributorAdapter = ContributorAdapter(this, contributorList)
        contributors_recycler_view.adapter = contributorAdapter
        getContributors(contributorAdapter, url)
    }

    private fun getContributors(contributorAdapter: ContributorAdapter, url: String) {
        val contributorsUrl = url + CONTRIBUTORS + SECURITY
        val contributorAsyncTask = ContributorAsyncTask(this@RepositoryActivity, contributorList, contributorAdapter)
        contributorAsyncTask.execute(contributorsUrl)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(menuItem)
    }
}