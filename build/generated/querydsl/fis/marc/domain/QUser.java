package fis.marc.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1082337462L;

    public static final QUser user = new QUser("user");

    public final StringPath address = createString("address");

    public final EnumPath<fis.marc.domain.enumType.Authority> auth = createEnum("auth", fis.marc.domain.enumType.Authority.class);

    public final StringPath createDate = createString("createDate");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath phnum = createString("phnum");

    public final ListPath<Process, QProcess> processes = this.<Process, QProcess>createList("processes", Process.class, QProcess.class, PathInits.DIRECT2);

    public final StringPath pwd = createString("pwd");

    public final StringPath username = createString("username");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

