package capstone.miso.dishcovery.domain.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 911874587L;

    public static final QMember member = new QMember("member1");

    public final capstone.miso.dishcovery.domain.QBaseEntity _super = new capstone.miso.dishcovery.domain.QBaseEntity(this);

    public final BooleanPath alarm = createBoolean("alarm");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final ListPath<capstone.miso.dishcovery.domain.preference.Preference, capstone.miso.dishcovery.domain.preference.QPreference> preferences = this.<capstone.miso.dishcovery.domain.preference.Preference, capstone.miso.dishcovery.domain.preference.QPreference>createList("preferences", capstone.miso.dishcovery.domain.preference.Preference.class, capstone.miso.dishcovery.domain.preference.QPreference.class, PathInits.DIRECT2);

    public final SetPath<MemberRole, EnumPath<MemberRole>> roleSet = this.<MemberRole, EnumPath<MemberRole>>createSet("roleSet", MemberRole.class, EnumPath.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

