package com.goutham.perugu.shopapp

import junit.framework.TestCase

infix fun <T> T.shouldBe(any: Any?) {
    TestCase.assertEquals(any, this)
}
