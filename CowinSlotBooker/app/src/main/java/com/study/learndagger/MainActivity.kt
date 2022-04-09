package com.study.learndagger

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.study.learndagger.car.Car
import javax.inject.Inject

//Directed Acyclic Graph - Dagger
//JSR 330 Java Specification Request
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var car1: Car

    @Inject
    lateinit var car2: Car

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

//        val create = DaggerActivityComponent.builder().horsePower(1400).engineCapacity(1000)
//            .appComponent((application as CSBApp).appComponent).build()
//        create.inject(this)
//        car1.drive()
//        car2.drive()

//        val appComponent = (application as CSBApp).appComponent.getActivityComponent(DieselEngineModule(1400))
//        appComponent.inject(this)

        val appComponent =
            (application as CSBApp).appComponent.activityComponentBuilder.engineCapacity(500)
                .horsePower(1400).build()
        appComponent.inject(this)

        car1.drive()
        car2.drive()

        val testClass = TestClass();
        testClass.testFunction()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}