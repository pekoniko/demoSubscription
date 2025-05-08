package com.example.demo.controller;

import com.example.demo.dto.ErrorResponse;
import com.example.demo.dto.SubscriptionDTO;
import com.example.demo.exception.CustomException;
import com.example.demo.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class SubscribeController {
    private final SubscribeService subscribeService;

    @PostMapping("/{id}/subscriptions")
    public ResponseEntity<?> postSubscription(@PathVariable Long id, @RequestBody SubscriptionDTO subscriptionDTO){
        return subscribeService.createSubscription(id, subscriptionDTO);
    }

    @GetMapping("/{id}/subscriptions")
    public ResponseEntity<?> getSubscription(@PathVariable Long id){
        return subscribeService.getSubscription(id);
    }

    @DeleteMapping("/{id}/subscription/{sub_id}")
    public ResponseEntity<?> delSubscription(@PathVariable Long id, @PathVariable(name = "sub_id") Long subId){
        return subscribeService.delSubscription(id, subId);
    }

    @GetMapping("/subscriptions/top")
    public ResponseEntity<?> getTopSubscriptions(){
        return subscribeService.getTopSubscriptions();
    }

    @ExceptionHandler({CustomException.class})
    public ResponseEntity<ErrorResponse> handleCustomExceptions(CustomException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
