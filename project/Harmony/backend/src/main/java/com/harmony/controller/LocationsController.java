package com.harmony.controller;

import com.harmony.dto.LocationsResponse;
import com.harmony.service.LocationsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("list")
@CrossOrigin(origins = "*")
@Tag(name = "Locations")
public class LocationsController {

    @Autowired
    private LocationsService locationsService;

    @Operation(summary = "Получение списка городов", description = "Возвращает список всех доступных городов")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Города успешно получены",
            content = @Content(schema = @Schema(implementation = LocationsResponse.class))
        ),
        @ApiResponse(responseCode = "401", description = "Токен невалиден или отсутствует"),
        @ApiResponse(responseCode = "400", description = "Ошибка получения данных")
    })
    @GetMapping("/locations")
    public ResponseEntity<List<LocationsResponse>> getLocations() {
        try {
            List<LocationsResponse> locations = locationsService.getLocations();
            return ResponseEntity.ok(locations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}