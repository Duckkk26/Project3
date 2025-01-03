package com.sapo.mock_project.inventory_receipt.mappers;

import com.sapo.mock_project.inventory_receipt.dtos.request.user.RegisterAccountRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.user.GetListAccountResponse;
import com.sapo.mock_project.inventory_receipt.dtos.response.user.UserDetailResponse;
import com.sapo.mock_project.inventory_receipt.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    User mapToEntity(RegisterAccountRequest request);

    UserDetailResponse mapToResponse(User user);

    GetListAccountResponse mapToGetListResponse(User user);

    void updateFromDTO(RegisterAccountRequest request, @MappingTarget User user);
}
