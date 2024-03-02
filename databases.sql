create schema schedule_system;

use schedule_system;

DROP TABLE IF EXISTS admin;

CREATE TABLE Admin (
    `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID', `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名', `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码', `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名', `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像', `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色标识', `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话', `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱', PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员' ROW_FORMAT = DYNAMIC;

INSERT INTO
    `admin`
VALUES (
        1, 'admin', 'admin', '管理员', 'http://localhost:9090/files/1697438073596-avatar.png', 'ADMIN', '13677889922', 'admin@xm.com'
    );

DROP TABLE IF EXISTS notice;

CREATE TABLE Notice (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID', `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题', `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '内容', `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建时间', `user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人', PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '公告信息表' ROW_FORMAT = DYNAMIC;

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

SET FOREIGN_KEY_CHECKS = 1;

use schedule_system;

DROP TABLE IF EXISTS admin;

CREATE TABLE Admin (
    `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID', `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名', `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码', `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名', `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像', `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色标识', `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话', `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱', PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员' ROW_FORMAT = DYNAMIC;

INSERT INTO
    `admin`
VALUES (
        1, 'admin', 'admin', '管理员', 'http://localhost:9090/files/1697438073596-avatar.png', 'ADMIN', '13677889922', 'admin@xm.com'
    );

DROP TABLE IF EXISTS notice;

CREATE TABLE Notice (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID', `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题', `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '内容', `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建时间', `user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人', PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '公告信息表' ROW_FORMAT = DYNAMIC;

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

SET FOREIGN_KEY_CHECKS = 1;

DROP TABLE IF EXISTS template_application;

DROP TABLE IF EXISTS schedule_event_relationship;

DROP TABLE IF EXISTS template_event_relationship;

DROP TABLE IF EXISTS team_member_relationship;

DROP TABLE IF EXISTS event_category_table;

DROP TABLE IF EXISTS comment_table;

DROP TABLE IF EXISTS template_table;

DROP TABLE IF EXISTS event_table;

DROP TABLE IF EXISTS team_table;

DROP TABLE IF EXISTS schedule_table;

DROP TABLE IF EXISTS user_table;

create table User (
    id int auto_increment, account_number varchar(50) not null, name varchar(50) not null, password varchar(50) not null, sex bool default true not null comment 'use "true" to represent male and "false" to represent female', email varchar(100) null, phone_number varchar(20) null, constraint user_table_pk primary key (id)
) comment 'basic user information';

create table Schedule (
    id int auto_increment, type varchar(50) not null, date date not null, user_id int not null, constraint schedule_table_pk primary key (id), constraint schedule_table_user_table_account_number_fk foreign key (user_id) references user (id)
) comment 'basic schedule information';

create table Template (
    id int auto_increment, creation_time datetime not null, user_id int not null, constraint template_table_pk primary key (id), constraint template_table_user_table_account_number_fk foreign key (user_id) references user (id)
) comment 'basic template information';

create table Event (
    id int auto_increment, name varchar(100) not null, start_time datetime null, last_time time not null, description text null, constraint event_table_pk primary key (id)
) comment 'basic event information';

create table Team (
    id int auto_increment, name varchar(100) not null, creation_time datetime not null, constraint team_table_pk primary key (id)
) comment 'basic team information';

create table TemplateApplication (
    schedule_id int not null, template_id int not null, primary key (schedule_id, template_id), constraint template_application_schedule_table_schedule_id_fk foreign key (schedule_id) references schedule (id), constraint template_application_template_table_template_id_fk foreign key (schedule_id) references template (id)
) comment 'relationship that reflects the application of templates in each schedule';

create table ScheduleEventRelation (
    schedule_id int not null, event_id int not null, constraint schedule_event_relationship_event_table_event_id_fk foreign key (event_id) references event (id), primary key (schedule_id, event_id), constraint schedule_event_relationship_schedule_table_schedule_id_fk foreign key (schedule_id) references schedule (id)
) comment 'relationship between schedules and events';

create table TemplateEventRelationship (
    template_id int not null, event_id int not null, primary key (event_id, template_id), constraint template_event_relationship_event_table_event_id_fk foreign key (event_id) references event (id), constraint template_event_relationship_template_table_template_id_fk foreign key (template_id) references template (id)
) comment 'relationship between templates and events';

create table Comment (
    id int auto_increment, user_id int not null, schedule_id int not null, comment_content text not null, constraint schedule_table_pk primary key (id), constraint comment_table_schedule_table_schedule_id_fk foreign key (schedule_id) references schedule (id), constraint comment_table_user_table_account_number_fk foreign key (user_id) references user (id)
) comment 'comment basic information';

create table TeamMemberRelationship (
    user_id int not null, team_id int not null, primary key (user_id, team_id), constraint team_member_relationship_team_table_team_id_fk foreign key (team_id) references team (id), constraint team_member_relationship_user_table_account_number_fk foreign key (user_id) references user (id)
) comment 'relationship between teams and users';

create table EventCategory (
    id int auto_increment, name varchar(100) not null, description varchar(255) null, image varchar(255) null, constraint event_category_table_pk primary key (id)
) comment 'basic event category information';