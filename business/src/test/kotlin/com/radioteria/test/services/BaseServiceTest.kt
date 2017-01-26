package com.radioteria.test.services

import org.junit.runner.RunWith
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(locations = arrayOf("classpath:business-context.xml"))
@ActiveProfiles("test")
abstract class BaseServiceTest
