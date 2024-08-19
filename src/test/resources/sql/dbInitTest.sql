CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "postgis";

CREATE TABLE IF NOT EXISTS "user"
(
    uuid uuid NOT NULL DEFAULT uuid_generate_v4(),
    login character varying(255) COLLATE pg_catalog."default" NOT NULL,
    password character varying(255) COLLATE pg_catalog."default" NOT NULL,
    created_at timestamp without time zone NOT NULL,
    user_name character varying(250) COLLATE pg_catalog."default" NOT NULL DEFAULT 'Unknown'::character varying,
    CONSTRAINT user_pkey PRIMARY KEY (uuid)
);

CREATE TABLE public."location" (
	"uuid" uuid DEFAULT uuid_generate_v4() NOT NULL,
	postal_code varchar(255) NULL,
	country varchar(255) NULL,
	area varchar(255) NULL,
	city varchar(255) NULL,
	street varchar(255) NULL,
	house_number varchar(255) NULL,
	address_line varchar(255) NULL,
	CONSTRAINT location_address_line_key UNIQUE (address_line),
	CONSTRAINT location_pkey PRIMARY KEY (uuid)
);

CREATE TYPE public.geo_point_type AS ENUM ('PRIVATE','PUBLIC');

CREATE TABLE public.geo_point (
	uuid uuid DEFAULT uuid_generate_v4() NOT NULL,
	user_uuid uuid NOT NULL,
	latitude numeric(35, 25) NOT NULL,
	longitude numeric(35, 25) NOT NULL,
	name varchar(255) NOT NULL,
	description varchar(255) NOT NULL,
	"type" public.geo_point_type NOT NULL,
	created_at timestamptz NOT NULL,
	live timestamptz NOT NULL,
	point public.geometry(point, 4326) NOT NULL,
	grade numeric(5) DEFAULT 0 NOT NULL,
	user_latitude numeric(35, 25) NULL,
	user_longitude numeric(35, 25) NULL,
	location_id uuid NULL,
	CONSTRAINT geo_point_pkey PRIMARY KEY (uuid),
	CONSTRAINT location_id_fk FOREIGN KEY (location_id) REFERENCES public."location"(uuid),
	CONSTRAINT user_uuid_fk FOREIGN KEY (user_uuid) REFERENCES public."user"(uuid)
);

CREATE TABLE public.geo_point_history (
	"uuid" uuid DEFAULT uuid_generate_v4() NOT NULL,
	user_uuid uuid NOT NULL,
	latitude numeric(35, 25) NOT NULL,
	longitude numeric(35, 25) NOT NULL,
	"name" varchar(255) NOT NULL,
	description varchar(255) NOT NULL,
	"type" public."geo_point_type" NOT NULL,
	created_at timestamptz NOT NULL,
	live timestamptz NOT NULL,
	point public.geometry(point, 4326) NOT NULL,
	grade numeric(5) NOT NULL,
	user_latitude numeric(35, 25) NULL,
	user_longitude numeric(35, 25) NULL,
	location_id uuid NULL,
	CONSTRAINT geo_point_history_pkey PRIMARY KEY (uuid)
);
ALTER TABLE public.geo_point_history ADD CONSTRAINT location_id_fk FOREIGN KEY (location_id) REFERENCES public."location"("uuid");
ALTER TABLE public.geo_point_history ADD CONSTRAINT user_uuid_fk FOREIGN KEY (user_uuid) REFERENCES public."user"("uuid");

CREATE TYPE public."grade_type" AS ENUM (
	'PLUS',
	'MINUS');

CREATE TABLE public.grade_log (
	"uuid" uuid DEFAULT uuid_generate_v4() NOT NULL,
	user_uuid uuid NOT NULL,
	"grade_type" public."grade_type" NOT NULL,
	geo_point_uuid uuid NOT NULL,
	"date" timestamptz DEFAULT now() NOT NULL,
	CONSTRAINT grade_log_pkey PRIMARY KEY (uuid)
);
ALTER TABLE public.grade_log ADD CONSTRAINT user_uuid_fk FOREIGN KEY (user_uuid) REFERENCES public."user"("uuid");

