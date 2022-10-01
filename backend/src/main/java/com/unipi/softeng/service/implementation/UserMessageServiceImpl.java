package com.unipi.softeng.service.implementation;

import com.unipi.softeng.model.UserMessage;
import com.unipi.softeng.repository.UserMessageRepo;
import com.unipi.softeng.service.UserMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor @Service @Transactional @Slf4j
public class UserMessageServiceImpl implements UserMessageService {

    private final UserMessageRepo userMessageRepo;

    @Override
    public List<UserMessage> findAllUserMessages() {
        log.info("Fetching all user messages.");
        return userMessageRepo.findAll();
    }

    @Override
    public UserMessage findUserMessage(Long id) {
        log.info("Searching for UserMessage #"+id.toString());
        Optional<UserMessage> userMessage = userMessageRepo.findById(id);
        return userMessage.orElse(null);
    }

    @Override
    public Page<UserMessage> findMessages_Paginated(int offset, int pageSize) {
        //log.info("")
        return userMessageRepo.findAll(PageRequest.of(offset,pageSize));
    }


    @Override
    public UserMessage addUserMessage(UserMessage userMessage) {
        log.info("Adding user message");
        return userMessageRepo.save(userMessage);
    }

    @Override
    public Boolean deleteUserMessage(Long id) {
        log.info("Deleting user message #"+id.toString());
        userMessageRepo.deleteById(id);
        return Boolean.TRUE;
    }
}
