package inha.teamproject

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView

class NextActivity_add : AppCompatActivity() {

    private var tv: TextView? = null
    private var searchbtn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next_add)

        tv = findViewById(R.id.tv) as TextView

        for (i in 0 until CustomAdapter_add.public_modelArrayList!!.size) {
            if (CustomAdapter_add.public_modelArrayList!!.get(i).getSelecteds()) {
                tv!!.text = tv!!.text.toString() + " " + CustomAdapter_add.public_modelArrayList!!.get(i).getAnimals()
            }
        }
        /*18/09/17 search버튼이 webcraslingactivity를 가리키도록 코드 추가*/
        searchbtn = findViewById(R.id.button_search)
        searchbtn?.setOnClickListener{
            val intent = Intent(this, WebCrawlingActivity::class.java)
            startActivity(intent)
        }

    }
}