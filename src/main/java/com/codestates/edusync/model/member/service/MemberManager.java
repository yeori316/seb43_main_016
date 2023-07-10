package com.codestates.edusync.model.member.service;

import com.codestates.edusync.model.member.entity.Member;

public interface MemberManager {
    Member get(String email);

    Member getNickName(String email);
}
