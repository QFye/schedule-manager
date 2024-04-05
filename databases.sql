SET FOREIGN_KEY_CHECKS = 1;

use schedule_system;

create table schedule_system.admin
(
    id       int auto_increment comment 'ID'
        primary key,
    username varchar(255) null comment '用户名',
    password varchar(255) null comment '密码',
    name     varchar(255) null comment '姓名',
    avatar   varchar(255) null comment '头像',
    role     varchar(255) null comment '角色标识',
    phone    varchar(255) null comment '电话',
    email    varchar(255) null comment '邮箱'
)
    comment '管理员' collate = utf8mb4_unicode_ci;

create table schedule_system.business
(
    id          int auto_increment
        primary key,
    name        varchar(255)                       not null,
    password    varchar(255)                       not null,
    phone       varchar(32)                        null,
    email       varchar(32)                        null,
    avatar      varchar(255)                       null,
    role        varchar(32)  default 'BUSINESS'    not null,
    username    varchar(255)                       not null,
    description varchar(255) default '该商家未设置商家简介。' not null,
    address     varchar(255) default '无'           null
)
    comment 'basic business information';



create table schedule_system.event
(
    id          int auto_increment
        primary key,
    name        varchar(100)                           not null,
    description longtext                               null,
    img         varchar(255)                           null,
    start       datetime     default CURRENT_TIMESTAMP not null,
    last        int          default 0                 not null,
    categoryId  int          default 1                 not null,
    price       float        default 0                 not null,
    status      varchar(32)  default 'STATIC'          not null,
    count       int          default 0                 not null,
    businessId  int                                    null,
    address     varchar(255) default '无'               not null
)
    comment 'basic event information';





create table schedule_system.eventcategory
(
    id          int auto_increment
        primary key,
    name        varchar(100)  not null,
    description varchar(255)  null,
    img         varchar(1024) null
)
    comment 'basic event category information';

create table schedule_system.notice
(
    id      int auto_increment comment 'ID'
        primary key,
    title   varchar(255) null comment '标题',
    content varchar(255) null comment '内容',
    time    varchar(255) null comment '创建时间',
    user    varchar(255) null comment '创建人'
)
    comment '公告信息表' collate = utf8mb4_unicode_ci;

create table schedule_system.`order`
(
    id         int auto_increment
        primary key,
    price      float        not null,
    userId     int          not null,
    businessId int          not null,
    details    varchar(255) null
)
    comment 'basic order information';

create table schedule_system.schedulecategory
(
    id          int auto_increment
        primary key,
    name        varchar(100) not null,
    description varchar(255) null,
    img         varchar(255) null
)
    comment 'basic schedule category information';

create table schedule_system.templatecategory
(
    id          int auto_increment
        primary key,
    name        varchar(255) not null,
    description longtext     null,
    img         varchar(255) null
);

create table schedule_system.user
(
    id       int auto_increment
        primary key,
    username varchar(50)                 not null,
    name     varchar(50)                 not null,
    password varchar(50)                 not null,
    sex      tinyint(1)   default 1      not null comment 'use "true" to represent male and "false" to represent female',
    email    varchar(100)                null,
    phone    varchar(20)                 null,
    avatar   varchar(255)                not null,
    role     varchar(255) default 'USER' not null,
    money    float        default 0      not null
)
    comment 'basic user information';



create table schedule_system.schedule
(
    id         int auto_increment
        primary key,
    categoryId varchar(50) not null,
    date       date        not null,
    userId     int         not null,
    constraint schedule_table_user_table_account_number_fk
        foreign key (userId) references schedule_system.user (id)
)
    comment 'basic schedule information';

create table schedule_system.scheduleeventrelation
(
    scheduleId int not null,
    eventId    int not null,
    primary key (scheduleId, eventId),
    constraint schedule_event_relationship_event_table_event_id_fk
        foreign key (eventId) references schedule_system.event (id),
    constraint schedule_event_relationship_schedule_table_schedule_id_fk
        foreign key (scheduleId) references schedule_system.schedule (id)
)
    comment 'relationship between schedules and events';

create table schedule_system.team
(
    id           int auto_increment
        primary key,
    name         varchar(100)                       not null,
    creationTime datetime default CURRENT_TIMESTAMP null,
    userId       int                                not null,
    constraint team_user_id_fk
        foreign key (userId) references schedule_system.user (id)
)
    comment 'basic team information';

create table schedule_system.teammemberrelationship
(
    userId int not null,
    teamId int not null,
    primary key (userId, teamId),
    constraint team_member_relationship_team_table_team_id_fk
        foreign key (teamId) references schedule_system.team (id),
    constraint team_member_relationship_user_table_account_number_fk
        foreign key (userId) references schedule_system.user (id)
)
    comment 'relationship between teams and users';

create table schedule_system.template
(
    id           int auto_increment
        primary key,
    name         varchar(255)                       not null,
    userId       int                                null,
    creationTime datetime default CURRENT_TIMESTAMP null,
    categoryId   int                                not null,
    price        float    default 0                 not null,
    businessId   int                                null,
    constraint template_table_user_table_account_number_fk
        foreign key (userId) references schedule_system.user (id),
    constraint template_templatecategory_id_fk
        foreign key (categoryId) references schedule_system.templatecategory (id)
)
    comment 'basic template information';

