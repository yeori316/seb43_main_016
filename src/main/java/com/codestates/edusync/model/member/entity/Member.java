package com.codestates.edusync.model.member.entity;

import com.codestates.edusync.model.common.entity.AuditEntity;
import com.codestates.edusync.model.schedule.entity.MemberSchedule;
import com.codestates.edusync.model.schedule.entity.ScheduleRef;
import com.codestates.edusync.model.study.comment.entity.Comment;
import com.codestates.edusync.model.study.study.entity.Study;
import com.codestates.edusync.model.study.studyjoin.entity.StudyJoin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.CascadeType.REMOVE;

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

    @Column(nullable = false)
    private String image;

    @Column(columnDefinition = "TEXT")
    private String aboutMe;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    @Column
    private LocalDateTime deletedAt;


    @OneToMany(mappedBy = "member", cascade = REMOVE)
    private List<MemberSchedule> memberSchedules;

    @OneToMany(mappedBy = "leader", cascade = REMOVE)
    private List<Study> leaders;

    @OneToMany(mappedBy = "member", cascade = REMOVE)
    private List<StudyJoin> studyJoins;

    @OneToMany(mappedBy = "member", cascade = REMOVE)
    private List<ScheduleRef> scheduleRefs;

    @OneToMany(mappedBy = "member", cascade = REMOVE)
    private List<Comment> comments;


    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;


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
