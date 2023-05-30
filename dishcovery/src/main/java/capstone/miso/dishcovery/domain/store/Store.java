package capstone.miso.dishcovery.domain.store;

/**
 * author        : duckbill413
 * date          : 2023-03-29
 * description   :
 **/

import capstone.miso.dishcovery.application.files.FileData;
import capstone.miso.dishcovery.domain.BaseEntity;
import capstone.miso.dishcovery.domain.keyword.Keyword;
import capstone.miso.dishcovery.domain.keyword.KeywordData;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Store extends BaseEntity {
    @Id
    private Long sid;
    private String name;
    private Double lat;
    private Double lon;
    @Column(length = 500)
    private String mainImageUrl;
    private String address;
    private String category;
    private String categoryKey;
    private String sector;
    private String phone;
    private int isExtracted;

    @Builder.Default
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Keyword> keywords = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    @Builder.Default
    private List<FileData> fileDataList = new ArrayList<>();

    public void addFileData(FileData fileData){
        if (fileData == null){
            return;
        }
        this.fileDataList.add(fileData);

        if (fileData.getStore() != this){
            fileData.setStore(this);
        }
    }
}
