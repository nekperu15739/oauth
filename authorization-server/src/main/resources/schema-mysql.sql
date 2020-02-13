-- auto-generated definition
create table USER_ENTITY
(
    ID                          varchar(36)               not null
        primary key,
    EMAIL                       varchar(255)              null,
    EMAIL_CONSTRAINT            varchar(255)              null,
    EMAIL_VERIFIED              bit default b'0'          not null,
    ENABLED                     bit default b'0'          not null,
    FEDERATION_LINK             varchar(255)              null,
    FIRST_NAME                  varchar(255) charset utf8 null,
    LAST_NAME                   varchar(255) charset utf8 null,
    REALM_ID                    varchar(255)              null,
    USERNAME                    varchar(255) charset utf8 null,
    CREATED_TIMESTAMP           bigint                    null,
    SERVICE_ACCOUNT_CLIENT_LINK varchar(36)               null,
    NOT_BEFORE                  int default 0             not null,
    constraint UK_DYKN684SL8UP1CRFEI6ECKHD7
        unique (REALM_ID, EMAIL_CONSTRAINT),
    constraint UK_RU8TT6T700S9V50BU18WS5HA6
        unique (REALM_ID, USERNAME)
);

create index IDX_USER_EMAIL
    on USER_ENTITY (EMAIL);

-- auto-generated definition
create table CREDENTIAL
(
    ID              varchar(36)  not null
        primary key,
    SALT            tinyblob     null,
    TYPE            varchar(255) null,
    USER_ID         varchar(36)  null,
    CREATED_DATE    bigint       null,
    USER_LABEL      varchar(255) null,
    SECRET_DATA     longtext     null,
    CREDENTIAL_DATA longtext     null,
    PRIORITY        int          null,
    constraint FK_PFYR0GLASQYL0DEI3KL69R6V0
        foreign key (USER_ID) references USER_ENTITY (ID)
);

create index IDX_USER_CREDENTIAL
    on CREDENTIAL (USER_ID);

