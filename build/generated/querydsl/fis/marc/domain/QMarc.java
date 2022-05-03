package fis.marc.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMarc is a Querydsl query type for Marc
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMarc extends EntityPathBase<Marc> {

    private static final long serialVersionUID = -1082592700L;

    public static final QMarc marc = new QMarc("marc");

    public final StringPath checked = createString("checked");

    public final StringPath comment = createString("comment");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath origin = createString("origin");

    public final StringPath worked = createString("worked");

    public QMarc(String variable) {
        super(Marc.class, forVariable(variable));
    }

    public QMarc(Path<? extends Marc> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMarc(PathMetadata metadata) {
        super(Marc.class, metadata);
    }

}

