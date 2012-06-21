# --- Sample dataset

# --- !Ups
insert into scale (id,month,portal_goal,portal_actual,han_goal,han_actual) values ( 1,'2012-05-01', 16200, 16200, 800, 2000);
insert into scale (id,month,portal_goal,portal_actual,han_goal,han_actual) values ( 2,'2012-06-01', 37800, 0, 1600, 0);
insert into scale (id,month,portal_goal,portal_actual,han_goal,han_actual) values ( 3,'2012-07-01', 59400, 0, 2000, 0);
insert into scale (id,month,portal_goal,portal_actual,han_goal,han_actual) values ( 4,'2012-08-01', 81000, 0, 2000, 0);
insert into scale (id,month,portal_goal,portal_actual,han_goal,han_actual) values ( 5,'2012-09-01', 117000, 0, 4500, 0);
insert into scale (id,month,portal_goal,portal_actual,han_goal,han_actual) values ( 6,'2012-10-01', 153000, 0, 7000, 0);
insert into scale (id,month,portal_goal,portal_actual,han_goal,han_actual) values ( 7,'2012-11-01', 189000, 0, 1200, 0);
insert into scale (id,month,portal_goal,portal_actual,han_goal,han_actual) values ( 8,'2012-12-01', 225000, 0, 1700, 0);


insert into service (id,month,percent_complete,avg_throughput) values ( 1,'2012-05-01', 0, 200);
insert into service (id,month,percent_complete,avg_throughput) values ( 2,'2012-06-01', 10, 0);
insert into service (id,month,percent_complete,avg_throughput) values ( 3,'2012-07-01', 0, 0);
insert into service (id,month,percent_complete,avg_throughput) values ( 4,'2012-08-01', 0, 0);
insert into service (id,month,percent_complete,avg_throughput) values ( 5,'2012-09-01', 0, 0);
insert into service (id,month,percent_complete,avg_throughput) values ( 6,'2012-10-01', 0, 0);
insert into service (id,month,percent_complete,avg_throughput) values ( 7,'2012-11-01', 0, 0);
insert into service (id,month,percent_complete,avg_throughput) values ( 8,'2012-12-01', 0, 0);

# --- !Downs

delete from burndown;
delete from iteration;
delete from team;
delete from release;
delete from scale;
delete from service;