package com.example.demo

import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.view.RedirectView

@RestController
class Controller(val pubSubTemplate: PubSubTemplate, val personRepository: PersonRepository) {

    val REGISTRATION_TOPIC = "registrations"

    @PostMapping("/registerPerson")
    fun registerPerson(
            @RequestParam("firstName") firstName: String,
            @RequestParam("lastName") lastName: String,
            @RequestParam("email") email: String): RedirectView {

        pubSubTemplate.publish(
                REGISTRATION_TOPIC,
                Person(firstName = firstName,
                        lastName = lastName,
                        email = email))
        return RedirectView("/")
    }

    @GetMapping("/registrants")
    fun getRegistrants(): ModelAndView {
        val personsList = personRepository.findAll().toList()
        return ModelAndView("registrants", mapOf("personsList" to personsList))
    }

}