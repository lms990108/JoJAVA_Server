package dankook.cs.aj24.domain.place.placedtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlaceDTO {
    private String address_name;
    private String category_group_code;
    private String category_group_name;
    private String distance;
    private String phone;
    private String place_name;
    private String place_address_name;
    private String x;
    private String y;
}