create table schedule_system.comment
(
    id             int auto_increment
        primary key,
    userId         int                                   not null,
    templateId     int                                   null,
    commentContent text                                  not null,
    time           datetime    default CURRENT_TIMESTAMP null,
    section        varchar(32) default 'EVENT'           not null,
    eventId        int                                   null,
    constraint comment_event_id_fk
        foreign key (eventId) references schedule_system.event (id),
    constraint comment_table_schedule_table_schedule_id_fk
        foreign key (templateId) references schedule_system.template (id),
    constraint comment_table_user_table_account_number_fk
        foreign key (userId) references schedule_system.user (id)
)
    comment 'comment basic information';



create table schedule_system.templateapplication
(
    scheduleId int not null,
    templateId int not null,
    primary key (scheduleId, templateId),
    constraint template_application_schedule_table_schedule_id_fk
        foreign key (scheduleId) references schedule_system.schedule (id),
    constraint template_application_template_table_template_id_fk
        foreign key (scheduleId) references schedule_system.template (id)
)
    comment 'relationship that reflects the application of templates in each schedule';

create table schedule_system.templateeventrelation
(
    templateId int not null,
    eventId    int not null,
    primary key (eventId, templateId),
    constraint template_event_relationship_event_table_event_id_fk
        foreign key (eventId) references schedule_system.event (id),
    constraint template_event_relationship_template_table_template_id_fk
        foreign key (templateId) references schedule_system.template (id)
)
    comment 'relationship between templates and events';

create table schedule_system.collect
(
    id         int auto_increment
        primary key,
    userId     int not null,
    businessId int not null,
    constraint collect_business_id_fk
        foreign key (businessId) references schedule_system.business (id),
    constraint collect_user_id_fk
        foreign key (userId) references schedule_system.user (id)
)
    comment 'basic collect information';



INSERT INTO
    `admin`
VALUES (
        1, 'admin', 'admin', '管理员', 'http://localhost:9090/files/1697438073596-avatar.png', 'ADMIN', '13677889922', 'admin@xm.com'
    );

INSERT INTO
    `notice`
VALUES (
        1, '今天系统正式上线，开始内测', '今天系统正式上线，开始内测', '2023-09-05', 'admin'
    );

INSERT INTO
    `notice`
VALUES (
        2, '所有功能都已完成，可以正常使用', '所有功能都已完成，可以正常使用', '2023-09-05', 'admin'
    );

INSERT INTO
    `notice`
VALUES (
        3, '今天天气很不错，可以出去一起玩了', '今天天气很不错，可以出去一起玩了', '2023-09-05', 'admin'
    );

INSERT INTO schedule_system.eventcategory (id, name, description, img) VALUES (1, '运动/健身/锻炼', '运动类事件', 'http://localhost:9090/files/1709814959024-运动.png');
INSERT INTO schedule_system.eventcategory (id, name, description, img) VALUES (2, '旅游/景区/游玩', '旅行类事件', 'http://localhost:9090/files/1709383167603-flag_6759362.png');
INSERT INTO schedule_system.eventcategory (id, name, description, img) VALUES (3, '小吃/餐饮/饭馆', '用餐服务', 'http://localhost:9090/files/1709814141727-餐馆.png');
INSERT INTO schedule_system.eventcategory (id, name, description, img) VALUES (4, '酒店/旅馆/住宿', '住宿服务', 'http://localhost:9090/files/1709814214294-住宿.png');
INSERT INTO schedule_system.eventcategory (id, name, description, img) VALUES (5, '乐园/公园/游玩', '乐园服务', 'http://localhost:9090/files/1709814332001-游乐园.png');
INSERT INTO schedule_system.eventcategory (id, name, description, img) VALUES (6, 'K歌/酒吧/娱乐', '娱乐服务', 'http://localhost:9090/files/1709814505980-K歌.png');
INSERT INTO schedule_system.eventcategory (id, name, description, img) VALUES (7, '书馆/文化/学习', '文化服务', 'http://localhost:9090/files/1709814760600-书.png');
INSERT INTO schedule_system.eventcategory (id, name, description, img) VALUES (8, '洗浴/汗蒸/温泉', '洗浴服务', 'http://localhost:9090/files/1709815162093-洗浴.png');
INSERT INTO schedule_system.eventcategory (id, name, description, img) VALUES (9, '医疗/美容/体检', '医疗服务', 'http://localhost:9090/files/1709815244894-医疗.png');
INSERT INTO schedule_system.eventcategory (id, name, description, img) VALUES (10, '结婚/摄影/庆典', '摄影服务', 'http://localhost:9090/files/1709815372670-摄影.png');
INSERT INTO schedule_system.eventcategory (id, name, description, img) VALUES (11, '生活/理发/维修', '生活服务', 'http://localhost:9090/files/1709815456106-维修.png');
INSERT INTO schedule_system.eventcategory (id, name, description, img) VALUES (12, '出行/租车/交通', '出行服务', 'http://localhost:9090/files/1709815539398-出行.png');
INSERT INTO schedule_system.eventcategory (id, name, description, img) VALUES (13, '购物/商城/果蔬', '购物服务', 'http://localhost:9090/files/1709815616076-购物.png');
INSERT INTO schedule_system.eventcategory (id, name, description, img) VALUES (14, '电影/演出/展览', '电影服务', 'http://localhost:9090/files/1709815769237-电影.png');