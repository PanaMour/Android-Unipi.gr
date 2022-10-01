package com.unipi.softeng.service;

import com.unipi.softeng.model.UserMessage;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserMessageService {

    List<UserMessage> findAllUserMessages();

    UserMessage findUserMessage(Long id);

    Page<UserMessage> findMessages_Paginated(int offset,int pageSize);

    UserMessage addUserMessage(UserMessage userMessage);

    Boolean deleteUserMessage(Long id);

}
