package capstone.miso.dishcovery.application.files;

/**
 * author        : duckbill413
 * date          : 2023-03-22
 * description   :
 **/

import capstone.miso.dishcovery.domain.BaseEntity;
import capstone.miso.dishcovery.domain.store.Store;
import jakarta.persistence.*;
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
    @NotNull
    private LocalDate date;
    @NotNull
    private LocalTime time;
    @NotEmpty
    private String storeName;
    private String storeAddress;
    private String purpose;
    private int participants;
    private int cost;
    private String paymentOption;
    private String expenditure;
    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "file_id")
    private File file;
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public void setFile(File file) {
        this.file = file;
        if (!file.getFileDataList().contains(this)) {
            file.getFileDataList().add(this);
        }
    }

    @Builder
    public FileData(String date, String time, String storeName, String storeAddress, String purpose, String participants, String cost, String paymentOption, String expenditure, File file, Store store) {
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

        DateTimeFormatter timeFormatter = new DateTimeFormatterBuilder()
                .appendPattern("[HH-mm-ss]")
                .appendPattern("[HH-mm]")
                .appendPattern("[H-m-s]")
                .appendPattern("[H-m]")
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter();
        this.time = LocalTime.parse(time, timeFormatter);

        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.purpose = purpose;

        if (Objects.isNull(participants)) {
            this.participants = 1;
        } else {
            this.participants = Integer.parseInt(participants.replaceAll("\\D", ""));
            if (participants.contains("외")) {
                this.participants += 1;
            }
        }
        this.cost = Integer.parseInt(cost.replaceAll("\\D", ""));
        this.paymentOption = paymentOption;
        this.expenditure = expenditure;
        this.file = file;
        this.store = store;
    }
}
