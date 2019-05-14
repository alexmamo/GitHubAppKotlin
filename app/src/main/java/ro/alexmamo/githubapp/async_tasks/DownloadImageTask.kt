package ro.alexmamo.githubapp.async_tasks

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import ro.alexmamo.githubapp.Constants.TAG
import ro.alexmamo.githubapp.R
import java.lang.ref.WeakReference

class DownloadImageTask(itemView: View) : AsyncTask<String, Void, Bitmap>() {
    private val weakReference: WeakReference<View> = WeakReference(itemView)

    override fun onPreExecute() {
        showProgressBar()
    }

    override fun doInBackground(vararg urls: String): Bitmap? {
        val avatarUrl = urls[0]
        var bitmap: Bitmap? = null
        try {
            val inputStream = java.net.URL(avatarUrl).openStream()
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
        return bitmap
    }

    override fun onPostExecute(result: Bitmap) {
        val itemView = weakReference.get()
        if (itemView != null) {
            val avatarUrlImageView = itemView.findViewById<ImageView>(R.id.avatar_url_image_view)
            avatarUrlImageView.setImageBitmap(result)
            hideProgressBar()
        }
    }

    private fun showProgressBar() {
        val itemView = weakReference.get()
        val progressBar = itemView!!.findViewById<ProgressBar>(R.id.progress_bar)
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        val itemView = weakReference.get()
        val progressBar = itemView!!.findViewById<ProgressBar>(R.id.progress_bar)
        progressBar.visibility = View.GONE
    }
}