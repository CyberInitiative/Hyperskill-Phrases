package org.hyperskill.phrases

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar
import kotlin.random.Random

class MainActivity : AppCompatActivity(), TimePickerFragment.OnTimeSetListener {

    private lateinit var addButton: FloatingActionButton
    private lateinit var reminderTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var phrasesAdapter: PhrasesAdapter

    private lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appDatabase = (application as PhraseApplication).database

        reminderTextView = findViewById(R.id.reminderTextView)
        reminderTextView.setOnClickListener {
            if (phrasesAdapter.getPhrases().isEmpty()) {
                Toast.makeText(this, "No phrases", Toast.LENGTH_LONG).show()
                reminderTextView.text = resources.getString(R.string.no_reminder_set)
            } else {
                TimePickerFragment().show(supportFragmentManager, TimePickerFragment.TAG)
            }
        }

        addButton = findViewById(R.id.addButton)
        addButton.setOnClickListener {
            val contentView = LayoutInflater.from(this).inflate(R.layout.phrase_dialog, null, false)
            AlertDialog.Builder(this)
                .setTitle("Add phrase dialog")
                .setView(contentView)
                .setPositiveButton(R.string.add) { _, _ ->
                    val editText = contentView.findViewById<EditText>(R.id.editText)
                    val phrase = Phrase(editText.text.toString())
                    appDatabase.phrases().insert(phrase)
                    phrasesAdapter.setPhrases(appDatabase.phrases().getAll())
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        phrasesAdapter = PhrasesAdapter() {
            appDatabase.phrases().delete(it)
            phrasesAdapter.setPhrases(appDatabase.phrases().getAll())

            if(phrasesAdapter.getPhrases().isEmpty()){
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

                for(pendingIntent in AlarmManagerHelper.pendingIntentMap){
                    alarmManager.cancel(pendingIntent.value)
                }
                reminderTextView.text = resources.getString(R.string.no_reminder_set)
            }
        }
        phrasesAdapter.setPhrases(appDatabase.phrases().getAll())
        recyclerView.adapter = phrasesAdapter

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = createNotificationChannel()
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): NotificationChannel {
        val name = getString(R.string.notification_channel_name)
        val descriptionText = getString(R.string.notification_channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(
            getString(R.string.notification_channel_id),
            name,
            importance
        ).apply {
            description = descriptionText
        }
        return channel
    }

    private fun setAlarm(hour: Int, minute: Int, randomPhrase: Phrase) {
        val intent = Intent(applicationContext, AlarmReceiver::class.java)
        intent.putExtra(AlarmReceiver.PHRASE_KEY, randomPhrase.phrase)

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            1,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        AlarmManagerHelper.addPendingIntent(randomPhrase.phrase, pendingIntent)

        val alarmManager: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()

            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    override fun sendTime(hour: Int, minute: Int) {
        val timeInfo = resources.getString(R.string.reminder_time, hour, minute)
        reminderTextView.text = timeInfo

        val randomIndex = Random.nextInt(phrasesAdapter.getPhrases().size)
        val randomPhrase = phrasesAdapter.getPhrases()[randomIndex]

        setAlarm(hour, minute, randomPhrase)
    }

}