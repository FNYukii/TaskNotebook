package com.example.y.tasknotebook

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Task : RealmObject() {
    @PrimaryKey
    var id: Int = 0
    var isAchieved: Boolean = false
    var achievedYear: Int = 0 //例: 20210615
    var achievedTime: Int = 0 //例: 091231
    var isPinned: Boolean = false
    var colorId: Int = 0
    var title: String = ""
    var detail: String = ""
}