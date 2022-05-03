package fis.marc.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProcess is a Querydsl query type for Process
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProcess extends EntityPathBase<Process> {

    private static final long serialVersionUID = -1758130544L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProcess process = new QProcess("process");

    public final QUser checker;

    public final StringPath createdDate = createString("createdDate");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMarc marc;

    public final EnumPath<fis.marc.domain.enumType.Status> status = createEnum("status", fis.marc.domain.enumType.Status.class);

    public final QUser user;

    public QProcess(String variable) {
        this(Process.class, forVariable(variable), INITS);
    }

    public QProcess(Path<? extends Process> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProcess(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProcess(PathMetadata metadata, PathInits inits) {
        this(Process.class, metadata, inits);
    }

    public QProcess(Class<? extends Process> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.checker = inits.isInitialized("checker") ? new QUser(forProperty("checker")) : null;
        this.marc = inits.isInitialized("marc") ? new QMarc(forProperty("marc")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

