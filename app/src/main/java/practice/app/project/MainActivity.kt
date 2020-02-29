package practice.app.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

class MainActivity : AppCompatActivity() {
    val list_tasks = arrayListOf<Task>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val transaction = supportFragmentManager.beginTransaction()
        if(item.itemId == R.id.menu_list){
            val fragment = ListFragment()
            transaction.replace(R.id.container_fragment, fragment)
        }
        if(item.itemId == R.id.menu_newTask){
            val fragment = NewTaskFragment()
            transaction.replace(R.id.container_fragment, fragment)
        }
        transaction.commit()
        return super.onOptionsItemSelected(item)
    }
}
