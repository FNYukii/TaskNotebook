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
    var achievedDatetime: Date? = null
}