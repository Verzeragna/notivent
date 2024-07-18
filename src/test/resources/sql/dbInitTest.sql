CREATE SCHEMA IF NOT EXISTS mvp;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp" SCHEMA mvp;
CREATE EXTENSION IF NOT EXISTS "postgis" SCHEMA mvp;

CREATE TABLE IF NOT EXISTS mvp."user"
(
    uuid uuid NOT NULL DEFAULT mvp.uuid_generate_v4(),
    login character varying(255) COLLATE pg_catalog."default" NOT NULL,
    password character varying(255) COLLATE pg_catalog."default" NOT NULL,
    created_at timestamp without time zone NOT NULL,
    user_name character varying(250) COLLATE pg_catalog."default" NOT NULL DEFAULT 'Unknown'::character varying,
    CONSTRAINT user_pkey PRIMARY KEY (uuid)
);

INSERT INTO mvp."user"(login,password,created_at) VALUES('test1@mail.ru', '$2a$16$.yH7bEhsOSKOHOMtxlmte.aH2u4Orll5tdywyX.IH4i4X/MuzEQxG',now());
