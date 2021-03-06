package dvachmovie.repository

import dvachmovie.api.FileItem

interface FourChanRepository {
    suspend fun getNumThreadsFromCatalog(board: String) : List<Pair<Int, String>>
    suspend fun getConcreteThreadByNum(board: String, numThread: String, nameThread: String): List<FileItem>
}
