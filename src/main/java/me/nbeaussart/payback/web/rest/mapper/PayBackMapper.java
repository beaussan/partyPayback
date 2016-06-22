package me.nbeaussart.payback.web.rest.mapper;

import me.nbeaussart.payback.domain.*;
import me.nbeaussart.payback.web.rest.dto.PayBackDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity PayBack and its DTO PayBackDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PayBackMapper {

    @Mapping(source = "source.id", target = "sourceId")
    @Mapping(source = "toPay.id", target = "toPayId")
    @Mapping(source = "event.id", target = "eventId")
    PayBackDTO payBackToPayBackDTO(PayBack payBack);

    List<PayBackDTO> payBacksToPayBackDTOs(List<PayBack> payBacks);

    @Mapping(source = "sourceId", target = "source")
    @Mapping(source = "toPayId", target = "toPay")
    @Mapping(source = "eventId", target = "event")
    PayBack payBackDTOToPayBack(PayBackDTO payBackDTO);

    List<PayBack> payBackDTOsToPayBacks(List<PayBackDTO> payBackDTOs);

    default ExtandedUser extandedUserFromId(Long id) {
        if (id == null) {
            return null;
        }
        ExtandedUser extandedUser = new ExtandedUser();
        extandedUser.setId(id);
        return extandedUser;
    }

    default Event eventFromId(Long id) {
        if (id == null) {
            return null;
        }
        Event event = new Event();
        event.setId(id);
        return event;
    }
}
