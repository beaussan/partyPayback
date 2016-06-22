package me.nbeaussart.payback.web.rest.mapper;

import me.nbeaussart.payback.domain.*;
import me.nbeaussart.payback.web.rest.dto.EventDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Event and its DTO EventDTO.
 */
@Mapper(componentModel = "spring", uses = {ExtandedUserMapper.class, })
public interface EventMapper {

    @Mapping(source = "owner.id", target = "ownerId")
    EventDTO eventToEventDTO(Event event);

    List<EventDTO> eventsToEventDTOs(List<Event> events);

    @Mapping(target = "initialPayiments", ignore = true)
    @Mapping(target = "paybacks", ignore = true)
    @Mapping(source = "ownerId", target = "owner")
    Event eventDTOToEvent(EventDTO eventDTO);

    List<Event> eventDTOsToEvents(List<EventDTO> eventDTOs);

    default ExtandedUser extandedUserFromId(Long id) {
        if (id == null) {
            return null;
        }
        ExtandedUser extandedUser = new ExtandedUser();
        extandedUser.setId(id);
        return extandedUser;
    }
}
