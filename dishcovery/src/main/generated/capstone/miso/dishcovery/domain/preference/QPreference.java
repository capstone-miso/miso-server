package capstone.miso.dishcovery.domain.preference;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPreference is a Querydsl query type for Preference
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPreference extends EntityPathBase<Preference> {

    private static final long serialVersionUID = 735961211L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPreference preference = new QPreference("preference");

    public final capstone.miso.dishcovery.domain.QBaseEntity _super = new capstone.miso.dishcovery.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final capstone.miso.dishcovery.domain.member.QMember member;

    public final NumberPath<Long> pid = createNumber("pid", Long.class);

    public final capstone.miso.dishcovery.domain.store.QStore store;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QPreference(String variable) {
        this(Preference.class, forVariable(variable), INITS);
    }

    public QPreference(Path<? extends Preference> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPreference(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPreference(PathMetadata metadata, PathInits inits) {
        this(Preference.class, metadata, inits);
    }

    public QPreference(Class<? extends Preference> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new capstone.miso.dishcovery.domain.member.QMember(forProperty("member")) : null;
        this.store = inits.isInitialized("store") ? new capstone.miso.dishcovery.domain.store.QStore(forProperty("store")) : null;
    }

}

