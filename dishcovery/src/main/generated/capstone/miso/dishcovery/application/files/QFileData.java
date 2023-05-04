package capstone.miso.dishcovery.application.files;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFileData is a Querydsl query type for FileData
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFileData extends EntityPathBase<FileData> {

    private static final long serialVersionUID = -1873555118L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFileData fileData = new QFileData("fileData");

    public final capstone.miso.dishcovery.domain.QBaseEntity _super = new capstone.miso.dishcovery.domain.QBaseEntity(this);

    public final NumberPath<Integer> cost = createNumber("cost", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DatePath<java.time.LocalDate> date = createDate("date", java.time.LocalDate.class);

    public final StringPath expenditure = createString("expenditure");

    public final NumberPath<Long> fid = createNumber("fid", Long.class);

    public final QFiles files;

    public final NumberPath<Integer> participants = createNumber("participants", Integer.class);

    public final StringPath paymentOption = createString("paymentOption");

    public final StringPath purpose = createString("purpose");

    public final capstone.miso.dishcovery.domain.store.QStore store;

    public final StringPath storeAddress = createString("storeAddress");

    public final StringPath storeName = createString("storeName");

    public final TimePath<java.time.LocalTime> time = createTime("time", java.time.LocalTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QFileData(String variable) {
        this(FileData.class, forVariable(variable), INITS);
    }

    public QFileData(Path<? extends FileData> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFileData(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFileData(PathMetadata metadata, PathInits inits) {
        this(FileData.class, metadata, inits);
    }

    public QFileData(Class<? extends FileData> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.files = inits.isInitialized("files") ? new QFiles(forProperty("files")) : null;
        this.store = inits.isInitialized("store") ? new capstone.miso.dishcovery.domain.store.QStore(forProperty("store")) : null;
    }

}

