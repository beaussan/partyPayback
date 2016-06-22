package me.nbeaussart.payback.web.rest.mapper;

import me.nbeaussart.payback.domain.*;
import me.nbeaussart.payback.web.rest.dto.ExtandedUserDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ExtandedUser and its DTO ExtandedUserDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ExtandedUserMapper {

    ExtandedUserDTO extandedUserToExtandedUserDTO(ExtandedUser extandedUser);

    List<ExtandedUserDTO> extandedUsersToExtandedUserDTOs(List<ExtandedUser> extandedUsers);

    @Mapping(target = "toPays", ignore = true)
    @Mapping(target = "payRecives", ignore = true)
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "initialPaiments", ignore = true)
    @Mapping(target = "eventParcipatings", ignore = true)
    ExtandedUser extandedUserDTOToExtandedUser(ExtandedUserDTO extandedUserDTO);

    List<ExtandedUser> extandedUserDTOsToExtandedUsers(List<ExtandedUserDTO> extandedUserDTOs);
}
