-- Drop table

DROP TABLE IF EXISTS public.loan;
DROP TABLE IF EXISTS public.member;
DROP TABLE IF EXISTS public.book;

DROP SEQUENCE IF EXISTS hibernate_sequence;
DROP SEQUENCE IF EXISTS loan_id_seq cascade;
DROP SEQUENCE IF EXISTS member_id_seq cascade;
DROP SEQUENCE IF EXISTS book_id_seq cascade;

DROP SCHEMA IF EXISTS TROPARO;

CREATE SCHEMA TROPARO;
-- Specify timezone
SET TIME ZONE 'CET';

CREATE SEQUENCE hibernate_sequence START 1;

CREATE TABLE public."member"
(
    id          SERIAL,
    dateconnect timestamp    NULL,
    datejoin    timestamp    NULL,
    email       varchar(255) NULL,
    firstName   varchar(255) NULL,
    lastName    varchar(255) NULL,
    login       varchar(255) NULL,
    "password"  varchar(255) NULL,
    "role"      varchar(255) NULL,
    "token"     varchar(255) NULL,
    "tokenexpiration" timestamp NULL,
    CONSTRAINT member_pkey PRIMARY KEY (id)
);



CREATE TABLE public.book
(
    id              SERIAL,
    author          varchar(255) NULL,
    edition         varchar(255) NULL,
    insert_date     timestamp    NULL,
    isbn            varchar(255) NULL,
    keywords        varchar(255) NULL,
    nb_pages        int4         NULL,
    publicationyear int4         NULL,
    title           varchar(255) NULL,
    CONSTRAINT book_pkey PRIMARY KEY (id)
);

-- Drop table


CREATE TABLE public.loan
(
    id               SERIAL,
    end_date         timestamp NULL,
    planned_end_date timestamp NULL,
    start_date       timestamp NULL,
    book_id          int4      NULL,
    borrower_id      int4      NULL,
    CONSTRAINT loan_pkey PRIMARY KEY (id),
    CONSTRAINT fkckf2g131el3qunjs9afsf6265 FOREIGN KEY (borrower_id) REFERENCES member (id),
    CONSTRAINT fkllwvq8yhcx4uqka3jay8a53o2 FOREIGN KEY (book_id) REFERENCES book (id)
);


INSERT INTO public."member" (dateconnect, datejoin, email, firstName, lastName, login, "password", "role", "token", "tokenexpiration" )
VALUES (current_timestamp, '2019-01-17 17:22:58.013', 'POLI@KOL.FR', 'JOHN', 'POLI', 'JPOLINO',
        '$2a$10$h0swcQCaOyuJ2CffLkVRn.vj.L2VaAqXCH2rRFGhGArN0YjVGktFK', 'USER', '62751f44-b7db-49f5-a19c-5b98edef50db', '2019-01-17 17:22:58.013')
     , ('2019-05-27 13:24:46.270', '2019-01-16 18:58:19.604', 'LOKI@LOKI.LOKII', 'JEAN', 'MOKOTI', 'LOKII',
        '$2a$10$cG0Byw7K1qHl8yBa71T4UOjDUevz4XKueiRO3rLW9BjOMBwRIdpcu', 'ADMIN', '1b0e0a5b-ae4f-41cf-a600-b8b26e2ec013', '2019-01-17 17:22:58.013')
     , ('2019-07-06 10:43:55', '2019-07-06 10:42:06', 'mom.o56@gmail.rok', 'MAURICE', 'LOMBARD', 'MOMO56',
        '$2a$10$BtyyTuY/Gt9fxZwSW5wINuapTewFUqxwomwXrsI/GQoUpI93YQvoy', 'REGULAR', '24ef3fb5-e487-4d37-872b-822470a14556', '2019-01-17 17:22:58.013')
;

INSERT INTO public.book (author, edition, insert_date, isbn, keywords, nb_pages, publicationyear, title)
VALUES ('MAURICE MOSS', 'GALLIMARD', '2019-01-18 15:25:33.546', '12345678OK', 'IT  MOON  ADVENTURE', 34, 2007,
        'LA GRANDE AVENTURE')
     , ('TEST', 'TEST', '2019-01-18 21:43:16.506', '1234567824', 'TEST', 125, 1985, 'TEST')
     , ('MAURICE MOSS', 'GALLIMARD', '2019-01-19 15:38:44.323', '12345678OK', 'IT  MOON  ADVENTURE', 34, 2007,
        'LA GRANDE AVENTURE')
     , ('MAURICE MOSS', 'GALLIMARD', '2019-01-19 15:38:44.411', '12345678OK', 'IT  MOON  ADVENTURE', 34, 2007,
        'LA GRANDE AVENTURE')
     , ('MAURICE MOSS', 'GALLIMARD', '2019-01-19 15:38:44.412', '12345678OK', 'IT  MOON  ADVENTURE', 34, 2007,
        'LA GRANDE AVENTURE')
;

INSERT INTO public.loan (end_date, planned_end_date, start_date, book_id, borrower_id)
VALUES ('2019-01-19 16:25:26.422', '2019-01-24 00:00:00.000', '2019-01-18 15:26:17.468', 3, 1)
     , ('2019-01-22 17:11:43.224', '2019-01-24 00:00:00.000', '2019-01-19 16:21:22.005', 3, 1)
     , ('2019-01-22 17:17:24.356', '2019-01-24 00:00:00.000', '2019-01-22 17:15:46.294', 3, 1)
     , (NULL, '2019-01-16 00:00:00.000', '2019-01-22 17:08:20.767', 5, 2)
     , (NULL, '2019-02-13 00:00:00.000', '2019-01-12 00:00:00.000', 2, 1)
;