drop TABLE nmdc_v1.set_member;
drop TABLE nmdc_v1.set;


CREATE TABLE nmdc_v1.set (
    id serial primary key,
    name character varying(128),
    spec character varying(64)
);

ALTER TABLE nmdc_v1.set OWNER TO nmdc_owner;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE nmdc_v1.set TO nmdc;


CREATE TABLE nmdc_v1.set_member (
    set_id integer REFERENCES nmdc_v1.set(id),	
    dataset_id character varying(100) references nmdc_v1.dataset(id)
);

ALTER TABLE nmdc_v1.set OWNER TO nmdc_owner;

insert into nmdc_v1.set (spec,name) select distinct 'Orginating Center',originating_center from nmdc_v1.dataset;
insert into nmdc_v1.set_member (set_id,dataset_id) select  s.id,d.id from nmdc_v1.dataset d,nmdc_v1.set s where d.originating_center = s.name ;
