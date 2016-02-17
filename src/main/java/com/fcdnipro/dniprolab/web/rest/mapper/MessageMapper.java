package com.fcdnipro.dniprolab.web.rest.mapper;

import com.fcdnipro.dniprolab.domain.*;
import com.fcdnipro.dniprolab.web.rest.dto.MessageDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Message and its DTO MessageDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MessageMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    MessageDTO messageToMessageDTO(Message message);

    @Mapping(source = "userId", target = "user")
    Message messageDTOToMessage(MessageDTO messageDTO);

    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
