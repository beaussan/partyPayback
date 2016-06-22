package me.nbeaussart.payback.web.rest.mapper;

import me.nbeaussart.payback.domain.*;
import me.nbeaussart.payback.web.rest.dto.InitialPaymentDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity InitialPayment and its DTO InitialPaymentDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface InitialPaymentMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "event.id", target = "eventId")
    InitialPaymentDTO initialPaymentToInitialPaymentDTO(InitialPayment initialPayment);

    List<InitialPaymentDTO> initialPaymentsToInitialPaymentDTOs(List<InitialPayment> initialPayments);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "eventId", target = "event")
    InitialPayment initialPaymentDTOToInitialPayment(InitialPaymentDTO initialPaymentDTO);

    List<InitialPayment> initialPaymentDTOsToInitialPayments(List<InitialPaymentDTO> initialPaymentDTOs);

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
