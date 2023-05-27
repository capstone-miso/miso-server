package capstone.miso.dishcovery.application.files;

/**
 * author        : duckbill413
 * date          : 2023-03-21
 * description   :
 **/

import capstone.miso.dishcovery.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "file")
public class Files extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fid;
    private String region;
    private String department;
    private String fileName;
    private String fileFormat;
    private String fileUrl;
    private LocalDate fileUploaded;
    private boolean fileDownloaded;
    private boolean converted;
    private String convertResult;
    @OneToMany(mappedBy = "files", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<FileData> fileDataList = new ArrayList<>();
    public void changeConvertStatus(boolean status){
        this.converted = status;
    }
    public void addFileData(FileData fileData){
        this.fileDataList.add(fileData);

        if (fileData.getFiles() != this){
            fileData.setFiles(this);
        }
    }
}
