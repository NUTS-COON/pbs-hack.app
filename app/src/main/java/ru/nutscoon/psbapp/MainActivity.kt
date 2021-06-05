package ru.nutscoon.psbapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.nutscoon.psb.credit.CreditActivity
import ru.nutscoon.psb.settings.SettingsActivity
import ru.nutscoon.psb.storis.StorisView
import ru.nutscoon.psbapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initStoris()
        val creditBtn = findViewById<Button>(R.id.btn_credit)
        creditBtn.setOnClickListener {
            val myIntent = Intent(this, CreditActivity::class.java)
            this.startActivity(myIntent)
        }

        val offerBtn = findViewById<LinearLayout>(R.id.offer_btn)
        offerBtn.setOnClickListener {
            val myIntent = Intent(this, CreditActivity::class.java)
            myIntent.putExtra("show_offer", 2362)
            this.startActivity(myIntent)
        }

        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val myIntent = Intent(this, SettingsActivity::class.java)
                this.startActivity(myIntent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initStoris() {
        val storis = findViewById<StorisView>(R.id.storis_cards)
        storis.init()

        val storisHeader = findViewById<TextView>(R.id.tv_storis_header)
        storisHeader.text = "У вас ${storis.storisCount} новые истории"
    }
}