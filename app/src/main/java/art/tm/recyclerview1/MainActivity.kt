package art.tm.recyclerview1

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import art.tm.recyclerview1.databinding.ActivityMainBinding
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {


    private var data1 = ArrayList<Data>()


    private lateinit var recyclerAdapter : ArtRecycler
    //private lateinit var recyclerView : RecyclerView

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //   recyclerView = findViewById(R.id.recycler)
       // val layoutManager = LinearLayoutManager(applicationContext)
      //  layoutManager.orientation = RecyclerView.VERTICAL
     //   recyclerView.layoutManager = layoutManager
        recyclerAdapter = ArtRecycler(data1)
        binding.recycler.adapter = recyclerAdapter

        registerForContextMenu(binding.buttonContext)

        prepareList()

        binding.buttonDodaj.setOnClickListener {
            data1.add(Data(binding.editItem.text.toString(), false));
            recyclerAdapter.notifyDataSetChanged()
        }

        binding.buttonZapisz.setOnClickListener {
            zapisz()
        }

        binding.buttonWczytaj.setOnClickListener {
            wczytaj()
        }
    }


    private fun prepareList() {
       data1.add(Data("spodnie1", false))
       data1.add(Data("koszula1", true))
       data1.add(Data("buty1", false))
       recyclerAdapter.notifyDataSetChanged()
   }

    private fun zapisz()  {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "text/*"
        intent.putExtra(Intent.EXTRA_TITLE, "lista.txt")
        launchCreateDocument.launch(intent)
    }

    private var launchCreateDocument = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val os = result.data?.data?.let { contentResolver.openOutputStream(it, "wa") }
            val bos = BufferedOutputStream(os)
            bos.bufferedWriter(Charset.defaultCharset())

            for  (i in 0 until data1.size) {
                bos.write(data1[i].s.toByteArray(Charset.defaultCharset()))
                bos.write("\n".toByteArray(Charset.defaultCharset()))
            }
            bos.close()

            Log.i("INTENT", result.data?.data.toString())
        }
    }

    private fun wczytaj() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "text/*"
        launchOpenDocument.launch(intent)
    }


    private var launchOpenDocument = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val is1 = result.data?.data?.let { contentResolver.openInputStream(it) }
            var s: List<String> = is1?.bufferedReader(Charset.defaultCharset())?.readLines()!!
            is1.close()
            recyclerAdapter.data1.removeAll(data1)

            for (element in s) {
                data1.add(Data(element.toString(),false))
            }
            recyclerAdapter.notifyDataSetChanged()
        }
    }

    //Context Menu Start

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?) {

        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.close_app -> {
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    //Context Menu Stop


}






