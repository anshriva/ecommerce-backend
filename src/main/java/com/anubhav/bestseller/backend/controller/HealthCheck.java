package com.anubhav.bestseller.backend.controller;

import com.anubhav.bestseller.backend.model.Item;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/healthCheck")
public class HealthCheck {
    @GetMapping("/{path}")
    public String isFine(@RequestParam String name, @PathVariable String path){
        return  name + " working fine for path" +  path;
    }

    @GetMapping("/item")
    public Item getItem(@RequestParam String name){
        Item item = new Item();
        item.name  = name;
        return  item;
    }

    @PostMapping("/itemByPost")
    public Item getItem(@RequestBody  Item item){
        item.name = item.name +"-" +  "Anubhav";
        return item;
    }
}
