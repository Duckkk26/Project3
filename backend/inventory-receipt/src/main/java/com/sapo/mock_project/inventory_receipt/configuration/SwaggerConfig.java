package com.sapo.mock_project.inventory_receipt.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Cấu hình Swagger để tạo API documentation cho các controller trong ứng dụng.
 * Lớp này cấu hình Swagger với các thông tin như tên dịch vụ, phiên bản, và địa chỉ server.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Cấu hình GroupedOpenApi để nhóm các API public lại với nhau, dựa trên package của controller.
     *
     * @param apiDocs giá trị của key swagger.service.api-docs trong file cấu hình, đại diện cho nhóm API.
     * @return đối tượng GroupedOpenApi được cấu hình để scan các controller.
     */
    @Bean
    public GroupedOpenApi publicApi(@Value("${swagger.service.api-docs}") String apiDocs) {
        return GroupedOpenApi.builder()
                // Tên nhóm API lấy từ file cấu hình (application.properties hoặc application.yml)
                .group(apiDocs)
                // Chỉ định package chứa các controller để scan
                .packagesToScan("com.sapo.mock_project.inventory_receipt.controllers")
                .build();
    }

    /**
     * Cấu hình OpenAPI để cung cấp các thông tin tổng quan cho tài liệu API (Swagger UI).
     *
     * @param title     tiêu đề của API, lấy từ file cấu hình (swagger.service.title).
     * @param version   phiên bản của API, lấy từ file cấu hình (swagger.service.version).
     * @param serverUrl URL của server, lấy từ file cấu hình (swagger.service.server).
     * @return đối tượng OpenAPI chứa thông tin về API.
     */
    @Bean
    public OpenAPI openAPI(
            @Value("${swagger.service.title}") String title,
            @Value("${swagger.service.version}") String version,
            @Value("${swagger.service.server}") String serverUrl) {
        return new OpenAPI()
                // Cấu hình server cho API, lấy URL từ file cấu hình
                .servers(List.of(new Server().url(serverUrl)))
                // Cấu hình các thông tin tổng quan của API
                .info(new Info().title(title)
                        .description("API documents")
                        .version(version)
                        // Cấu hình giấy phép cho API (ở đây là Apache 2.0)
                        .license(new License().name("Apache 2.0").url("https://springdoc.org")));
    }
}
