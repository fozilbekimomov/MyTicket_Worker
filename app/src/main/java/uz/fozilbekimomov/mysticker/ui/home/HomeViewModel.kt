package uz.fozilbekimomov.mysticker.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.fozilbekimomov.mysticker.core.models.LocationModel
import uz.fozilbekimomov.mysticker.core.utils.LocationLiveData
import uz.fozilbekimomov.mysticker.core.utils.TAG


/**
 * Created by <a href="mailto: fozilbekimomov@gmail.com" >Fozilbek Imomov</a>
 *
 * @author fozilbekimomov
 * @version 1.0
 * @date 12/9/20
 * @project MySticker
 */


class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private var mStorageRef: StorageReference? = null
    private var db: FirebaseFirestore? = null

    private val locationData = LocationLiveData(application)

    init {
        mStorageRef = FirebaseStorage.getInstance().reference
        db = FirebaseFirestore.getInstance()
    }

    fun getLocationData() = locationData


    private val _errorData = MutableLiveData<String>()
    val errorData: LiveData<String> get() = _errorData

    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String> get() = _imageUrl
    fun uploadImage(byteArray: ByteArray, userName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                val user = userName.replace(" ", "")
                val riversRef: StorageReference? =
                    mStorageRef?.child("images/${user}/${System.currentTimeMillis()}.jpg")

                riversRef?.putBytes(byteArray)?.apply {
                    addOnSuccessListener {

                        val url = it.metadata?.path
                        _imageUrl.postValue(url)

                    }

                    addOnFailureListener {

                        _errorData.postValue(exception?.message)

                    }
                }
            }
        }
    }

    private val _userData = MutableLiveData<String>()
    val userData: LiveData<String> get() = _userData
    fun uploadUserData(imageUrl: String, userName: String, location: LocationModel) {

        Log.d(TAG, "uploadUserData:1 ${db != null}")


        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                val userN = userName.replace(" ", "")

                val userData: MutableMap<String, Any> = HashMap()

                userData["userName"] = userName
                userData["image_url"] = imageUrl
                userData["lat"] = location.latitude
                userData["long"] = location.longitude


                val collection =
                    db!!.collection("users")
                        .document(userN)
                        .collection("${System.currentTimeMillis()}")


                collection.add(userData)
                    .addOnSuccessListener { documentReference ->

                        _userData.postValue(documentReference.path)
                        Log.d(TAG, "uploadUserData: ${documentReference.id}")

                    }
                    .addOnFailureListener { e ->
                        Log.d(TAG, "uploadUserData: ${e.message}")
                        _errorData.postValue(e.message)
                    }
            }

            Log.d(TAG, "uploadUserData:2 ${db != null}")
        }
    }

}

