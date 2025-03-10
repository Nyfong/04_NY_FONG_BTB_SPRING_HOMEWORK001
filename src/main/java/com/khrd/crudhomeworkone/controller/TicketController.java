package com.khrd.crudhomeworkone.controller;


import com.khrd.crudhomeworkone.model.Ticket;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
 *
 * RestAPi with CRUD and more operation
 *
 * **/

@RestController
@RequestMapping("/api/v1/tickets/")
public class TicketController {

    List<Ticket> tickets = new ArrayList<>();

    //constructor
    TicketController() {
        tickets.add(new Ticket(1L, "Kao", LocalDate.of(2024, 10, 15), "New York Central Station", "Los Angeles Union Station", 1.99, true, false, "C001"));
        tickets.add(new Ticket(2L, "a", LocalDate.of(2024, 10, 15), "New York Central Station", "Los Angeles Union Station", 1.99, true, true, "C003"));
        tickets.add(new Ticket(3L, "Kdet", LocalDate.of(2024, 10, 15), "New York Central Station", "Los Angeles Union Station", 1.99, true, true, "C003"));

    }

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
        tickets.add(ticket);
        return new ResponseEntity<>(ticket, HttpStatus.CREATED);
    }

    //Retrieve All Tickets
    @GetMapping
    public ResponseEntity<List> getAllTicket() {
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    //Retrieve a Ticket by ID (using @PathVariable)
    @GetMapping("/{id}")
    public ResponseEntity<?> getTicketById(@PathVariable Long id) {
        String responseBody = String.format("Ticket with id %d not found", id);
        Ticket foundTikcet = tickets.stream()
                .filter(t -> t.getTicketId().equals(id)).findFirst().orElse(null);
        if (foundTikcet != null)
            return new ResponseEntity<>(foundTikcet, HttpStatus.OK);
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }
    //Search for a Ticket by Passenger Name (using @RequestParam)

    @GetMapping("/name")
    public ResponseEntity<?> getTicketByName(@RequestParam String name) {
        String responseBody = String.format("Ticket with name %s not found", name);
        List<Ticket> foundTicketName = tickets.stream()
                .filter(n -> n.getPassengerName().contains(name)).collect(Collectors.toCollection(ArrayList::new
                ));
        if (foundTicketName.isEmpty()) {
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(foundTicketName, HttpStatus.OK);
    }
    //Filter Tickets by Ticket Status and Travel Date (using @RequestParam)

    //Update a Ticket by ID

    //Delete a Ticket by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable Long id) {
        boolean isRemoved = tickets.removeIf(ticket -> ticket.getTicketId().equals(id));

        if (isRemoved) {
            String responseBody = String.format("Ticket with id %d deleted successfully", id);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }
        String responseBody = String.format("Ticket with id %d not found", id);
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);

    }


}
