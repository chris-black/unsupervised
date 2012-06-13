# --- First database schema

# --- !Ups

create table burndown (
  id                        bigint not null,
  day                       varchar(50),
  hours                     integer,
  ideal                     integer,
  points                    integer,
  iteration_id 	       		bigint,
  constraint pk_burndown primary key (id))
;

create table iteration (
  id                        bigint not null,
  name                      varchar(255),
  obj_id                    varchar(255),
  iteration_start			timestamp,
  iteration_end 			timestamp,
  total_hours               integer,
  total_points              integer,
  team_id 	           		bigint,
  constraint pk_iteration primary key (id))
;

create table release (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_release primary key (id))
;

create table team (
  id                        bigint not null,
  obj_id                    varchar(255),
  name                      varchar(255),
  release_id 	            bigint,
  constraint pk_team primary key (id))
;

create sequence burndown_seq start 1000;
create sequence iteration_seq start 1000;
create sequence release_seq start 1000;
create sequence team_seq start 1000;

alter table team add constraint fk_team_release_1 foreign key (release_id) references release (id) on delete restrict on update restrict;
alter table iteration add constraint fk_iteration_team_1 foreign key (team_id) references team (id) on delete restrict on update restrict;
alter table burndown add constraint fk_burndown_iteration_1 foreign key (iteration_id) references iteration (id) on delete restrict on update restrict;


# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists burndown;
drop table if exists iteration;
drop table if exists release;
drop table if exists team;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists burndown_seq;
drop sequence if exists iteration_seq;
drop sequence if exists release_seq;
drop sequence if exists team_seq;
