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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class File extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fid;
    @Enumerated(EnumType.STRING)
    private Region region;
    @NonNull
    private String title;
    @NonNull
    private String department;
    @NonNull
    private String fileName;
    @NonNull
    private String fileUrl;
    @NonNull
    private LocalDate fileUploaded;
    private boolean fileDownloaded;
    private boolean converted;
    @OneToMany(mappedBy = "file", cascade = CascadeType.PERSIST)
    private List<FileData> fileDataList = new ArrayList<>();

    public void changeConvertStatus(boolean status){
        this.converted = status;
    }
    @Builder
    public File(Region region, String title, String department, String fileName, String fileUrl, LocalDate fileUploaded, boolean converted) {
        this.region = region;
        this.title = title;
        this.department = department;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.fileUploaded = Objects.nonNull(fileUploaded) ? fileUploaded : LocalDate.now();
        this.converted = converted;
    }
    public void addFileData(FileData fileData){
        this.fileDataList.add(fileData);
        if (fileData.getFile() != this){
            fileData.setFile(this);
        }
    }
}
