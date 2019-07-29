
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
    "reminder"          BOOLEAN NOT NULL,
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




CREATE TABLE public.loan
(
    id               SERIAL,
    reservation_date timestamp NULL,
    available_date       timestamp NULL,
    start_date       timestamp NULL,
    planned_end_date timestamp NULL,
    end_date         timestamp NULL,
    isbn            varchar(255) NOT NULL,
    book_id          int4      NULL,
    borrower_id      int4      NULL,
    CONSTRAINT loan_pkey PRIMARY KEY (id),
    CONSTRAINT fkckf2g131el3qunjs9afsf6265 FOREIGN KEY (borrower_id) REFERENCES member (id),
    CONSTRAINT fkllwvq8yhcx4uqka3jay8a53o2 FOREIGN KEY (book_id) REFERENCES book (id)
);
