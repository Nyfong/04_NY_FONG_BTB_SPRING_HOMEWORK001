package com.khrd.crudhomeworkone.controller;


import com.khrd.crudhomeworkone.model.Items;
import com.khrd.crudhomeworkone.model.Ticket;
import com.khrd.crudhomeworkone.model.TicketRequest;
import com.khrd.crudhomeworkone.reponse.ResponseTicket;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        tickets.add(new Ticket(1L, "Kao", LocalDate.of(2024, 10, 15), "New York Central Station", "Los Angeles Union Station", 1.99, true, "BOOK", "C001"));
        tickets.add(new Ticket(2L, "a", LocalDate.of(2024, 10, 15), "New York Central Station", "Los Angeles Union Station", 1.99, false, "CANCELLED", "C003"));
        tickets.add(new Ticket(3L, "Kdet", LocalDate.of(2024, 10, 15), "New York Central Station", "Los Angeles Union Station", 1.99, true, "COMPLETED", "C003"));

    }
    @Operation(summary = "Create new ticket")
    @PostMapping
    public ResponseEntity<?> createTicket(@RequestBody Ticket ticket) {
        tickets.add(ticket);
        return new ResponseEntity<>(ResponseTicket.<Ticket>builder().success(Boolean.TRUE).payload(ticket)
                .build(), HttpStatus.CREATED);
    }

    //Retrieve All Tickets
    @Operation(summary = "Get All Tickets")
    @GetMapping
    public ResponseEntity<?> getAllTicket() {
        return new ResponseEntity<>(ResponseTicket.<Items>builder().success(Boolean.TRUE).message("Response all tickets").status("OK").payload(Items.builder().item(tickets).build()).timestamp(LocalDateTime.now()).build(), HttpStatus.OK);
    }

    //Retrieve a Ticket by ID (using @PathVariable)
    @GetMapping("/{ticket-id}")
    public ResponseEntity<?> getById(@PathVariable("ticket-id") Long id) {
        String responseBody = String.format("Ticket with id %d not found", id);
        Ticket foundTikcet = tickets.stream()
                .filter(t -> t.getTicketId().equals(id)).findFirst().orElse(null);
        //foundTikcet
        if (foundTikcet != null)
            return new ResponseEntity<>(ResponseTicket.<Items>builder()
                    .success(Boolean.TRUE)
                    .message("Get  by id Successfull!").status("OK")
                    .payload(Items.builder().item(foundTikcet).build())
                    .build(), HttpStatus.OK);

        return new ResponseEntity<>(ResponseTicket.<Items>builder()
                .success(Boolean.FALSE)
                .message("not found!").status("404")
                .payload(Items.builder().item(null).build())
                .build(), HttpStatus.NOT_FOUND);
    }

    //Search for a Ticket by Passenger Name (using @RequestParam)
    @Operation(summary = "Search for a Ticket by Passenger Name")
    @GetMapping("/name")
    public ResponseEntity<?> getTicketByName(@RequestParam String name) {
        String responseBody = String.format("Ticket with name %s not found", name);
        List<Ticket> foundTicketName = tickets.stream()
                .filter(n -> n.getPassengerName().contains(name)).collect(Collectors.toCollection(ArrayList::new
                ));
        if (foundTicketName.isEmpty()) {
            return new ResponseEntity<>(ResponseTicket.<Items>builder()
                    .success(Boolean.FALSE)
                    .message(responseBody).status("404")
                    .payload(Items.builder().item(null).build()).timestamp(LocalDateTime.now())
                    .build(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(ResponseTicket.<Items>builder()
                .success(Boolean.TRUE)
                .message("search by name Successfull!").status("OK")
                .payload(Items.builder().item(foundTicketName).build()).timestamp(LocalDateTime.now())
                .build(), HttpStatus.OK);
    }



    //Filter Tickets by Ticket Status and Travel Date (using @RequestParam)
    @Operation(summary = "Filter ticket by Status and travel date")
    public ResponseEntity<?> filterByDateAndTicketStatus(){


        return  new ResponseEntity<>("a", HttpStatus.OK);
    }


    //Update a Ticket by ID
    @Operation(summary = "Update an existing ticket by id")
    @PutMapping("/update/{ticket-id}")
    public ResponseEntity<?> updateTicketById(@PathVariable("ticket-id") Long id, @RequestBody TicketRequest ticketRequest) {

        for (Ticket ticket : tickets) {
            if (ticket.getTicketId().equals(id)) {

                ticket.setPassengerName(ticketRequest.getPassengerName());
                ticket.setTravelDate(ticketRequest.getTravelDate());
                ticket.setSourceStation(ticketRequest.getSourceStation());
                ticket.setDestinationStation(ticketRequest.getDestinationStation());
                ticket.setPrice(ticketRequest.getPrice());
                ticket.setTicketStatus(String.valueOf(ticketRequest.getTicketStatus()));
                ticket.setPaymentStatus(ticketRequest.isPaymentStatus());
                ticket.setSeatNumber(ticketRequest.getSeatNumber());

                return new ResponseEntity<Ticket>(ticket, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>("Update the ticket", HttpStatus.NOT_FOUND);
    }

    //Delete a Ticket by ID
    @Operation(summary = "Delete ticket by Id")
    @DeleteMapping("/{ticket-id}")
    public ResponseEntity<?> deleteTicket(@PathVariable("ticket-id") Long id) {
        boolean isRemoved = tickets.removeIf(ticket -> ticket.getTicketId().equals(id));
        if (isRemoved) {
            String responseBody = String.format("Ticket with id %d deleted successfully", id);
            return new ResponseEntity<>(ResponseTicket.<Boolean>builder()
                    .success(Boolean.TRUE)
                    .message("Get  by id Successfull!").status("OK")
                    .payload(isRemoved).timestamp(LocalDateTime.now())
                    .build(), HttpStatus.OK);
        }
        String responseBody = String.format("Ticket with id %d not found", id);
        return new ResponseEntity<>(ResponseTicket.<Items>builder()
                .success(Boolean.FALSE)
                .message("id not found! ").status("404")
                .payload(Items.builder().item(null).build()).timestamp(LocalDateTime.now())
                .build(), HttpStatus.NOT_FOUND);
    }


}
