package com.radioteria.web.rest.controllers

import com.radioteria.db.entities.Content
import com.radioteria.db.repositories.ContentRepository
import com.radioteria.web.rest.exceptions.NotFoundException
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.annotation.Resource

@RestController
@RequestMapping("/api/content")
open class ContentRestController {
    @Resource
    lateinit var contentRepository: ContentRepository

    @Transactional
    @RequestMapping("{id}", method = arrayOf(RequestMethod.GET))
    open fun read(@PathVariable id: Long): Content {
        return contentRepository.findById(id)
                ?: throw NotFoundException("content", id)
    }

    @Transactional
    @RequestMapping(method = arrayOf(RequestMethod.POST))
    open fun create(content: Content): Content {
        contentRepository.persist(content)
        return content
    }

    @Transactional
    @RequestMapping("{id}", method = arrayOf(RequestMethod.DELETE))
    open fun delete(content: Content) {
        contentRepository.remove(content)
    }

    @Transactional
    @RequestMapping(method = arrayOf(RequestMethod.GET))
    open fun list(): List<Content> {
        return contentRepository.list()
    }
}