package dankook.cs.aj24.domain.place;

import dankook.cs.aj24.domain.place.PlaceDocument;
import dankook.cs.aj24.domain.place.placedtos.CreatePlaceDTO;
import dankook.cs.aj24.domain.place.placedtos.UpdatePlaceDTO;
import dankook.cs.aj24.domain.place.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/places")
@Tag(name = "Place API", description = "장소 관련 API")
public class PlaceController {

    private final PlaceService placeService;

    @Autowired
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    // 장소 추가
    @PostMapping
    @Operation(summary = "장소 추가", description = "새로운 장소를 등록합니다.")
    @ApiResponse(responseCode = "200", description = "장소 등록 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlaceDocument.class)))
    public ResponseEntity<PlaceDocument> addPlace(@RequestBody CreatePlaceDTO createPlaceDTO) {
        PlaceDocument createdPlace = placeService.addPlace(createPlaceDTO);
        return ResponseEntity.ok(createdPlace);
    }

    // 장소 수정
    @PutMapping("/{placeId}")
    @Operation(summary = "장소 수정", description = "기존 장소를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "장소 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlaceDocument.class)))
    @ApiResponse(responseCode = "404", description = "장소를 찾을 수 없음", content = @Content(mediaType = "application/json"))
    public ResponseEntity<PlaceDocument> updatePlace(@PathVariable String placeId, @RequestBody UpdatePlaceDTO updatePlaceDTO) {
        PlaceDocument updatedPlace = placeService.updatePlace(placeId, updatePlaceDTO);
        return ResponseEntity.ok(updatedPlace);
    }

    // 장소 삭제
    @DeleteMapping("/{placeId}")
    @Operation(summary = "장소 삭제", description = "장소를 삭제합니다. (soft_delete)")
    @ApiResponse(responseCode = "200", description = "장소 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlaceDocument.class)))
    @ApiResponse(responseCode = "404", description = "장소를 찾을 수 없음", content = @Content(mediaType = "application/json"))
    public ResponseEntity<PlaceDocument> deletePlace(@PathVariable String placeId){
        PlaceDocument deletedPlace = placeService.deletePlace(placeId);
        return ResponseEntity.ok(deletedPlace);
    }

    // 장소 조회
    @GetMapping("/{placeId}")
    @Operation(summary = "장소 조회", description = "장소를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "장소 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlaceDocument.class)))
    @ApiResponse(responseCode = "404", description = "장소를 찾을 수 없음", content = @Content(mediaType = "application/json"))
    public ResponseEntity<PlaceDocument> getPlace(@PathVariable String placeId) {
        PlaceDocument place = placeService.getPlace(placeId);
        return ResponseEntity.ok(place);
    }

    @GetMapping
    @Operation(summary = "모든 장소 조회", description = "등록된 모든 장소 목록을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "장소 목록 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlaceDocument.class)))
    public Page<PlaceDocument> getAllPlaces(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return placeService.getAllPlaces(page, size);
    }
}
