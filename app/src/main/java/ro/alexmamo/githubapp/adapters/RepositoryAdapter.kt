package ro.alexmamo.githubapp.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ro.alexmamo.githubapp.R
import ro.alexmamo.githubapp.activities.RepositoryActivity
import ro.alexmamo.githubapp.models.Repository

class RepositoryAdapter(private val context: Context, private val repositoryList: List<Repository>) : RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, i: Int): RepositoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_repository, parent, false)
        return RepositoryViewHolder(view)
    }

    override fun onBindViewHolder(repositoryViewHolder: RepositoryViewHolder, position: Int) {
        val repository = repositoryList[position]
        repositoryViewHolder.bind(repository)
    }

    override fun getItemCount(): Int {
        return repositoryList.size
    }

    inner class RepositoryViewHolder internal constructor(private val view: View) : RecyclerView.ViewHolder(view) {
        internal fun bind(repository: Repository) {
            setIdTextView(repository.id)
            setNameTextView(repository.name)
            setFullNameTextView(repository.fullName)

            view.setOnClickListener {
                val repositoryActivityIntent = Intent(context, RepositoryActivity::class.java)
                repositoryActivityIntent.putExtra("repository", repository)
                context.startActivity(repositoryActivityIntent)
            }
        }

        private fun setIdTextView(id: String) {
            val idTextView = view.findViewById<TextView>(R.id.id_text_view)
            idTextView.text = context.getString(R.string.starting_id, id)
        }

        private fun setNameTextView(name: String) {
            val nameTextView = view.findViewById<TextView>(R.id.name_text_view)
            nameTextView.text = name
        }

        private fun setFullNameTextView(fullName: String) {
            val fullNameTextView = view.findViewById<TextView>(R.id.full_name_text_view)
            fullNameTextView.text = fullName
        }
    }
}