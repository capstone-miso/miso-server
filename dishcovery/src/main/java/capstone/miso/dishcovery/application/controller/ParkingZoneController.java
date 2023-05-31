package capstone.miso.dishcovery.application.controller;

import capstone.miso.dishcovery.domain.parkinglot.dto.ParkingDTO;
import capstone.miso.dishcovery.domain.parkinglot.repository.ParkingJDBCRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/park")
@Tag(name = "주차장", description = "주자장 Controller")
public class ParkingZoneController {
    private final ParkingJDBCRepository parkingJDBCRepository;

    @Operation(summary = "주변 지역 주차장 조회 2km 이내")
    @GetMapping("")
    @PreAuthorize("permitAll()")
    public List<ParkingDTO> findAreaParkingZone(@RequestParam("lon") @Valid @NotNull Double lon,
                                                @RequestParam("lat") @Valid @NotNull Double lat) {
        return parkingJDBCRepository.findAreaParkinglot(lon, lat);
    }
}
