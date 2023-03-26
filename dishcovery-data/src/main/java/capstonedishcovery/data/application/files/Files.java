package capstonedishcovery.data.application.files;

/**
 * author        : duckbill413
 * date          : 2023-03-21
 * description   :
 **/
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Files {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fid;
    private String title;
    private String department;
    private String fileName;
    private String fileUrl;
    private LocalDate fileUploaded;
    private boolean converted;
    private LocalDateTime createdAt;

    public void changeConvertStatus(boolean status){
        this.converted = status;
    }
}
