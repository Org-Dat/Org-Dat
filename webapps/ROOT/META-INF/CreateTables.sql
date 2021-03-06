create table signup_detail(user_id bigserial not null primary key ,user_name varchar(255) not null ,user_email text not null unique ,user_password text not null ,user_phone text null ,role text not null);
create table cookie_management(user_id bigint references signup_detail(user_id) ON UPDATE CASCADE ON DELETE CASCADE,ip_address text not null,user_agent text not null,iambdt_cookie text unique not null,cookie text);
create table org_management(user_id bigint references signup_detail(user_id) ON UPDATE CASCADE ON DELETE CASCADE,org_name text primary key,role text not null);
create table db_management(user_id bigint references signup_detail(user_id) ON UPDATE CASCADE ON DELETE CASCADE ,org_name text references org_management(org_name) ON UPDATE CASCADE ON DELETE CASCADE,db_name text not null primary key,role text not null);
create table table_management(user_id bigint references signup_detail(user_id) on update cascade on delete cascade,role text not null, org_name text references org_management(org_name) on update cascade on delete cascade,db_name text references db_management(db_name) on update cascade on delete cascade , table_name text not null);
create table auth_management(user_id bigint references signup_detail(user_id) on update cascade on delete cascade,role text not null,auth_token text not null primary key);
create table security_management(user_id bigint references signup_detail(user_id) on update cascade on delete cascade,question text not null,answer text not null);

create table history (user_id bigint references signup_detail(user_id) on update cascade on delete cascade,ip_address text not null,signin_time varchar(100) ,signout_time varchar(100))


create table lock_management(
    user_id bigint references signup_detail(user_id) on update cascade on delete cascade,
    visible boolean ,
    org_name text references org_management(org_name) on update cascade on delete cascade,
    db_name text references db_management(db_name) on update cascade on delete cascade ,
    table_name text not null
);
