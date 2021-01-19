package navigation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.monggu.instargramcloenkt.R
import com.monggu.instargramcloenkt.databinding.ActivityAddphotoBinding
import com.monggu.instargramcloenkt.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*


class AddphotoActivity : AppCompatActivity() {
    var PICK_IMAGE_FROM_ALBUM = 0
    var storage : FirebaseStorage? =null
    var photoUri : Uri? = null
    var binding = ActivityAddphotoBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //파이어베이스 스토리지 불러오기
        storage = FirebaseStorage.getInstance()

        //카메라 앨범 오픈
        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)

        //업로드 작동
        binding.btnPhoto.setOnClickListener {
            contentUpload()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_FROM_ALBUM){
            if (resultCode == Activity.RESULT_OK){
                photoUri = data?.data
                binding.imgPhoto.setImageURI(photoUri)
            }else{
                finish()
            }
        }
    }
    //파일 네임 만들기
    fun contentUpload() {
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName = "IMAGE_" + timestamp + "_.png"
        var storageRef = storage?.reference?.child("images")?.child(imageFileName)

        //파일 업로드
        storageRef?.putFile(photoUri!!)?.addOnSuccessListener {
            Toast.makeText(this,getString(R.string.upload_success),Toast.LENGTH_SHORT).show()
        }
    }
}