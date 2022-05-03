package fis.marc.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QManage is a Querydsl query type for Manage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QManage extends EntityPathBase<Manage> {

    private static final long serialVersionUID = -989616860L;

    public static final QManage manage = new QManage("manage");

    public final NumberPath<Long> goal = createNumber("goal", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QManage(String variable) {
        super(Manage.class, forVariable(variable));
    }

    public QManage(Path<? extends Manage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QManage(PathMetadata metadata) {
        super(Manage.class, metadata);
    }

}

