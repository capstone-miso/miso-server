package capstone.miso.dishcovery.domain.store;

/**
 * author        : duckbill413
 * date          : 2023-03-29
 * description   :
 **/

import capstone.miso.dishcovery.application.files.FileData;
import capstone.miso.dishcovery.domain.BaseEntity;
import capstone.miso.dishcovery.domain.menu.Menu;
import capstone.miso.dishcovery.domain.storeimg.StoreImg;
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
    private String address;
    private String category;
    private String sector;
    private String phone;
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<FileData> fileDataList = new ArrayList<>();
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreImg> storeImgs = new ArrayList<>();

    public void addFileData(FileData fileData){
        if (fileData == null){
            return;
        }
        this.fileDataList.add(fileData);

        if (fileData.getStore() != this){
            fileData.setStore(this);
        }
    }
    public void addStoreImg(StoreImg storeImg){
        if (storeImg == null){
            return;
        }

        this.storeImgs.add(storeImg);

        if (storeImg.getStore() != this){
            storeImg.setStore(this);
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
