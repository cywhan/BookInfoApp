package inha.teamproject

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import android.graphics.BitmapFactory



class SearchActivity : AppCompatActivity() {

    companion object {
        const val YOUR_API_KEY = "YOUR_API_KEY"
        const val MAX_DIMENSION = 1200
    }

    private val stringBuilder = StringBuilder()

    // 최종적으로 찾은 text는 사용할 수 있게 여기다가 저장
    private val findTextList = mutableListOf<String>()

    private var selectedUri: Uri? = null
    private var bitmap:Bitmap? = null
    //버튼들 가져오기 위한 선언
    private var btn_detect: Button? = null
    private var iv_main: ImageView? = null
    private var tv_main: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        btn_detect = findViewById(R.id.detect) as Button
        iv_main = findViewById(R.id.photo) as ImageView
        tv_main = findViewById(R.id.tv_main) as TextView
        //카메라에서 바로 넘어온 경우
        if(intent.getStringExtra("uri") == "camera data" ) {
            //bytearray를 다시 bitmap으로
            val imBytes = intent.getByteArrayExtra("im")
            val bmp = BitmapFactory.decodeByteArray(imBytes, 0, imBytes.size)
            Log.d("MainActivity", imBytes.size.toString())

            bitmap = scaleBitmapDown(bmp!!, MAX_DIMENSION)
            iv_main!!.setImageBitmap(bitmap)
        }//갤러리에서 넘어온 경우
        else{
            //앞에서 String으로 변환된 uri를 다시 uri형태로 바꾼다
            val myUri = Uri.parse(intent.getStringExtra("uri"))
            bitmap = scaleBitmapDown(MediaStore.Images.Media.getBitmap(contentResolver, myUri), MAX_DIMENSION)
            iv_main!!.setImageBitmap(bitmap)
        }
        // 검색 시작 버튼
        btn_detect!!.setOnClickListener {
            // bitmap 이미지를 ByteArray로 변환하는 과정
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)
            val imageBytes = byteArrayOutputStream.toByteArray()


            if(imageBytes!=null)
                detectText(imageBytes)

            val result = stringBuilder.toString()
            Log.d("MainActivity", "result = $result")

            tv_main!!.text = result
        }
    }
    //아래 함수는 현재 사용하지 않습니다 원래 한솔씨 작업창에서 갤러리 누르면 동작하는 부분인데
    //전에 있던 갤러리 버튼으로 연동하기 때문입니다
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            Log.d("MainActivity", "Photo was selected")

            selectedUri = data.data

            // 이미지 크기 줄이고 imageView에 표시
            // bitmap = scaleBitmapDown(MediaStore.Images.Media.getBitmap(contentResolver, selectedUri), MAX_DIMENSION)
            // iv_main!!.setImageBitmap(bitmap)
        }
    }


    private fun detectText(imageString: ByteArray){

        // Base64로 encoding
        val imgData = Base64.encodeToString(imageString, Base64.DEFAULT)
        //val imgData = Base64.encode(imageString, Base64.DEFAULT)
        Log.d("MainActivity", "imgData = $imgData")

        // 요청보낼 Json 만드는 곳
        val jsonObj = JSONObject("""{
  "requests": [
    {
      "image": {
        "content": "$imgData"
      },
      "features": [
        {
          "type": "TEXT_DETECTION"
        }
      ]
    }
  ]
}""")

        // POST API 를 보내야 되는데 안드로이드는 요청을 백그라운드에서 해야돼서
        // 백그라운드 작업을 편하게 하기위해 Volley 사용
        val url = "https://vision.googleapis.com/v1/images:annotate?key=$YOUR_API_KEY"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, jsonObj, Response.Listener { response ->
            // 응답 받는거 성공 시
            Log.d("MainActivity", "response = $response")

            // 응답 Json 안에 2중으로 Array 가 있어서 이렇게 속으로 들어갔는데 간단히 할 필요가 있을 것 같음
            val responseArray = response.getJSONArray("responses")
            val testAnnotations = responseArray.getJSONObject(0).getJSONArray("textAnnotations")
            Log.d("MainActivity", "testAnnotations = $testAnnotations")

            // 찾은 text 표시용
            val message = StringBuilder("사진속에 있는 문자들\n\n")

            // 들어간 json에서 찾은 text 하나씩 message, findTextList에 추가
            for(i in 0 until testAnnotations.length()){
                val responseText = testAnnotations.getJSONObject(i)
                Log.d("MainActivity", "find Text in $i's ward = ${responseText.getString("description")}")
                message.append("${responseText.getString("description")}\n")
                findTextList.add(responseText.getString("description"))
            }

            // textView에 표시
            tv_main!!.text = message.toString()

        }, Response.ErrorListener {
            // 응답 받는거 실패 시
            Log.d("MainActivity", "do json but error") })


        // Volley 시작
        val queue = Volley.newRequestQueue(this)
        queue.add(jsonObjectRequest)
    }

    // 사진 찍은거 그냥 보내면 이미지가 너무 커서 사이즈 줄이는거 Sample APP에 있는거 그대로 사용
    private fun scaleBitmapDown(bitmap: Bitmap, maxDimension: Int): Bitmap {

        val originalWidth = bitmap.width
        val originalHeight = bitmap.height
        var resizedWidth = maxDimension
        var resizedHeight = maxDimension

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension
            resizedWidth = (resizedHeight * originalWidth.toFloat() / originalHeight.toFloat()).toInt()
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension
            resizedHeight = (resizedWidth * originalHeight.toFloat() / originalWidth.toFloat()).toInt()
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension
            resizedWidth = maxDimension
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false)
    }
}