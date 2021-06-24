package com.example.y.tasknotebook

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Task : RealmObject() {
    @PrimaryKey
    var id: Int = 0
    var isAchieved: Boolean = false
    var achievedYear: Int = 0 //例: 2021, 2022
    var achievedMonth: Int = 0 //例: 6. 10
    var achievedDate: Int = 0 //例: 3, 17
    var achievedHour: Int = 0 //例: 7, 11
    var achievedMinute: Int = 0 //例: 9, 34
    var isPinned: Boolean = false
    var colorId: Int = 0
    var title: String = ""
    var detail: String = ""
}