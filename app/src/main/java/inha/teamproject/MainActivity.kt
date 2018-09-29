package inha.teamproject

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {

    private val CAMERA_REQUEST_CODE = 5
    private val GALLERY_REQUEST_CODE = 6

    ///임시로 지정한 Array<String>//
    var animallist = arrayOf("Lionasdfa\naeasdfse", "Tiger1321324423sfzdfzsdfasefzsdadfaefsfaes", "Leopard", "Cat","cute cat", "ugly cat","and me..","213","423","132","4","5","8","0","9","4","2")
//ㅇ
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ///원래 코드18.09.26///
        /*
        camera_btn.setOnClickListener {
            Log.d("MainActivity", "onClick: launching camera.")


            val intent = Intent(this, MainCamera::class.java)

            startActivity(intent)
            //val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //startActivityForResult(intent, CAMERA_REQUEST_CODE)
        }
        */
        ///수정 18.09.26////
        camera_btn.setOnClickListener {
            Log.d("MainActivity", "onClick: launching camera.")

            //val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //startActivityForResult(intent, CAMERA_REQUEST_CODE)
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(callCameraIntent.resolveActivity(packageManager)!=null){
                startActivityForResult(callCameraIntent, CAMERA_REQUEST_CODE)
            }
        }

        gallery_btn.setOnClickListener {
            Log.d("MainActivity", "onClick: Go To Gallery")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, GALLERY_REQUEST_CODE)
        }

        /////////////////////////추가작성 18.09.26/////////////////////////////
        search_btn.setOnClickListener {
            Log.d("MainActivity", "onClick: Try_search")

            val intent = Intent(this, MainActivity_search_add::class.java)
            intent.putExtra("test",animallist)
            startActivity(intent)
        }
        //////////////////////////추가작성 끝 /////////////////////////////
    }
    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CAMERA_REQUEST_CODE && data!=null){
            Log.d("onActivityResult", "done taking a photo")

            selectedPhotoUri = data.data
            val uriToString = selectedPhotoUri.toString()

            Log.d("onActivityResult", uriToString)
            //bitmap 을 bytearray로 전송(putExtra에 bitmap을 보내주지 않아서)하기 위한 변환
            val image: Bitmap? = data.extras.get("data") as Bitmap
            val byteArrayOutputStream = ByteArrayOutputStream()
            image!!.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val imageBytes = byteArrayOutputStream.toByteArray()
            //보내는 작업 여기서 uri는 카메라에서 바로 보내는 걸 알기 위해 아래와 같이 변경
            val intent = Intent(this, SearchActivity::class.java)
            intent.putExtra("camera", "camera data")
            intent.putExtra("uri", uriToString)
            intent.putExtra("im",imageBytes)

            startActivity(intent)
        }else if(requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data!=null){
            Log.d("onActivityResult", "set image View")

            selectedPhotoUri = data.data
            val uriToString = selectedPhotoUri.toString()
            // val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            Log.d("onActivityResult", uriToString)

            val intent = Intent(this, SearchActivity::class.java)
            intent.putExtra("uri", uriToString)

            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.setting_menu -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
