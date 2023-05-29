package capstone.miso.dishcovery.domain.store;

/**
 * author        : duckbill413
 * date          : 2023-03-29
 * description   :
 **/

import capstone.miso.dishcovery.application.files.FileData;
import capstone.miso.dishcovery.domain.BaseEntity;
import capstone.miso.dishcovery.domain.image.Image;
import capstone.miso.dishcovery.domain.keyword.Keyword;
import capstone.miso.dishcovery.domain.keyword.KeywordData;
import capstone.miso.dishcovery.domain.menu.Menu;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

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

    @Builder.Default
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StoreOffInfo> storeOffInfos = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StoreOnInfo> storeOnInfos = new HashSet<>();

    @OneToOne(mappedBy = "store", cascade = CascadeType.ALL)
    private KeywordData keywordData;
    public void addFileData(FileData fileData){
        if (fileData == null){
            return;
        }
        this.fileDataList.add(fileData);

        if (fileData.getStore() != this){
            fileData.setStore(this);
        }
    }
    public void addStoreImg(Image image){
        if (image == null){
            return;
        }

        this.images.add(image);

        if (image.getStore() != this){
            image.setStore(this);
        }
    }

    public void addMenu(Menu menu){
        if (menu == null){
            return;
        }

        this.menus.add(menu);
        if (menu.getStore() != this){
            menu.setStore(this);
        }
    }
}
