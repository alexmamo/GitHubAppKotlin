package ro.alexmamo.githubapp.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ro.alexmamo.githubapp.R
import ro.alexmamo.githubapp.async_tasks.DownloadImageTask
import ro.alexmamo.githubapp.models.Contributor

class ContributorAdapter(private val context: Context, private val contributorList: List<Contributor>) : RecyclerView.Adapter<ContributorAdapter.ContributorViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ContributorViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_contributor, parent, false)
        return ContributorViewHolder(view)
    }

    override fun onBindViewHolder(contributorViewHolder: ContributorViewHolder, position: Int) {
        val contributor = contributorList[position]
        contributorViewHolder.bind(contributor)
    }

    override fun getItemCount(): Int {
        return contributorList.size
    }

    inner class ContributorViewHolder(private var view: View) : RecyclerView.ViewHolder(view) {
        fun bind(contributor: Contributor) {
            setLoginTextView(contributor.login)
            setAvatarUrlImageView(contributor.avatarUrl)
        }

        private fun setLoginTextView(login: String) {
            val loginTextView = view.findViewById<TextView>(R.id.login_text_view)
            loginTextView.text = login
        }

        private fun setAvatarUrlImageView(avatarUrl: String) {
            val downloadImageTask = DownloadImageTask(view)
            downloadImageTask.execute(avatarUrl)
        }
    }
}