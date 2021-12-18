

CREATE DATABASE  `questionnaire` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

#问卷
create table paper
(
    id          varchar(64)   not null
        primary key,
    user_id     varchar(64)   not null,
    title       varchar(64)   not null,
    create_time datetime      not null,
    status      int default 0 not null,
    start_time  datetime      null,
    end_time    datetime      null
);



#问题
create table question
(
    id              varchar(64)  not null
        primary key,
    paper_id        varchar(64)  not null,
    create_time     datetime     not null,
    question_type   int          not null,
    question_title  varchar(128) not null,
    question_option varchar(512) not null
)
    charset = ucs2;



#答题
create table answer
(
    id             varchar(64)  not null
        primary key,
    paper_id       varchar(64)  not null,
    question_id    varchar(64)  not null,
    question_type  int          not null,
    create_time    datetime     not null,
    answer_content varchar(512) not null,
    user_id        varchar(64)  null
);

#score
create table score
(
    id    varchar(64) null,
    score int         null,
    time  datetime    null
);


create table user
(
    id            varchar(64)   not null
        primary key,
    username      varchar(64)   not null,
    password      varchar(64)   not null,
    email         varchar(64)   not null,
    create_time   datetime      not null,
    surgery_date  datetime      null,
    status        int default 0 not null,
    random_code   varchar(64)   not null,
    parent_id     varchar(64)   null,
    real_name     varchar(64)   null,
    age           varchar(64)   null,
    gender        varchar(64)   null,
    education     varchar(64)   null,
    race          varchar(64)   null,
    smoke_history varchar(64)   null,
    comorbidity   varchar(64)   null,
    etoh_history  varchar(64)   null,
    identity      int           null,
    constraint random_code
        unique (random_code)
);
