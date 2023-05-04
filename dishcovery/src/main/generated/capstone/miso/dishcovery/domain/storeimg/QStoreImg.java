package capstone.miso.dishcovery.domain.storeimg;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStoreImg is a Querydsl query type for StoreImg
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStoreImg extends EntityPathBase<StoreImg> {

    private static final long serialVersionUID = 1722819451L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStoreImg storeImg = new QStoreImg("storeImg");

    public final capstone.miso.dishcovery.domain.QBaseEntity _super = new capstone.miso.dishcovery.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath imageUrl = createString("imageUrl");

    public final StringPath photoId = createString("photoId");

    public final NumberPath<Long> sid = createNumber("sid", Long.class);

    public final capstone.miso.dishcovery.domain.store.QStore store;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QStoreImg(String variable) {
        this(StoreImg.class, forVariable(variable), INITS);
    }

    public QStoreImg(Path<? extends StoreImg> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStoreImg(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStoreImg(PathMetadata metadata, PathInits inits) {
        this(StoreImg.class, metadata, inits);
    }

    public QStoreImg(Class<? extends StoreImg> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.store = inits.isInitialized("store") ? new capstone.miso.dishcovery.domain.store.QStore(forProperty("store")) : null;
    }

}

