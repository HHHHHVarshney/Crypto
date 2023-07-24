package com.example.crypto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.crypto.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvAdapter: rv_adapter
    private lateinit var data:ArrayList<rv_model>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        data = ArrayList()
        apiData
        rvAdapter = rv_adapter(this,data)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = rvAdapter

        binding.search.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                val filteredData = ArrayList<rv_model>()
                for(item in data){
                    if(item.name.lowercase(Locale.getDefault()).contains(p0.toString().lowercase(
                            Locale.getDefault()))){
                        filteredData.add(item)
                    }
                }
                if(filteredData.isEmpty()){
                    Toast.makeText(this@MainActivity,"No Data Available",Toast.LENGTH_LONG).show()
                }else{
                    rvAdapter.changeData(filteredData)
                }
            }

        })
    }

    val apiData:Unit
    get(){
        val url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest"

        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest:JsonObjectRequest = object: JsonObjectRequest(Request.Method.GET,url,null, Response.Listener {
            response ->
            binding.progressBar.isVisible = false
            try {
                val dataArray = response.getJSONArray("data")
                for(i in 0 until dataArray.length()){
                    val dataObject = dataArray.getJSONObject(i)
                    val symbol = dataObject.getString("symbol")
                    val name = dataObject.getString("name")
                    val quote = dataObject.getJSONObject("quote")
                    val USD = quote.getJSONObject("USD")
                    val price = String.format("$"+"%.2f",  USD.getDouble("price"))
                    data.add(rv_model(name,symbol,price))
                }
                rvAdapter.notifyDataSetChanged()
            }catch (e:Exception){
                Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
            }
        },Response.ErrorListener {
            Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["X-CMC_PRO_API_KEY"] = "328294c2-e267-4ea7-80b3-f97158f9abb9"
                return headers
            }
        }
        queue.add(jsonObjectRequest)

    }
}