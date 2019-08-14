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
    id                SERIAL,
    dateConnect       timestamp    NULL,
    dateJoin          timestamp    NULL,
    email             varchar(255) NULL,
    firstName         varchar(255) NULL,
    lastName          varchar(255) NULL,
    login             varchar(255) NULL,
    "password"        varchar(255) NULL,
    "role"            varchar(255) NULL,
    "token"           varchar(255) NULL,
    "tokenexpiration" timestamp    NULL,
    "reminder"        BOOLEAN      NOT NULL,
    CONSTRAINT member_pkey PRIMARY KEY (id)
);



CREATE TABLE public.book
(
    id              SERIAL,
    author          varchar(255) NULL,
    edition         varchar(255) NULL,
    insert_date     timestamp    NULL,
    isbn            varchar(255) NOT NULL,
    keywords        varchar(255) NULL,
    nb_pages        int4         NULL,
    publicationYear int4         NULL,
    title           varchar(255) NULL,
    CONSTRAINT book_pkey PRIMARY KEY (id)
);

-- Drop table


CREATE TABLE public.loan
(
    id               SERIAL,
    reservation_date timestamp    NULL,
    available_date   timestamp    NULL,
    start_date       timestamp    NULL,
    planned_end_date timestamp    NULL,
    end_date         timestamp    NULL,
    isbn             varchar(255) NOT NULL,
    book_id          int4         NULL,
    borrower_id      int4         NULL,
    CONSTRAINT loan_pkey PRIMARY KEY (id),
    CONSTRAINT fkckf2g131el3qunjs9afsf6265 FOREIGN KEY (borrower_id) REFERENCES member (id),
    CONSTRAINT fkllwvq8yhcx4uqka3jay8a53o2 FOREIGN KEY (book_id) REFERENCES book (id)
);


INSERT INTO public."member" (dateconnect, datejoin, email, firstName, lastName, login, "password", "role", "token",
                             "tokenexpiration", "reminder")
VALUES (current_timestamp, '2019-01-17 17:22:58.013', 'POLI@KOL.FR', 'JOHN', 'POLI', 'JPOLINO',
        '$2a$10$QOj.jxZFXZcss0hd9em2D.6ROAgrBoFbwAj4Fwak6EqHQBFMieyhi', 'USER', '62751f44-b7db-49f5-a19c-5b98edef50db',
        '2019-01-17 17:22:58.013', false)
     , ('2019-05-27 13:24:46.270', '2019-01-16 18:58:19.604', 'LOKI@LOKI.LOKII', 'JEAN', 'MOKOTI', 'LOKII',
        '$2a$10$shu9chhwSesp/3ZIukarpu49OAnooYwCMXrlolphPbgI7Nd.doS/e', 'ADMIN', '1b0e0a5b-ae4f-41cf-a600-b8b26e2ec013',
        '2019-01-17 17:22:58.013', false)
     , ('2019-07-01 10:43:55', '2019-07-06 10:42:06', 'mom.o56@gmail.rok', 'MAURICE', 'LOMBARD', 'MOMO56',
        '$2a$10$BtyyTuY/Gt9fxZwSW5wINuapTewFUqxwomwXrsI/GQoUpI93YQvoy', 'REGULAR',
        '24ef3fb5-e487-4d37-872b-822470a14556', '2019-01-17 17:22:58.013', true)
;

INSERT INTO public.book (author, edition, insert_date, isbn, keywords, nb_pages, publicationYear, title)
VALUES ('MAURICE MOSS', 'GALLIMARD', '2019-01-18 15:25:33.546', '12345678OK', 'IT  MOON  ADVENTURE', 34, 2007,
        'LA GRANDE AVENTURE')
     , ('TEST', 'TEST', '2019-01-18 21:43:16.506', '1234567824', 'TEST', 125, 1985, 'TEST')
     , ('MAURICE MOSS', 'GALLIMARD', '2019-01-19 15:38:44.323', '12345678OK', 'IT  MOON  ADVENTURE', 34, 2007,
        'LA GRANDE AVENTURE')
     , ('MAURICE MOSS', 'GALLIMARD', '2019-01-19 15:38:44.411', '12345678OK', 'IT  MOON  ADVENTURE', 34, 2007,
        'LA GRANDE AVENTURE')
     , ('MAURICE MOSS', 'GALLIMARD', '2019-01-19 15:38:44.412', '12345678OK', 'IT  MOON  ADVENTURE', 34, 2007,
        'LA GRANDE AVENTURE')
     , ('JAMES KOLO', 'DUPION', '2019-01-19 15:38:44.412', '8574596258', 'PAST PRESENT FUTURE', 340, 1986,
        'BOKANA')
     , ('ALPHONSE DAUDET', 'FLAMMARION', '2019-02-21 15:25:33.546', '555784913P', 'MULE STOIQUE', 33, 1988,
        'MAZOR')
     , ('CHARLIE KAUFMAN', 'DULOC', '2019-02-21 15:25:33.546', '555784944P', 'MULOTIER', 33, 1988,
        'ETERNAL SUNSHINE OF THE SPOTLESS MIND')
;

INSERT INTO public.loan (reservation_date, available_date, start_date, planned_end_date, end_date, isbn, book_id,
                         borrower_id)
VALUES (NULL, NULL, '2019-01-18 15:26:17.468', '2019-01-25 00:00:00.000', '2019-01-25 16:25:26.422', '12345678OK', 3, 1)
     , (NULL, NULL, '2019-02-12 16:21:22.005', '2019-02-19 00:00:00.000', '2019-02-25 17:11:43.224', '12345678OK', 3, 1)
     , (NULL, NULL, '2018-12-22 17:15:46.294', '2018-12-29 00:00:00.000', '2018-12-24 17:17:24.356', '12345678OK', 3, 1)
     , (NULL, NULL, '2019-03-15 17:08:20.767', '2019-03-23 00:00:00.000', NULL, '8574596258', 6, 2)
     , (NULL, NULL, '2019-08-12 00:00:00.000', '2019-08-19 00:00:00.000', NULL, '1234567824', 2, 1)
     , ('2019-08-12 00:00:00.000', '2019-07-10 00:00:00.000', NULL, NULL, NULL, '1234567824', 2, 1)
     , ('2019-07-10 00:00:00.000', '2019-07-10 00:00:00.000', '2019-08-12 00:00:00.000', '2019-08-19 00:00:00.000',
        NULL, '1234567824', 2, 1)
     , ('2019-07-10 00:00:00.000', '2019-07-10 00:00:00.000', NULL, null, NULL, '1234567824', null, 1)
     , ('2019-07-10 00:00:00.000', '2019-07-10 00:00:00.000', '2019-06-12 00:00:00.000', '2019-08-02 00:00:00.000',
        NULL, '1234567824', 7, 3)
;