package dvachmovie.fragment.start

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class StartVM @Inject constructor() : ViewModel() {
    val initText = MutableLiveData<String>("Initialization")
    val viewRetryBtn = MutableLiveData<Boolean>(false)
    val initGif by lazy {
        MutableLiveData<Int>()
    }
}
