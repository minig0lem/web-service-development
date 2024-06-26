package db.project.controller;

import db.project.dto.BikeListResponseDto;
import db.project.dto.PostBikeCreateDto;
import db.project.dto.PostBikeDeleteDto;
import db.project.exceptions.ErrorResponse;
import db.project.service.BikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class BikeController {  // 자전거 생성 및 수정 Controller
    private final BikeService bikeService;

    @GetMapping({"bike/list/{page}", "bike/list"})
    @ResponseBody
    @Operation(
            summary = "자전거 리스트",
            description = "자전거 추가/삭제 버튼을 클릭했을 때의 API"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "자전거 리스트 열람 성공"),
            @ApiResponse(responseCode = "404", description = "페이지를 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "내부 서버 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    // 자전거 List
    public ResponseEntity<BikeListResponseDto> getBikeList(@PathVariable(value = "page", required = false) Optional<Integer> page) {
        BikeListResponseDto bikeListResponseDto = bikeService.bikeList(page);

        return ResponseEntity.ok(bikeListResponseDto);
    }

    @PostMapping("bike/create")
    @ResponseBody
    @Operation(
            summary = "자전거 추가",
            description = "자전거 id와 대여소 id를 입력하고 추가 버튼을 클릭했을 때의 API"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "자전거 추가 성공", content = {
                    @Content(examples = {
                            @ExampleObject(value = "{}")})
            }),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 대여소 ID", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "중복된 자전거 ID", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "내부 서버 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    // 자전거 생성
    public ResponseEntity<String> postBikeCreate(@RequestBody PostBikeCreateDto form) {
        bikeService.bikeCreate(form);

        return ResponseEntity.ok("{}");
    }

    @PostMapping("bike/delete")
    @ResponseBody
    @Operation(
            summary = "자전거 삭제",
            description = "자전거 삭제 버튼을 클릭했을 때의 API"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "자전거 삭제 성공", content = {
                    @Content(examples = {
                            @ExampleObject(value = "{}")})
            }),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 자전거 ID", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "내부 서버 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    //자전거 삭제
    public ResponseEntity<String> postBikeDelete(@RequestBody PostBikeDeleteDto form) {
        bikeService.bikeDelete(form);

        return ResponseEntity.ok("{}");
    }
}
