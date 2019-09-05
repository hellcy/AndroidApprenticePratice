package com.yuan.listmaker

import android.content.Context
import android.preference.PreferenceManager
class ListDataManager(val context: Context) {
    fun saveList(list: TaskList) {
        // 1
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context).edit()
        // 2 Write TaskList to SharedPreferences as set of Strings, passing in the name of your
        //list as the key. Since the tasks in TaskList is an array of strings, we can’t store it
        //directly in a string. So you convert the tasks in TaskList to a HashSet which we can
        //then pass as the value to be saved
        sharedPreferences.putStringSet(list.name, list.tasks.toHashSet())
        // 3
        sharedPreferences.apply()
    }

    fun readLists(): ArrayList<TaskList> {
        // 1
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        // 2
        val sharedPreferenceContents = sharedPreferences.all
        // 3
        val taskLists = ArrayList<TaskList>()
        // 4
        for (taskList in sharedPreferenceContents) {
            val itemsHashSet = taskList.value as HashSet<String>
            val list = TaskList(taskList.key, ArrayList(itemsHashSet))
            // 5
            taskLists.add(list)
        }
        return taskLists
    }
}