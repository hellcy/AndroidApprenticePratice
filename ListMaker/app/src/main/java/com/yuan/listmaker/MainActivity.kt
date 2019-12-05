package com.yuan.listmaker

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_list.*

class MainActivity : AppCompatActivity(), ListSelectionFragment.OnListItemFragmentInteractionListener
{
    // reference to the fragment, so we can use the logic in fragment
    private var listSelectionFragment: ListSelectionFragment = ListSelectionFragment.newInstance()

    val listDataManager: ListDataManager = ListDataManager(this)
    lateinit var listsRecyclerView: RecyclerView // recyclerView is going to be created sometime in the future, lazy in Swift

    private var fragmentContainer: FrameLayout? = null
    companion object {
        val INTENT_LIST_KEY = "list"
        val LIST_DETAIL_REQUEST_CODE = 123
    }

    private var largeScreen = false
    private var listFragment : ListDetailFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setSupportActionBar(toolbar)

        listSelectionFragment = supportFragmentManager.findFragmentById(R.id.list_selection_fragment) as ListSelectionFragment

        fragmentContainer = findViewById(R.id.fragment_container)

        largeScreen = fragmentContainer != null

        fab.setOnClickListener { view ->
            showCreateListDialog()
        }
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

    override fun onListItemClicked(list: TaskList) {
        showListDetail(list)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data:
    Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 1
        if (requestCode == LIST_DETAIL_REQUEST_CODE) {
            // 2
            data?.let {
                // 3
                listSelectionFragment.saveList(data.getParcelableExtra<TaskList>(INTENT_LIST_KEY))
            }
        }
    }

    private fun showCreateListDialog() {
        val dialogTitle = getString(R.string.name_of_list)
        val positiveButtonTitle = getString(R.string.create_list)

        val builder = AlertDialog.Builder(this)
        val listTitleEditText = EditText(this) // input text field
        listTitleEditText.inputType = InputType.TYPE_CLASS_TEXT

        builder.setTitle(dialogTitle)
        builder.setView(listTitleEditText)

        // save list to sharedPreference and add to recyclerView every time when we press Create Button
        builder.setPositiveButton(positiveButtonTitle) { dialog, _ ->
            val list = TaskList(listTitleEditText.text.toString())
            listSelectionFragment.addList(list)

            dialog.dismiss()
            showListDetail(list)
        }

        builder.create().show()
    }

    private fun showListDetail(list: TaskList) {
        if (!largeScreen) {
            val listDetailIntent = Intent(this, ListDetailActivity::class.java)

            listDetailIntent.putExtra(INTENT_LIST_KEY, list)

            // adds the expectation
            //that MainActivity.kt will hear back from ListDetailActivity.kt once it has finished
            //being onscreen
            startActivityForResult(listDetailIntent, LIST_DETAIL_REQUEST_CODE)
        } else {
            title = list.name

            val listFragment = ListDetailFragment.newInstance(list)

            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, listFragment, getString(R.string.list_fragment_tag))
                .addToBackStack(null).commit()

            fab.setOnClickListener {view -> showCreateTaskDialog()}
        }
    }

    private fun showCreateTaskDialog() {
        val taskEditText = EditText(this)
        taskEditText.inputType = InputType.TYPE_CLASS_TEXT
        AlertDialog.Builder(this)
            .setTitle(R.string.task_to_add)
            .setView(taskEditText)
            .setPositiveButton(R.string.add_task) { dialog, _ ->
                val task = taskEditText.text.toString()
                listFragment?.addTask(task)
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        title = resources.getString(R.string.app_name)
        // 1
        listFragment?.list?.let {
            listSelectionFragment?.listDataManager?.saveList(it)
        }
        // 2
        if (listFragment != null) {
            supportFragmentManager
                .beginTransaction()
                .remove(listFragment ?: return)
                .commit()
            listFragment = null
        }
        // 3
        fab.setOnClickListener { view ->
            showCreateListDialog()
        }
    }
}
