package inha.teamproject

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import java.util.ArrayList

class MainActivity_search_add : AppCompatActivity() {

    private var lv: ListView? = null
    private var modelArrayList: ArrayList<StringsearchActivity_add>? = null
    private var customAdapter: CustomAdapter_add? = null
    private var btnselect: Button? = null
    private var btndeselect: Button? = null
    private var btnnext: Button? = null
    //private var animallist = arrayOf("Lion", "Tiger", "Leopard", "Cat","cute cat", "ugly cat","and me..")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_add)
        var animallist = intent!!.getStringArrayExtra("test") as Array
        animallist = animallist.plus("done?")
        lv = findViewById(R.id.lv) as ListView
        btnselect = findViewById(R.id.select) as Button
        btndeselect = findViewById(R.id.deselect) as Button
        btnnext = findViewById(R.id.next) as Button

        modelArrayList = getModel(false,animallist)
        customAdapter = CustomAdapter_add(this, modelArrayList!!)
        lv!!.adapter = customAdapter

        btnselect!!.setOnClickListener {
            modelArrayList = getModel(true,animallist)
            customAdapter = CustomAdapter_add(this, modelArrayList!!)
            CustomAdapter_add.public_modelArrayList = modelArrayList as ArrayList<StringsearchActivity_add>
            lv!!.adapter = customAdapter
        }
        btndeselect!!.setOnClickListener {
            modelArrayList = getModel(false,animallist)
            customAdapter = CustomAdapter_add(this, modelArrayList!!)
            CustomAdapter_add.public_modelArrayList = modelArrayList as ArrayList<StringsearchActivity_add>
            lv!!.adapter = customAdapter
        }
        btnnext!!.setOnClickListener {
            val intent = Intent(this, NextActivity_add::class.java)
            startActivity(intent)
        }
    }
    private fun getModel(isSelect: Boolean, animallist: Array<String>): ArrayList<StringsearchActivity_add> {
        val list = ArrayList<StringsearchActivity_add>()

        Log.d("SearchActivity", "Done")
        for (i in 0..(animallist.size-1)) {

            val model = StringsearchActivity_add()
            model.setSelecteds(isSelect)
            model.setAnimals(animallist[i])
            list.add(model)
        }
        return list
    }
}
