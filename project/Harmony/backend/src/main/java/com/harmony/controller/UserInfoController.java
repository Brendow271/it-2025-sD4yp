package com.harmony.controller;

import com.harmony.dto.UserInfoRequest;
import com.harmony.dto.UserInfoResponse;
import com.harmony.entity.UserInfo;
import com.harmony.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("info")
@CrossOrigin(origins = "*")
@Tag(name = "User Information", description = "Изменение информации о пользователе")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @Operation(summary = "Обновление информации о пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные обновлены успешно",
                    content = @Content(schema = @Schema(implementation = UserInfoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка обновления данных")
    })
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody UserInfoRequest request){
        try {
            UserInfo user = userInfoService.updateInfo(
                    request.getUserId(),
                    request.getAge(),
                    request.getGenres(),
                    request.getInstruments(),
                    request.getLocation(),
                    request.getAbout()
            );
            UserInfoResponse response = new UserInfoResponse(user);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при обновлении информации: " + e.getMessage());
        }
    }
}
