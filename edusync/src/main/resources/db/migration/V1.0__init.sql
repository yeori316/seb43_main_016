create table if not exists member (
    id            bigint primary key auto_increment not null,
    uuid          varchar(100) not null,
    email         varchar(100) not null unique,
    password      varchar(100) not null,
    nick_name     varchar(100) not null unique,
    image         blob,
    about_me      TEXT,
    with_me       TEXT,
    status        varchar(50) not null,
    roles         varchar(50) not null,
    provider      varchar(50) not null,
    created_at    timestamp not null default now(),
    modified_at   timestamp not null default now(),
    deleted_at    timestamp
);

create table if not exists study_join (
    id            bigint primary key auto_increment not null,
    is_approved   boolean not null default false,
    member_id     bigint not null,
    study_id      bigint not null,
    created_at    timestamp not null default now(),
    modified_at   timestamp not null default now()
);

create table if not exists study (
    id              bigint primary key auto_increment not null,
    study_name      varchar(100) not null,
    image           blob,
    member_min      int,
    member_max      int,
    platform        varchar(100) not null,
    introduction    TEXT not null,
    is_requited     boolean default false,
    member_id       bigint not null,
    created_at      timestamp not null default now(),
    modified_at     timestamp not null default now()
);

create table if not exists day_of_week (
    id          bigint primary key auto_increment not null,
    study_id    bigint not null,
    sunday      boolean not null default false,
    monday      boolean not null default false,
    tuesday     boolean not null default false,
    wednesday   boolean not null default false,
    thursday    boolean not null default false,
    friday      boolean not null default false,
    saturday    boolean not null default false
);

create table if not exists schedule (
    id              bigint primary key auto_increment not null,
    title           varchar(100) not null,
    description     varchar(255),
    color           varchar(50),
    start_day       date not null,
    end_day         date not null,
    start_time      time not null,
    end_time        time not null,
    member_id       bigint not null,
    study_id        bigint not null
);

create table if not exists comment (
    id            bigint primary key auto_increment not null,
    content       varchar(255) not null,
    member_id     bigint not null,
    study_id      bigint not null,
    created_at    timestamp,
    modified_at   timestamp
);

create table if not exists tag_ref (
    id          bigint primary key auto_increment not null,
    study_id    bigint not null,
    tag_id      bigint not null
);

create table if not exists tag (
    id            bigint primary key auto_increment not null,
    tag_value     varchar(100) not null
);

alter table study add foreign key (member_id) references member (id);
alter table study_join add foreign key (member_id) references member (id);
alter table schedule add foreign key (member_id) references member (id);
alter table comment add foreign key (member_id) references member (id);

alter table study_join add foreign key (study_id) references study (id);
alter table day_of_week add foreign key (study_id) references study (id);
alter table schedule add foreign key (study_id) references study (id);
alter table comment add foreign key (study_id) references study (id);
alter table tag_ref add foreign key (study_id) references study (id);

alter table tag_ref add foreign key (tag_id) references tag (id);