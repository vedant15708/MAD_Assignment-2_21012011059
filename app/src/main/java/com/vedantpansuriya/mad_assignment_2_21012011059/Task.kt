package com.vedantpansuriya.mad_assignment_2_21012011059
data class Task(val name: String, var isDone: Boolean, var priority: Int = 0){
    override fun toString(): String {
        return name
    }
}