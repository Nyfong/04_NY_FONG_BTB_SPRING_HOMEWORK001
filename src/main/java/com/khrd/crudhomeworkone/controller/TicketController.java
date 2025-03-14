package com.khrd.crudhomeworkone.controller;


import com.khrd.crudhomeworkone.model.*;
import com.khrd.crudhomeworkone.reponse.ResponseTicket;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.catalina.User;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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
        // Existing tickets with corrections (unique IDs)
        tickets.add(new Ticket(1L, "Kao", LocalDate.of(2024, 10, 15), "New York Central Station", "Los Angeles Union Station", 1.99, true, TicketStatus.BOOKED, "C001"));
        tickets.add(new Ticket(2L, "Alex", LocalDate.of(2024, 10, 15), "Chicago Union Station", "Miami Central Station", 2.99, false, TicketStatus.CANCELLED, "C002"));
        tickets.add(new Ticket(3L, "Sok", LocalDate.of(2024, 10, 15), "Boston South Station", "Seattle King Street", 3.99, true, TicketStatus.COMPLETED, "C003"));

        // Additional varied test data
        tickets.add(new Ticket(4L, "Maria", LocalDate.of(2024, 10, 14), "Washington Union", "Austin Station", 4.99, true, TicketStatus.BOOKED, "C004"));
        tickets.add(new Ticket(5L, "John", LocalDate.of(2024, 10, 16), "Philadelphia 30th St", "Denver Union", 5.99, true, TicketStatus.COMPLETED, "C005"));
        tickets.add(new Ticket(6L, "Emma", LocalDate.of(2024, 10, 15), "San Francisco Caltrain", "Portland Union", 6.99, false, TicketStatus.CANCELLED, "C006"));
        tickets.add(new Ticket(7L, "Liam", LocalDate.of(2024, 10, 17), "Atlanta Peachtree", "Charlotte Station", 7.99, true, TicketStatus.BOOKED, "C007"));
        tickets.add(new Ticket(8L, "Olivia", LocalDate.of(2024, 10, 14), "Houston Main St", "Phoenix Union", 8.99, true, TicketStatus.COMPLETED, "C008"));
        tickets.add(new Ticket(9L, "Noah", LocalDate.of(2024, 10, 13), "Detroit Station", "Minneapolis Depot", 9.99, false, TicketStatus.CANCELLED, "C009"));
        tickets.add(new Ticket(10L, "Ava", LocalDate.of(2024, 10, 16), "St Louis Gateway", "Kansas City Union", 10.99, true, TicketStatus.BOOKED, "C010"));
    }

    @Operation(summary = "Create new ticket")
    @PostMapping
    public ResponseEntity<?> createTicket(@RequestBody Ticket ticket) {

        ticket.setTicketId((long) (tickets.size() + 1)); // Ensure ID is a Long
        tickets.add(ticket);
        return new ResponseEntity<>(ResponseTicket.<Ticket>builder().success(Boolean.TRUE).message("Succusfull Create").status("OK").payload(ticket).timestamp(LocalDateTime.now())
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

    //Filter tickets by Status and travel date
    @Operation(summary = "Filter tickets by Status and travel date")
    @GetMapping("/filter")
    public ResponseEntity<ResponseTicket<List<Ticket>>> filterByDateAndTicketStatus(
            @RequestParam TicketStatus eTicketStatus,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate filterDate) {

        List<Ticket> filteredTickets = tickets.stream()
                // Compare enum directly (not as String)
                .filter(ticket -> ticket.getTicketStatus() == eTicketStatus)
                // Use LocalDate comparison
                .filter(ticket -> ticket.getTravelDate().isEqual(filterDate))
                .collect(Collectors.toList());

        return filteredTickets.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseTicket.<List<Ticket>>builder()
                        .status("NOT_FOUND")
                        .message(String.format("No tickets found with status %s and date %s", eTicketStatus, filterDate))
                        .payload(Collections.emptyList())
                        .timestamp(LocalDateTime.now())
                        .build())
                : ResponseEntity.ok(
                ResponseTicket.<List<Ticket>>builder()
                        .status("OK")
                        .message("Tickets filtered successfully.")
                        .payload(filteredTickets)
                        .timestamp(LocalDateTime.now())
                        .build());
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
                ticket.setTicketStatus(ticketRequest.getTicketStatus());
                ticket.setPaymentStatus(ticketRequest.isPaymentStatus());
                ticket.setSeatNumber(ticketRequest.getSeatNumber());
                return new ResponseEntity<Ticket>(ticket, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>("Can not update the ticket :) ", HttpStatus.NOT_FOUND);
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


    //bonus
    // Update multiple by an Id
    @Operation(summary = "Update payment status for multiple tickets by their IDs")
    @PutMapping
    public ResponseEntity<?> updatePaymentStatus(@RequestBody PaymentUpdateRequest paymentUpdateRequest) {
        List<Long> ticketIds = paymentUpdateRequest.getTicketIds();
        Boolean paymentStatus = paymentUpdateRequest.getPaymentStatus();
        List<Ticket> updatedTickets = new ArrayList<>();

        for (Ticket ticket : tickets) {
            if (ticketIds.contains(ticket.getTicketId())) {
                // Update the payment status
                ticket.setPaymentStatus(paymentStatus);
                updatedTickets.add(ticket); // Add the updated ticket to the response list
            }
        }

        if (updatedTickets.isEmpty()) {
            return new ResponseEntity<>("No tickets found with the provided IDs", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(updatedTickets, HttpStatus.OK);
    }

    @Operation(summary = "Get All Tickets with Pagination")
    @GetMapping("/pagenation")
    public ResponseEntity<?> getAllTicket(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        try {
            // Ensure tickets list is not null
            List<Ticket> allTickets = tickets != null ? tickets : new ArrayList<>();

            // Validate page number
            if (page <= 0) {
                Map<String, String> errors = new HashMap<>();
                errors.put("page", "Page number must be greater than 0");

                ApiErrorResponse<Map<String, String>> errorResponse = new ApiErrorResponse<>();
                errorResponse.setType("about:blank");
                errorResponse.setTitle("Bad Request");
                errorResponse.setStatus(400);
                errorResponse.setInstance("/api/v1/tickets");
                errorResponse.setTimestamp(Instant.now());
                errorResponse.setErrors(errors);

                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            // Calculate pagination values
            long totalElements = allTickets.size();
            int totalPages = totalElements == 0 ? 1 : (int) Math.ceil((double) totalElements / size);

            // Check if page is within bounds
            if (page > totalPages) {
                Map<String, String> errors = new HashMap<>();
                errors.put("page", "Page number exceeds available pages");

                ApiErrorResponse<Map<String, String>> errorResponse = new ApiErrorResponse<>();
                errorResponse.setType("about:blank");
                errorResponse.setTitle("Bad Request");
                errorResponse.setStatus(400);
                errorResponse.setInstance("/api/v1/tickets");
                errorResponse.setTimestamp(Instant.now());
                errorResponse.setErrors(errors);

                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            // Convert 1-based page to 0-based index for calculations
            int fromIndex = (page - 1) * size;
            int toIndex = Math.min(fromIndex + size, (int) totalElements);

            // Handle empty list case
            List<Ticket> pageTickets;
            if (fromIndex >= totalElements) {
                pageTickets = new ArrayList<>();
            } else {
                pageTickets = new ArrayList<>(allTickets.subList(fromIndex, toIndex));
            }

            // Create pagination info
            PaginationInfo paginationInfo = new PaginationInfo();
            paginationInfo.setTotalElements(totalElements);
            paginationInfo.setCurrentPage(page);
            paginationInfo.setPageSize(size);
            paginationInfo.setTotalPages(totalPages);

            // Create paged response
            PagedResponseListTicket response = new PagedResponseListTicket();
            response.setItems(pageTickets);
            response.setPagination(paginationInfo);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            // Log the exception
            Map<String, String> errors = new HashMap<>();
            errors.put("page", e.getMessage());

            ApiErrorResponse<Map<String, String>> errorResponse = new ApiErrorResponse<>();
            errorResponse.setType("about:blank");
            errorResponse.setTitle("Internal Server Error");
            errorResponse.setStatus(500);
            errorResponse.setInstance("/api/v1/tickets");
            errorResponse.setTimestamp(Instant.now());
            errorResponse.setErrors(errors);

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





}