CREATE TABLE public.geo_point_comments (
	"uuid" uuid DEFAULT uuid_generate_v4() NOT NULL,
	user_uuid uuid NOT NULL,
	geo_point_uuid uuid NOT NULL,
	"text" varchar(250) NOT NULL,
	created_at timestamptz DEFAULT now() NOT NULL,
	CONSTRAINT geo_point_comments_pkey PRIMARY KEY (uuid),
	CONSTRAINT geo_point_uuid_fk FOREIGN KEY (geo_point_uuid) REFERENCES public.geo_point("uuid"),
	CONSTRAINT user_uuid_fk FOREIGN KEY (user_uuid) REFERENCES public."user"("uuid")
);

CREATE TABLE public.geo_point_comments_history (
	"uuid" uuid DEFAULT uuid_generate_v4() NOT NULL,
	user_uuid uuid NOT NULL,
	geo_point_uuid uuid NOT NULL,
	"text" varchar(250) NOT NULL,
	created_at timestamptz DEFAULT now() NOT NULL,
	CONSTRAINT geo_point_comments_history_pkey PRIMARY KEY (uuid)
);
ALTER TABLE public.geo_point_comments_history ADD CONSTRAINT user_uuid_fk FOREIGN KEY (user_uuid) REFERENCES public."user"("uuid");

CREATE TYPE public."tariff_type" AS ENUM (
	'YEAR');

CREATE TABLE public.tariff (
	uuid uuid DEFAULT uuid_generate_v4() NOT NULL,
	name varchar(255) NOT NULL,
	"type" public.tariff_type NOT NULL,
	price numeric(5, 2) NOT NULL,
	subtitle varchar(250) DEFAULT ''::character varying NOT NULL,
	CONSTRAINT tariff_pkey PRIMARY KEY (uuid)
);

CREATE TABLE public.payment_parameters (
	"uuid" uuid DEFAULT uuid_generate_v4() NOT NULL,
	terminal_key varchar(250) NOT NULL,
	public_key varchar NOT NULL,
	CONSTRAINT payment_parameters_pkey PRIMARY KEY (uuid)
);

CREATE TABLE public."subscription" (
	"uuid" uuid DEFAULT uuid_generate_v4() NOT NULL,
	tariff_uuid uuid NOT NULL,
	user_uuid uuid NOT NULL,
	end_at timestamp NOT NULL,
	CONSTRAINT subscription_pkey PRIMARY KEY (uuid),
	CONSTRAINT tariff_uuid_fk FOREIGN KEY (tariff_uuid) REFERENCES public.tariff("uuid"),
	CONSTRAINT user_uuid_fk FOREIGN KEY (user_uuid) REFERENCES public."user"("uuid")
);
CREATE INDEX ix_tariff ON public.subscription USING btree (uuid, tariff_uuid);
CREATE INDEX ix_tariff_user ON public.subscription USING btree (uuid, tariff_uuid, user_uuid);

CREATE TYPE public."order_status" AS ENUM (
	'PENDING',
	'DONE',
	'ERROR');

CREATE TABLE public."order" (
	id int4 GENERATED BY DEFAULT AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	user_uuid uuid NOT NULL,
	subscription_uuid uuid NULL,
	tariff_uuid uuid NOT NULL,
	status public."order_status" NOT NULL,
	created_at timestamp DEFAULT now() NOT NULL,
	updated_at timestamp DEFAULT now() NOT NULL,
	CONSTRAINT order_pkey PRIMARY KEY (id),
	CONSTRAINT subscription_uuid_fk FOREIGN KEY (subscription_uuid) REFERENCES public."subscription"("uuid"),
	CONSTRAINT tariff_uuid_fk FOREIGN KEY (tariff_uuid) REFERENCES public.tariff("uuid"),
	CONSTRAINT user_uuid_fk FOREIGN KEY (user_uuid) REFERENCES public."user"("uuid")
);

