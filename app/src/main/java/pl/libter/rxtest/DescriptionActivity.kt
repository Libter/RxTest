package pl.libter.rxtest

import android.app.Activity
import android.os.Bundle
import pl.libter.rxtest.api.Repository
import ru.noties.markwon.Markwon
import android.text.method.LinkMovementMethod
import android.widget.TextView
import ru.noties.markwon.renderer.SpannableRenderer
import ru.noties.markwon.SpannableConfiguration

class DescriptionActivity: Activity() {

    companion object {
        // https://stackoverflow.com/a/7984845
        lateinit var repository: Repository
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.description)

        RepositoriesAdapter.fill(findViewById(R.id.repository), repository)

        val description = findViewById<TextView>(R.id.description)
        description.movementMethod = LinkMovementMethod.getInstance()

        val parsed = Markwon.createParser().parse(repository.description)
        val text = SpannableRenderer().render(SpannableConfiguration.create(this), parsed)

        Markwon.unscheduleDrawables(description)
        Markwon.unscheduleTableRows(description)
        description.text = text
        Markwon.scheduleDrawables(description)
        Markwon.scheduleTableRows(description)

    }
}