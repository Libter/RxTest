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

    companion object {
        fun fill(view: View, repository: Repository) {
            view.findViewById<ImageView>(R.id.source).setImageResource(repository.source.icon)
            view.findViewById<TextView>(R.id.name).text = repository.name
            view.findViewById<TextView>(R.id.ownerName).text = repository.owner.name
            view.findViewById<ImageView>(R.id.ownerAvatar).setImageBitmap(repository.owner.avatar)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.repository, parent, false)
        fill(view, getItem(position)); return view
    }

}