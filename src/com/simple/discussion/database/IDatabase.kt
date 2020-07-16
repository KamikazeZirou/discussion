package com.simple.discussion.database

interface IDatabase {
    fun connect()
    fun cleanup()
}