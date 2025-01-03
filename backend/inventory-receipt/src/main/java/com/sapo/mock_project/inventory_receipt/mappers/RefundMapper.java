package com.sapo.mock_project.inventory_receipt.mappers;

import com.sapo.mock_project.inventory_receipt.dtos.response.refundinformation.GetListByGRNResponse;
import com.sapo.mock_project.inventory_receipt.dtos.response.refundinformation.RefundDetailResponse;
import com.sapo.mock_project.inventory_receipt.entities.RefundInformation;
import com.sapo.mock_project.inventory_receipt.entities.RefundInformationDetail;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RefundMapper {
    GetListByGRNResponse mapToResponse(RefundInformation entity);

    RefundDetailResponse mapToResponse(RefundInformationDetail entity);
}
