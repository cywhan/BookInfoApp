/*
package inha.teamproject

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_camera.*


class MainCamera : AppCompatActivity() {

    val CAMERA_REQUEST_CODE =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        take1.setOnClickListener {}  //일단 비워둠
        take.setOnClickListener{
            val callCameraIntent = Intent (MediaStore.ACTION_IMAGE_CAPTURE)
            if(callCameraIntent.resolveActivity(packageManager) != null){
                startActivityForResult(callCameraIntent, CAMERA_REQUEST_CODE)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            CAMERA_REQUEST_CODE->{
                if(resultCode == Activity.RESULT_OK && data !=null){
                    photo.setImageBitmap(data.extras.get("data") as Bitmap)
                }
            }
            else->{
                Toast.makeText(this,"Unrecognized request code", Toast.LENGTH_SHORT).show()
            }
        }
    }



}
*/

///우선 전부 주석처리만 했습니다 18.09.26