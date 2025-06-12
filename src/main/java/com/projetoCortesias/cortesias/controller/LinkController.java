package com.projetoCortesias.cortesias.controller;

import com.projetoCortesias.cortesias.service.BitlyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/links")
public class LinkController {

    @Autowired
    private BitlyService bitlyService;

    @PostMapping("/encurtar")
    public Map<String, String> encurtar(@RequestBody Map<String, String> body) {
        String url = body.get("url");
        return bitlyService.encurtar(url);
    }
}
