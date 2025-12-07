package me.minkyoung.buddy_back.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.entity.User;
import me.minkyoung.buddy_back.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class PenaltyService {
    private final UserRepository userRepository;

    public void applyPenaltyRules(User tartgetUser){
        
    }
}
