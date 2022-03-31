package art.tm.recyclerview1

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

internal class ArtRecycler(var data1: ArrayList<Data>) : RecyclerView.Adapter<ArtRecycler.ArtViewHolder>() {

    private var clieckedPosition: Int = -1
    private var clickedPositionText : String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtRecycler.ArtViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ArtViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArtRecycler.ArtViewHolder, position: Int) {
        val item = data1[position]
        holder.itemText.text = item.s
        holder.bool.isChecked = item.b
        holder.itemView.setOnClickListener {
            Log.i("CLICK", "CLICKED $position")
            clickedPositionText = item.s
            getClickedPositionText()
        }


        holder.itemView.setOnLongClickListener {
            clieckedPosition = holder.adapterPosition
            deleteItem()
        }
    }

    override fun getItemCount(): Int {
        return data1.size
    }

    private fun deleteItem(position: Int): Boolean {
        data1.removeAt(position)
        notifyDataSetChanged()
        return true
    }

    fun deleteItem(): Boolean {
        return deleteItem(clieckedPosition)
    }

    fun getClickedPositionText() : String {
        Log.i("CLICKED TEXT", clickedPositionText)
        return clickedPositionText
    }

    internal class ArtViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        var itemText : TextView = view.findViewById(R.id.text)
        var bool : CheckBox = view.findViewById(R.id.bool)
    }

}

