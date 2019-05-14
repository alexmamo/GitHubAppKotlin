package ro.alexmamo.githubapp

object Constants {
    const val TAG = "GitHubAppTag"
    private const val CLIENT_ID = "9ba4f9c12fa92045aa29"
    private const val CLIENT_SECRET = "816f71bd25f5b0e6890ee0532ac8e77c5c9ca581"
    const val SECURITY = "?client_id=$CLIENT_ID&client_secret=$CLIENT_SECRET"
    const val BASE_URL = "https://api.github.com/repositories$SECURITY"
    const val SINCE = "&since="
    const val CONTRIBUTORS = "/contributors"
    const val REPOSITORIES_PER_PAGE = 25
}