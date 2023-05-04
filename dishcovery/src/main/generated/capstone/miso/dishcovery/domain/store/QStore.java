package capstone.miso.dishcovery.domain.store;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStore is a Querydsl query type for Store
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStore extends EntityPathBase<Store> {

    private static final long serialVersionUID = -49165759L;

    public static final QStore store = new QStore("store");

    public final capstone.miso.dishcovery.domain.QBaseEntity _super = new capstone.miso.dishcovery.domain.QBaseEntity(this);

    public final StringPath address = createString("address");

    public final StringPath category = createString("category");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final ListPath<capstone.miso.dishcovery.application.files.FileData, capstone.miso.dishcovery.application.files.QFileData> fileDataList = this.<capstone.miso.dishcovery.application.files.FileData, capstone.miso.dishcovery.application.files.QFileData>createList("fileDataList", capstone.miso.dishcovery.application.files.FileData.class, capstone.miso.dishcovery.application.files.QFileData.class, PathInits.DIRECT2);

    public final NumberPath<Integer> isExtracted = createNumber("isExtracted", Integer.class);

    public final ListPath<capstone.miso.dishcovery.domain.keyword.Keyword, capstone.miso.dishcovery.domain.keyword.QKeyword> keywords = this.<capstone.miso.dishcovery.domain.keyword.Keyword, capstone.miso.dishcovery.domain.keyword.QKeyword>createList("keywords", capstone.miso.dishcovery.domain.keyword.Keyword.class, capstone.miso.dishcovery.domain.keyword.QKeyword.class, PathInits.DIRECT2);

    public final NumberPath<Double> lat = createNumber("lat", Double.class);

    public final NumberPath<Double> lon = createNumber("lon", Double.class);

    public final ListPath<capstone.miso.dishcovery.domain.menu.Menu, capstone.miso.dishcovery.domain.menu.QMenu> menus = this.<capstone.miso.dishcovery.domain.menu.Menu, capstone.miso.dishcovery.domain.menu.QMenu>createList("menus", capstone.miso.dishcovery.domain.menu.Menu.class, capstone.miso.dishcovery.domain.menu.QMenu.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final StringPath phone = createString("phone");

    public final StringPath sector = createString("sector");

    public final NumberPath<Long> sid = createNumber("sid", Long.class);

    public final ListPath<capstone.miso.dishcovery.domain.storeimg.StoreImg, capstone.miso.dishcovery.domain.storeimg.QStoreImg> storeImgs = this.<capstone.miso.dishcovery.domain.storeimg.StoreImg, capstone.miso.dishcovery.domain.storeimg.QStoreImg>createList("storeImgs", capstone.miso.dishcovery.domain.storeimg.StoreImg.class, capstone.miso.dishcovery.domain.storeimg.QStoreImg.class, PathInits.DIRECT2);

    public final SetPath<StoreOffInfo, QStoreOffInfo> storeOffInfos = this.<StoreOffInfo, QStoreOffInfo>createSet("storeOffInfos", StoreOffInfo.class, QStoreOffInfo.class, PathInits.DIRECT2);

    public final SetPath<StoreOnInfo, QStoreOnInfo> storeOnInfos = this.<StoreOnInfo, QStoreOnInfo>createSet("storeOnInfos", StoreOnInfo.class, QStoreOnInfo.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QStore(String variable) {
        super(Store.class, forVariable(variable));
    }

    public QStore(Path<? extends Store> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStore(PathMetadata metadata) {
        super(Store.class, metadata);
    }

}

