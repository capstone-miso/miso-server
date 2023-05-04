package capstone.miso.dishcovery.application.files;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFiles is a Querydsl query type for Files
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFiles extends EntityPathBase<Files> {

    private static final long serialVersionUID = 1729544171L;

    public static final QFiles files = new QFiles("files");

    public final capstone.miso.dishcovery.domain.QBaseEntity _super = new capstone.miso.dishcovery.domain.QBaseEntity(this);

    public final BooleanPath converted = createBoolean("converted");

    public final StringPath convertResult = createString("convertResult");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath department = createString("department");

    public final NumberPath<Long> fid = createNumber("fid", Long.class);

    public final ListPath<FileData, QFileData> fileDataList = this.<FileData, QFileData>createList("fileDataList", FileData.class, QFileData.class, PathInits.DIRECT2);

    public final BooleanPath fileDownloaded = createBoolean("fileDownloaded");

    public final StringPath fileFormat = createString("fileFormat");

    public final StringPath fileName = createString("fileName");

    public final DatePath<java.time.LocalDate> fileUploaded = createDate("fileUploaded", java.time.LocalDate.class);

    public final StringPath fileUrl = createString("fileUrl");

    public final StringPath region = createString("region");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QFiles(String variable) {
        super(Files.class, forVariable(variable));
    }

    public QFiles(Path<? extends Files> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFiles(PathMetadata metadata) {
        super(Files.class, metadata);
    }

}

