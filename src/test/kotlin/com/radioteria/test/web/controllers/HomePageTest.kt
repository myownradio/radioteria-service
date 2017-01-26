package com.radioteria.test.web.controllers

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

import org.hamcrest.core.Is.`is`
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import javax.annotation.Resource

@SpringBootTest
@RunWith(SpringRunner::class)
@ContextConfiguration(locations = arrayOf("classpath:application-context.xml"))
@ActiveProfiles("test")
class HomePageTest {

    @Resource
    lateinit var wac: WebApplicationContext

    lateinit var mockMvc: MockMvc

    @Before
    fun setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
    }

    @Test
    fun testRoot() {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk)
                .andExpect(content().string("Hello, World!"))
    }

    @Test
    @WithMockUser(roles = arrayOf("REGULAR_USER"), username = "someone")
    fun testView() {
        mockMvc.perform(get("/views"))
                .andExpect(status().isOk)
                .andExpect(view().name("index"))
                .andExpect(model().attribute("user", `is`("someone")))
    }

}
