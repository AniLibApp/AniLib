package com.revolgenx.anilib


import com.google.gson.Gson
import org.junit.Test

class UnitTest {

    @Test
    fun test() {
        ModelTest().also {
            it.test("my testing")
        }.let {
            Gson().toJson(it).also {
                println(it)
            }
        }.let {
            Gson().fromJson(it, ModelTest::class.java).let {
                print(it.test())
            }
        }
    }

}