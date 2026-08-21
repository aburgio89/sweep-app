package com.example.sweepapp.util

import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull

class LoginValidationTest {

    //Test 1: A blank password will result in an error message "Enter your password".
    @Test
    fun missingPasswordShowsErrorMessage() {
        val result = validateLoginInput(email = "unittest@test.com", password = "")
        assertEquals("Enter your password.", result)
    }

    //Test 2: An inputted email and password will not return a validateLoginInput error message.
    @Test
    fun emailAndPasswordPassValidation() {
        val result = validateLoginInput(email = "unittest@test.com", password = "testpw1")
        assertNull(result)
    }
}