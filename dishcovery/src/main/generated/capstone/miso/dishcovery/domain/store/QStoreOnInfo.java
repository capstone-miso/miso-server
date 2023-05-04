package capstone.miso.dishcovery.domain.store;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStoreOnInfo is a Querydsl query type for StoreOnInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStoreOnInfo extends EntityPathBase<StoreOnInfo> {

    private static final long serialVersionUID = -762896178L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStoreOnInfo storeOnInfo = new QStoreOnInfo("storeOnInfo");

    public final StringPath info = createString("info");

    public final NumberPath<Long> infoId = createNumber("infoId", Long.class);

    public final QStore store;

    public QStoreOnInfo(String variable) {
        this(StoreOnInfo.class, forVariable(variable), INITS);
    }

    public QStoreOnInfo(Path<? extends StoreOnInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStoreOnInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStoreOnInfo(PathMetadata metadata, PathInits inits) {
        this(StoreOnInfo.class, metadata, inits);
    }

    public QStoreOnInfo(Class<? extends StoreOnInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.store = inits.isInitialized("store") ? new QStore(forProperty("store")) : null;
    }

}

