# --- Sample dataset

# --- !Ups
insert into release (id,name) values (1, 'Grays');

insert into team (id,name,release_id) values ( 1,'Energize',1);
insert into team (id,name,release_id) values ( 2,'Enterprise',1);
insert into team (id,name,release_id) values ( 3,'HER',1);

insert into iteration (id,name,iteration_start,iteration_end,team_id) values (1,'2012-05-21','2012-05-21','2012-06-01', 1);
insert into iteration (id,name,iteration_start,iteration_end,team_id) values (2,'2012-05-21','2012-05-21','2012-06-01', 2);
insert into iteration (id,name,iteration_start,iteration_end,team_id) values (3,'2012-05-21','2012-05-21','2012-06-01', 3);

insert into burndown (id,day,hours,ideal,points,iteration_id) values (  1,'2012-05-21', 120, 120, 0, 1);
insert into burndown (id,day,hours,ideal,points,iteration_id) values (  2,'2012-05-22', 103, 90, 4, 1);
insert into burndown (id,day,hours,ideal,points,iteration_id) values (  3,'2012-05-23', 81, 60, 12, 1);
insert into burndown (id,day,hours,ideal,points,iteration_id) values (  4,'2012-05-24', 38, 30, 16, 1);

insert into burndown (id,day,hours,ideal,points,iteration_id) values (  5,'2012-05-21', 120, 120, 0, 2);
insert into burndown (id,day,hours,ideal,points,iteration_id) values (  6,'2012-05-22', 109, 90, 3, 2);
insert into burndown (id,day,hours,ideal,points,iteration_id) values (  7,'2012-05-23', 77, 60, 17, 2);
insert into burndown (id,day,hours,ideal,points,iteration_id) values (  8,'2012-05-24', 12, 30, 30, 2);

insert into burndown (id,day,hours,ideal,points,iteration_id) values (  9,'2012-05-21', 120, 120, 0, 3);
insert into burndown (id,day,hours,ideal,points,iteration_id) values ( 10,'2012-05-22', 94, 90, 14, 3);
insert into burndown (id,day,hours,ideal,points,iteration_id) values ( 11,'2012-05-23', 30, 60, 22, 3);
insert into burndown (id,day,hours,ideal,points,iteration_id) values ( 12,'2012-05-24', 7, 30, 29, 3);

# --- !Downs

delete from burndown;
delete from iteration;
delete from team;
delete from release;