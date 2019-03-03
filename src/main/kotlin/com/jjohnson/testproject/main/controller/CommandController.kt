package com.jjohnson.testproject.main.controller

import com.jjohnson.testproject.main.openapi.api.MainApi
import com.jjohnson.testproject.main.openapi.model.CommandItem
import com.jjohnson.testproject.main.services.CommandIssueService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller

@Controller
class CommandController(
    val commandIssueService: CommandIssueService
) : MainApi {
    override fun commandIssue(commandItem: CommandItem?): ResponseEntity<Void> {
        commandIssueService.issueCommand(commandItem = commandItem)
        return ResponseEntity(HttpStatus.OK)
    }
}
