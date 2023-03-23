package capstonedishcovery.data.application.files;

/**
 * author        : duckbill413
 * date          : 2023-03-22
 * description   :
 **/
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class FileData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fid;
    private String name;
    private LocalDateTime useDate;
    private String area;
    @NotEmpty
    private String department;
    @NotEmpty
    private String storeName;
    private String address;
    private String purpose;
    @NotNull
    private Integer participants;
    @NotNull
    private Integer cost;
    private String paymentOption;
    private String expenditure;
    @ManyToOne
    private Files files;
}
