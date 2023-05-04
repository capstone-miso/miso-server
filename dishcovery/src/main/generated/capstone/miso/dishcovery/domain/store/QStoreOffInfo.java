package capstone.miso.dishcovery.domain.store;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStoreOffInfo is a Querydsl query type for StoreOffInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStoreOffInfo extends EntityPathBase<StoreOffInfo> {

    private static final long serialVersionUID = 1916676412L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStoreOffInfo storeOffInfo = new QStoreOffInfo("storeOffInfo");

    public final StringPath info = createString("info");

    public final NumberPath<Long> infoId = createNumber("infoId", Long.class);

    public final QStore store;

    public QStoreOffInfo(String variable) {
        this(StoreOffInfo.class, forVariable(variable), INITS);
    }

    public QStoreOffInfo(Path<? extends StoreOffInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStoreOffInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStoreOffInfo(PathMetadata metadata, PathInits inits) {
        this(StoreOffInfo.class, metadata, inits);
    }

    public QStoreOffInfo(Class<? extends StoreOffInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.store = inits.isInitialized("store") ? new QStore(forProperty("store")) : null;
    }

}

