package capstone.miso.dishcovery.application.files;

/**
 * author        : duckbill413
 * date          : 2023-03-22
 * description   :
 **/

import capstone.miso.dishcovery.domain.BaseEntity;
import capstone.miso.dishcovery.domain.store.Store;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FileData extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fid;
    private LocalDate date;
    private LocalTime time;
    @NotBlank
    private String storeName;
    private String storeAddress;
    private String purpose;
    private int participants;
    private int cost;
    private String paymentOption;
    private String expenditure;
    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "file_id")
    private Files files;
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public void setFiles(Files files) {
        this.files = files;
        if (!files.getFileDataList().contains(this)) {
            files.getFileDataList().add(this);
        }
    }

    @Builder
    public FileData(String date, String time, String storeName, String storeAddress, String purpose, String participants, String cost, String paymentOption, String expenditure, Files files, Store store) {
        this.storeName = checkNullOrEmpty(storeName);

        // 날짜 수정 23 -> 2023 년
        if (date.matches("\\d{2}-\\d{1,2}-\\d{1,2}"))
            date = "20" + date;
        DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder()
                .appendPattern("[yyyy-MM-dd]")
                .appendPattern("[MM-dd]")
                .appendPattern("[yyyy-M-d]")
                .appendPattern("[M-d]")
                .parseDefaulting(ChronoField.YEAR_OF_ERA, LocalDate.now().getYear())
                .toFormatter();
        this.date = LocalDate.parse(date, dateFormatter);
        try {
            DateTimeFormatter timeFormatter = new DateTimeFormatterBuilder()
                    .appendPattern("[HH-mm-ss]")
                    .appendPattern("[H-m-s]")
                    .appendPattern("[HH-mm]")
                    .appendPattern("[H-m]")
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .toFormatter();
            this.time = LocalTime.parse(time, timeFormatter);
        } catch (Exception e) {
//            e.printStackTrace();
        }

        this.storeAddress = storeAddress;
        this.purpose = purpose;

        if (Objects.isNull(participants)) {
            throw new IllegalArgumentException("인원수 입력 오류");
        } else {
            this.participants = Integer.parseInt(participants.replaceAll("\\D", ""));
            if (participants.contains("외")) {
                this.participants += 1;
            }
        }
        this.cost = Integer.parseInt(cost.replaceAll("\\D", ""));
        if (this.cost < 100) {
            throw new IllegalArgumentException("Cost 입력 오류");
        }
        this.paymentOption = paymentOption;
        this.expenditure = expenditure;

        if (Objects.nonNull(files))
            files.addFileData(this);
        if (Objects.nonNull(store))
            store.addFileData(this);
    }

    private String checkNullOrEmpty(String str) {
        if (str == null || str.replaceAll(" ", "").isBlank()) {
            throw new IllegalArgumentException("String is null or blank.");
        }
        return str;
    }
}
