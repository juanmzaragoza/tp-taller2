CREATE SEQUENCE user_id_seq;

CREATE TABLE public.users
(
    id integer NOT NULL DEFAULT nextval('user_id_seq'),
    username varchar(100) UNIQUE NOT NULL,
    password varchar(256) NOT NULL,
    token varchar(256),
    token_expiration timestamp with time zone,
    PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
);

ALTER TABLE public.users
    OWNER to postgres;