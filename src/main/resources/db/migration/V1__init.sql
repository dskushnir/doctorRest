create table doctor(
id serial primary key,
name varchar(255)
);

create table doctor_specialization(
doctor_id integer,
specialization varchar(255)
);

create table doctor_specializations(
doctor_id integer,
specializations text
);

create table hour_to_pet_id(
id serial primary key,
hour varchar(255),
pet_id varchar(255)
);

create table pet(
id serial primary key,
name varchar(255)
);

create table schedule(
id serial primary key,
version integer,
doctor_id integer,
hour varchar(255),
pet_id varchar(255),
visit_date date
);
