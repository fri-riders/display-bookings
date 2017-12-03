-- Table: public.bookings

-- DROP TABLE public.bookings;

CREATE TABLE public.bookings
(
    id integer NOT NULL,
    id_accommodation integer,
    id_user integer,
    from_date date,
    to_date date,
    people integer,
    CONSTRAINT bookings_pkey PRIMARY KEY (id)
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.bookings
    OWNER to postgres;