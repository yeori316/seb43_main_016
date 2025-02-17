package com.codestates.edusync.model.common.entity;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditEntity extends BaseEntity {

    @CreatedDate // Entity 생성 시간 자동 저장
    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @LastModifiedDate // 조회한 Entity 의 값을 변경할 때 시간 자동 저장(수정)
    @Column(nullable = false)
    protected LocalDateTime modifiedAt;
}