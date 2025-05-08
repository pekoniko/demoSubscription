package com.example.demo.service;

import com.example.demo.dto.SubscriptionDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.exception.CustomException;
import com.example.demo.model.Subscription;
import com.example.demo.model.User;
import com.example.demo.repository.SubscriptionRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscribeService {
    private final UserRepository userRep;
    private final SubscriptionRepository subRep;


    public ResponseEntity<?> createSubscription(Long id, SubscriptionDTO subscriptionDTO) {
        checkUserExist(id);
        Subscription newSub = new Subscription(userRep.getReferenceById(id), subscriptionDTO.subscribeName());
        subRep.save(newSub);
        return ResponseEntity.ok("Saved successfully");
    }

    public ResponseEntity<?> getSubscription(Long id) {
        checkUserExist(id);
        List<Subscription> allUserSubs = subRep.findByUserId(userRep.getReferenceById(id));
        if (allUserSubs.isEmpty())
            throw new CustomException("User with that id have no subs");
        List<SubscriptionDTO> subsResult = allUserSubs.stream()
                .map(s -> new SubscriptionDTO(s.getSubscriptionName())).toList();
        return ResponseEntity.ok(subsResult);
    }

    public ResponseEntity<?> delSubscription(Long id, Long subId) {
        checkUserExist(id);
        subRep.deleteById(subId);
        return ResponseEntity.ok("Deleted successfully");
    }

    public ResponseEntity<?> getTopSubscriptions() {
        List<Subscription> subs = subRep.findAll();
        List<String> countForId = subs.stream().collect(Collectors.groupingBy(Subscription::getSubscriptionName, Collectors.counting()))
                .entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3).map(Map.Entry::getKey).toList();
        return ResponseEntity.ok(countForId);
    }

    private void checkUserExist(Long id) {
        if (!userRep.existsById(id))
            throw new CustomException("No user with that ID");
    }

}
