package dankook.cs.aj24.domain.place;

import dankook.cs.aj24.common.error.CustomException;
import dankook.cs.aj24.domain.place.placedtos.CreatePlaceDTO;
import dankook.cs.aj24.domain.place.placedtos.UpdatePlaceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;

import static dankook.cs.aj24.common.error.ErrorCode.*;

@Service
public class PlaceService {
    private final PlaceRepository placeRepository;

    @Autowired
    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    // 장소 생성
    public PlaceDocument addPlace(CreatePlaceDTO createPlaceDTO) {
        // CreatePlaceDTO를 PlaceDocument로 변환
        PlaceDocument place = new PlaceDocument(
                null, // id는 자동 생성
                createPlaceDTO.getAddress_name(),
                createPlaceDTO.getCategory_group_code(),
                createPlaceDTO.getCategory_group_name(),
                createPlaceDTO.getDistance(),
                createPlaceDTO.getPhone(),
                createPlaceDTO.getPlace_name(),
                createPlaceDTO.getPlace_address_name(),
                createPlaceDTO.getX(),
                createPlaceDTO.getY(),
                createPlaceDTO.getRating(),
                LocalDateTime.now(), // 생성일자 설정
                null, // 수정일자는 처음에는 null로 설정
                null  // 삭제일자는 처음에는 null로 설정
        );

        // 장소 추가
        return placeRepository.save(place);
    }

    // 장소 수정
    public PlaceDocument updatePlace(String placeId, UpdatePlaceDTO updatePlaceDTO) {
        // 기존 장소 조회
        PlaceDocument existingPlace = placeRepository.findById(placeId)
                .orElseThrow(() -> new CustomException(OBJECT_NOT_FOUND));

        // 수정일자 업데이트
        existingPlace.setUpdatedAt(LocalDateTime.now());

        // 수정된 장소 저장
        return placeRepository.save(existingPlace);
    }

    // 장소 삭제
    public PlaceDocument deletePlace(String placeId) {
        // 기존 장소 조회
        PlaceDocument existingPlace = placeRepository.findById(placeId)
                .orElseThrow(() -> new CustomException(OBJECT_NOT_FOUND));

        // 삭제일자 업데이트
        existingPlace.setDeletedAt(LocalDateTime.now());

        return placeRepository.save(existingPlace);
    }

    // 장소 조회
    public PlaceDocument getPlace(String placeId) {
        // 기존 장소 조회
        PlaceDocument existingPlace = placeRepository.findById(placeId)
                .orElseThrow(() -> new CustomException(OBJECT_NOT_FOUND));

        // 기존 장소가 삭제되었는지 확인
        if (existingPlace.isDeleted()) {
            // 삭제된 장소이면 예외를 던지거나 다른 처리를 수행할 수 있습니다.
            throw new CustomException(PLACE_DELETED);
        } else {
            // 삭제되지 않은 장소이면 해당 장소 반환
            return existingPlace;
        }


    }

    // 삭제되지 않은 장소 전체 조회 및 페이지네이션
    public Page<PlaceDocument> getAllPlaces(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return placeRepository.findByDeletedAtIsNull(pageable);
    }
}
