package com.sapo.mock_project.inventory_receipt.mappers;

import com.sapo.mock_project.inventory_receipt.dtos.request.supplier.CreateSupplierRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.supplier.UpdateSupplierRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.supplier.DebtSupplierGetListResponse;
import com.sapo.mock_project.inventory_receipt.dtos.response.supplier.ExportDataResponse;
import com.sapo.mock_project.inventory_receipt.dtos.response.supplier.SupplierDetail;
import com.sapo.mock_project.inventory_receipt.entities.DebtSupplier;
import com.sapo.mock_project.inventory_receipt.entities.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SupplierMapper {
    Supplier mapToEntity(CreateSupplierRequest request);

    SupplierDetail mapToResponse(Supplier supplier);

    DebtSupplierGetListResponse mapToResponseDebtSupplier(DebtSupplier debtSupplier);

    ExportDataResponse mapToResponseExportData(Supplier supplier);

    void updateFromDTO(UpdateSupplierRequest request, @MappingTarget Supplier supplier);
}
