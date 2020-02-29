package practice.app.project

import java.text.SimpleDateFormat
import java.util.*

class Task {
    var iD = 0
    var task: String
    var timestamp: String
    var listIndex = 0

    constructor() {
        task = ""
        timestamp = timestamp()
        listIndex = 0
    }

    constructor(ID: Int, task: String, timestamp: String?) {
        iD = ID
        this.task = task
        this.timestamp = timestamp()
    }

    fun setTimestamp() {
        timestamp = timestamp()
    }

    private fun timestamp(): String {
        var timeStampFormatted =
            SimpleDateFormat("MM/dd/yyyy").format(Date())
        timeStampFormatted += " "
        timeStampFormatted += SimpleDateFormat("HH:mm").format(Date())
        return timeStampFormatted
    }

    override fun toString(): String {
        return "Task{" +
                "ID=" + iD +
                ", task='" + task + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", listIndex=" + listIndex +
                '}'
    }
}