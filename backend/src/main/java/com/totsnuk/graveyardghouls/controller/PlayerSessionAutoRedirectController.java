package com.totsnuk.graveyardghouls.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerSessionAutoRedirectController {
    @PostMapping("/gameAutoRedirect")
    public String postGameAutoRedirect(@RequestBody String entity) {
        // get
        return entity;
    }

}
