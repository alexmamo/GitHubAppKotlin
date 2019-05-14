package ro.alexmamo.githubapp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.AbsListView
import kotlinx.android.synthetic.main.activity_repositories.*
import ro.alexmamo.githubapp.Constants.BASE_URL
import ro.alexmamo.githubapp.Constants.REPOSITORIES_PER_PAGE
import ro.alexmamo.githubapp.Constants.SINCE
import ro.alexmamo.githubapp.R
import ro.alexmamo.githubapp.adapters.RepositoryAdapter
import ro.alexmamo.githubapp.async_tasks.RepositoryAsyncTask
import ro.alexmamo.githubapp.models.Repository
import java.util.ArrayList

class RepositoriesActivity : AppCompatActivity() {
    private val repositoryList = ArrayList<Repository>()
    private var isScrolling = false
    private var isLastItemReached = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repositories)
        initViews()
    }

    private fun initViews() {
        val repositoryAdapter = RepositoryAdapter(this, repositoryList)
        repository_recycler_view.adapter = repositoryAdapter
        getRepositories(this@RepositoriesActivity, repositoryAdapter, BASE_URL)

        val onScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()
                val visibleItemCount = linearLayoutManager.childCount
                val totalItemCount = linearLayoutManager.itemCount

                if (isScrolling && firstVisibleItemPosition + visibleItemCount == totalItemCount && !isLastItemReached) {
                    isScrolling = false
                    val lastRepository = repositoryList[repositoryList.size - 1]
                    val lastRepositoryId = lastRepository.id
                    val nextPageUrl = BASE_URL + SINCE + lastRepositoryId
                    getRepositories(this@RepositoriesActivity, repositoryAdapter, nextPageUrl)

                    if (repositoryList.size < REPOSITORIES_PER_PAGE) {
                        isLastItemReached = true
                    }
                }
            }
        }
        repository_recycler_view.addOnScrollListener(onScrollListener)
    }

    private fun getRepositories(repositoriesActivity: RepositoriesActivity,repositoryAdapter: RepositoryAdapter, url: String) {
        val repositoryAsyncTask = RepositoryAsyncTask(repositoriesActivity, repositoryList, repositoryAdapter)
        repositoryAsyncTask.execute(url)
    }
}