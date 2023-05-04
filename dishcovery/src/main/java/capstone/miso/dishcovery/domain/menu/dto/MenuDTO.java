package capstone.miso.dishcovery.domain.menu.dto;

/**
 * author        : duckbill413
 * date          : 2023-05-03
 * description   :
 **/

public record MenuDTO (
        Long mid,
        String name,
        String cost,
        String detail,
        String menuImg
){
}
