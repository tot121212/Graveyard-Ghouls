package com.totsnuk.graveyardghouls;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GraveyardghoulsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraveyardghoulsApplication.class, args);
        System.out.println("=== GRAVEYARD GHOULS ===");
        System.out.println("Server started");
        System.out.println("Press Ctrl+C to stop server");
    }

}