INSERT INTO "user"(uuid,login,password,created_at) VALUES('1751ba42-3936-4284-bd2f-4e48eb39e900'::uuid, 'test1@mail.ru', '$2a$16$.yH7bEhsOSKOHOMtxlmte.aH2u4Orll5tdywyX.IH4i4X/MuzEQxG',now());
INSERT INTO "user"(uuid,login,password,created_at) VALUES('1751ba42-3936-4284-bd2f-4e48eb39e911'::uuid, 'test2@mail.ru', '$2a$16$.yH7bEhsOSKOHOMtxlmte.aH2u4Orll5tdywyX.IH4i4X/MuzEQxG',now());
INSERT INTO tariff(uuid,name,type,price,subtitle) VALUES('a7b73ce0-015e-471f-9725-02b05a710ea1'::uuid,'Годовой (базовый)','YEAR'::tariff_type,100,'Базовый тариф');
INSERT INTO payment_parameters(terminal_key,public_key) VALUES('1234','MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAv');

INSERT INTO "location"(uuid,postal_code,country,area,city,street,house_number,address_line) VALUES('f6ff1629-2324-4cb5-b211-3c691d08caf9'::uuid,'450007','Россия','Уфимский район','Уфа','Бану Валеевой улица','5','ул. Бану Валеевой, 5, Уфа, Башкортостан Респ., Россия, 450007');
INSERT INTO geo_point(uuid,user_uuid,latitude,longitude,name,description,"type",created_at,live,point,grade,user_latitude,user_longitude,location_id)
VALUES('834955b7-c8aa-43fe-a47e-54f7597ba671'::uuid,'1751ba42-3936-4284-bd2f-4e48eb39e900'::uuid,54.7968070982345000000000000,55.9531652927399000000000000,'Локация','Локация','PRIVATE'::geo_point_type,'2024-08-06 11:49:03.907 +0500','2024-08-13 11:49:03.907 +0500',ST_SetSRID(ST_MakePoint(55.95316529273987, 54.796807098234524), 4326)::geometry,0,54.7963137086481000000000000,55.9558569733053000000000000,'f6ff1629-2324-4cb5-b211-3c691d08caf9'::uuid);
INSERT INTO geo_point(uuid,user_uuid,latitude,longitude,name,description,"type",created_at,live,point,grade,user_latitude,user_longitude,location_id)
VALUES('a9346b4b-00c4-4438-96a6-5d6fb9bf7eb9'::uuid,'1751ba42-3936-4284-bd2f-4e48eb39e900'::uuid,54.7968070982345000000000000,55.9531652927399000000000000,'Локация','Локация','PUBLIC'::geo_point_type,'2024-08-06 11:49:03.907 +0500','2024-08-13 11:49:03.907 +0500',ST_SetSRID(ST_MakePoint(55.95316529273987, 54.796807098234524), 4326)::geometry,3,54.7963137086481000000000000,55.9558569733053000000000000,'f6ff1629-2324-4cb5-b211-3c691d08caf9'::uuid);
INSERT INTO geo_point(uuid,user_uuid,latitude,longitude,name,description,"type",created_at,live,point,grade,user_latitude,user_longitude,location_id)
VALUES('834955b7-c8aa-43fe-a47e-54f7597ba688'::uuid,'1751ba42-3936-4284-bd2f-4e48eb39e911'::uuid,54.7968070982345000000000000,55.9531652927399000000000000,'Локация','Локация','PRIVATE'::geo_point_type,'2024-08-06 11:49:03.907 +0500','2024-08-13 11:49:03.907 +0500',ST_SetSRID(ST_MakePoint(55.95316529273987, 54.796807098234524), 4326)::geometry,0,54.7963137086481000000000000,55.9558569733053000000000000,'f6ff1629-2324-4cb5-b211-3c691d08caf9'::uuid);
INSERT INTO geo_point_comments(user_uuid,geo_point_uuid,"text",created_at) VALUES('1751ba42-3936-4284-bd2f-4e48eb39e900'::uuid,'834955b7-c8aa-43fe-a47e-54f7597ba671'::uuid,'Норм место','2024-08-06 12:00:03.907 +0500');