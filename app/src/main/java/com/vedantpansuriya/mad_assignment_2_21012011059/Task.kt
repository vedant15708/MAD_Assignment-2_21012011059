package com.vedantpansuriya.mad_assignment_2_21012011059

class Task(val name: String, var isDone: Boolean, var reminder: Long = -1){
    override fun toString(): String {
        return name
    }
}