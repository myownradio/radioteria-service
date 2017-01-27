package com.radioteria.web.rest.controllers

import com.radioteria.db.entities.Channel
import com.radioteria.db.repositories.ChannelRepository
import com.radioteria.web.rest.exceptions.NotFoundException
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.annotation.Resource

@RestController
@RequestMapping("/api/channels")
open class ChannelRestController : BaseRestController() {
    @Resource
    lateinit var channelRepository: ChannelRepository

    @RequestMapping("{id}", method = arrayOf(RequestMethod.GET))
    fun read(@PathVariable id: Long): Channel {
        return channelRepository.findById(id)
                ?: throw NotFoundException("channel", id)
    }

    @Transactional
    @RequestMapping(method = arrayOf(RequestMethod.POST))
    open fun create(channel: Channel): Channel {
        channelRepository.persist(channel)
        return channel
    }

    @RequestMapping("{id}", method = arrayOf(RequestMethod.DELETE))
    fun delete(channel: Channel) {
        channelRepository.remove(channel)
    }

    @RequestMapping(method = arrayOf(RequestMethod.GET))
    fun list(): List<Channel> {
        return channelRepository.list()
    }
}