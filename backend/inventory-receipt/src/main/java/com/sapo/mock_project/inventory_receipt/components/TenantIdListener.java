package com.sapo.mock_project.inventory_receipt.components;

import com.sapo.mock_project.inventory_receipt.entities.BaseEntity;
import jakarta.persistence.PrePersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TenantIdListener {
    @Autowired
    private AuthHelper authHelper;

    @PrePersist
    public void setTenantId(BaseEntity baseEntity) {
        if (baseEntity.getTenantId() != null) {
            return;
        }
        String tenantId = authHelper.getUser().getTenantId();
        baseEntity.setTenantId(tenantId);
    }
}
