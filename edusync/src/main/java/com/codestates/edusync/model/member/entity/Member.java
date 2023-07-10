package com.codestates.edusync.model.member.entity;

import com.codestates.edusync.model.common.entity.AuditEntity;
import com.codestates.edusync.model.study.comment.entity.Comment;
import com.codestates.edusync.model.study.schedule.entity.Schedule;
import com.codestates.edusync.model.study.study.entity.Study;
import com.codestates.edusync.model.study.studyjoin.entity.StudyJoin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class Member extends AuditEntity {

    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false, updatable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickName;

    @Column
    private String image;

    @Column(columnDefinition = "TEXT")
    private String aboutMe;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    @OneToMany(mappedBy = "member", cascade = {PERSIST, MERGE})
    private List<Study> leaders;

    @OneToMany(mappedBy = "member", cascade = {PERSIST, REMOVE})
    private List<StudyJoin> studyJoins;

    @OneToMany(mappedBy = "member", cascade = {PERSIST, REMOVE})
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "member", cascade = {PERSIST, REMOVE})
    private List<Comment> comments;

    public enum Status {
        ACTIVE("active"),
        SLEEP("sleep"),
        QUIT("quit");

        @Getter
        private final String status;

        Status(String status) {
            this.status = status;
        }
    }

    public enum Provider {
        LOCAL("local"),
        GOOGLE("google"),
        KAKAO("kakao"),
        NAVER("naver");

        @Getter
        private final String provider;

        Provider(String provider) {
            this.provider = provider;
        }
    }
}
