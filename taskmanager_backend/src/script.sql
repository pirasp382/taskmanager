drop table if exists user_status;
drop table if exists user_priority;
drop table if exists task;
DROP TABLE IF EXISTS status;
DROP TABLE IF EXISTS priority;
DROP TABLE IF EXISTS user;

-- Status table
CREATE TABLE status
(
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    color VARCHAR(255)
);

-- Priority table
CREATE TABLE priority
(
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    color VARCHAR(255)
);

-- User table
CREATE TABLE user
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    username  VARCHAR(255) NOT NULL,
    password  VARCHAR(255) NOT NULL,
    fullname  VARCHAR(255),
    bio       TEXT,
    avatarUrl VARCHAR(255),
    lastLogin DATETIME,
    createdAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    email     VARCHAR(255) UNIQUE
);

create table user_status(
                            userId bigint,
                            statusId bigint,
                            primary key (userId, statusId),
                            foreign key (userId) references user (id) on delete cascade ,
                            foreign key (statusId) references status(id) on delete cascade
);

create table user_priority(
                              userId bigint,
                              priorityId bigint,
                              primary key (userId, priorityId),
                              foreign key (userId) references user(id) on delete cascade ,
                              foreign key (priorityId) references priority(id) on delete cascade
);

-- Task table
CREATE TABLE task
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    createdDate DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lastUpdate  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    userId      BIGINT       NOT NULL,
    statusId    BIGINT       NOT NULL,
    priorityId  BIGINT       NOT NULL,
    FOREIGN KEY (userId) REFERENCES user (id),
    FOREIGN KEY (statusId) REFERENCES status (id),
    FOREIGN KEY (priorityId) REFERENCES priority (id)
);

insert into  status(title)
values
    ('todo'),
    ('in_progress'),
    ('done');

insert into  priority(title)
values
    ('low'),
    ('middle'),
    ('high');