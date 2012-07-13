# --- Sample dataset

# --- !Ups

alter table iteration add column completed_hours integer;
alter table iteration add column completed_points integer;

# --- !Downs

alter table iteration drop column completed_hours;
alter table iteration drop column completed_points;


