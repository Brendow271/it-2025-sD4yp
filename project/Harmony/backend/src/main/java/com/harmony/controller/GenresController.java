package com.harmony.controller;

import com.harmony.dto.GenresResponse;
import com.harmony.service.GenresService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("list")
@CrossOrigin(origins = "*")
@Tag(name = "Genres")
public class GenresController {

    @Autowired
    private GenresService genresService;

    @Operation(summary = "Получение списка жанров")
    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Жанры успешно получены",
                content = @Content(
                    schema = @Schema(implementation = GenresResponse.class)
                )
            ),
            @ApiResponse(responseCode = "400",description = "Ошибка получения данных")
    })
    @GetMapping("/genres")
    public ResponseEntity<List<GenresResponse>> getGenres(){
        try {
            List<GenresResponse> genres = genresService.getGenres();
            return ResponseEntity.ok(genres);


        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}
