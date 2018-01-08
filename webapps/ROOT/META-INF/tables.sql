create table signup_detail(
    user_id bigserial not null primary key ,
    user_name varchar(255) not null ,
    user_email text not null unique ,
    user_password text not null ,
    user_phone text null ,
    role text not null
);

create table cookie_management(
    user_id bigint references signup_detail(user_id) ON UPDATE CASCADE ON DELETE CASCADE,
    ip_address text not null,
    user_agent text not null,
    iambdt_cookie text unique not null,
    cookie text
);
 
create table org_details(
    org_id  bigserial not null primary key ,
    owner_id  bigint references signup_detail(user_id) ON UPDATE CASCADE ON DELETE CASCADE,
    org_name text not null unique,
    unique(org_name ,owner_id )
);

create table db_details(
    db_id  bigserial not null primary key ,
    org_id bigint references org_details(org_id) ON UPDATE CASCADE ON DELETE CASCADE,
    db_name text not null unique,
    unique(org_id,db_name)
);

create table table_details(
    table_id  bigserial not null primary key ,
    org_id bigint references org_details(org_id) ON UPDATE CASCADE ON DELETE CASCADE,
    db_id  bigint references db_details(db_id) ON UPDATE CASCADE ON DELETE CASCADE,
    table_name text not null unique,
    unique(org_id,db_id,table_name)
);

create table org_management(
    user_id bigint references signup_detail(user_id) ON UPDATE CASCADE ON DELETE CASCADE,
    org_id bigint references org_details(org_id) ON UPDATE CASCADE ON DELETE CASCADE,
    role text not null
);

create table db_management(
    user_id bigint references signup_detail(user_id) ON UPDATE CASCADE ON DELETE CASCADE ,
    db_id  bigint references db_details(db_id) ON UPDATE CASCADE ON DELETE CASCADE,
    role text not null
);

create table table_management(
    user_id bigint references signup_detail(user_id) on update cascade on delete cascade,
    table_id bigint references table_details(table_id) on update cascade on delete cascade ,
    role text not null
);

create table auth_management(
    user_id bigint references signup_detail(user_id) on update cascade on delete cascade,
    role text not null,
    auth_token text not null primary key
);
create table security_management(
    user_id bigint references signup_detail(user_id) on update cascade on delete cascade,
    question text not null,
    answer text not null
);

create table history ( 
    user_id bigint references signup_detail(user_id) on update cascade on delete cascade,
    ip_address text not null,
    signin_time varchar(100) ,
    signout_time varchar(100)
);

create table lock_management(
    user_id bigint references signup_detail(user_id) on update cascade on delete cascade,
    visible boolean ,
    org_id bigint references org_details(org_id) on update cascade on delete cascade,
    db_id bigint references db_details(db_id) on update cascade on delete cascade ,
    table_id bigint references table_details(table_id) on update cascade on delete cascade 
);