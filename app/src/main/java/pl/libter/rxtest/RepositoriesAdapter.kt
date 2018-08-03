package pl.libter.rxtest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import pl.libter.rxtest.api.Repository

class RepositoriesAdapter(context: Context, list: ArrayList<Repository>): ArrayAdapter<Repository>(context, 0, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val row = convertView ?: LayoutInflater.from(context).inflate(R.layout.repository, parent, false)
        val repository = getItem(position)
        row.findViewById<ImageView>(R.id.source).setImageResource(repository.source.icon)
        row.findViewById<TextView>(R.id.name).text = repository.name
        row.findViewById<TextView>(R.id.ownerName).text = repository.owner.name
        row.findViewById<ImageView>(R.id.ownerAvatar).setImageBitmap(repository.owner.avatar)
        return row
    }

}