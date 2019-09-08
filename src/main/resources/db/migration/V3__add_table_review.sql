
create table review(
id serial primary key,
version integer,
schedule_id integer,
local_date_time_review timestamp,
service integer,
equipment integer,
qualification_specialist integer,
effectiveness_of_treatment  integer,
rating_overall integer,
comment text,
CONSTRAINT schedule_id UNIQUE(schedule_id)
);
