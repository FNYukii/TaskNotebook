package com.example.y.tasknotebook

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Task : RealmObject() {
    @PrimaryKey
    var id: Int = 0
    var isAchieved: Boolean = false
    var isPinned: Boolean = false
    var title: String = ""
    var detail: String = ""
    var achievedYear: Int = -1 //例: 2021, 2022
    var achievedMonth: Int = -1 //例: 6. 10
    var achievedDay: Int = -1 //例: 3, 17
    var achievedHour: Int = -1 //例: 7, 11
    var achievedMinute: Int = -1 //例: 9, 34
    var achievedDatetime: Date? = null
}