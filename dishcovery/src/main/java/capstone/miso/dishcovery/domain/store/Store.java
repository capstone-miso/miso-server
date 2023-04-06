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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sid;
    private String name;
    private Double lat;
    private Double lon;
    private String sector;
    private String phone;
    @OneToMany(mappedBy = "store", cascade = CascadeType.PERSIST)
    private List<FileData> fileDataList = new ArrayList<>();
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus = new ArrayList<>();
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreImg> storeImgs = new ArrayList<>();
}
