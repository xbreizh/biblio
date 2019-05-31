-- Drop table

-- DROP TABLE public."member";

CREATE TABLE public."member" (
        id int4 NOT NULL,
        dateconnect timestamp NULL,
        datejoin timestamp NULL,
        email varchar(255) NULL,
        firstname varchar(255) NULL,
        lastname varchar(255) NULL,
        login varchar(255) NULL,
        "password" varchar(255) NULL,
        "role" varchar(255) NULL,
        "token" varchar(255) NULL,
        CONSTRAINT member_pkey PRIMARY KEY (id)
);


-- DROP TABLE public.book;

CREATE TABLE public.book (
        id int4 NOT NULL,
        author varchar(255) NULL,
        edition varchar(255) NULL,
        insert_date timestamp NULL,
        isbn varchar(255) NULL,
        keywords varchar(255) NULL,
        nb_pages int4 NULL,
        publicationyear int4 NULL,
        title varchar(255) NULL,
        CONSTRAINT book_pkey PRIMARY KEY (id)
);

-- Drop table

-- DROP TABLE public.loan;

CREATE TABLE public.loan (
        id int4 NOT NULL,
        end_date timestamp NULL,
        planned_end_date timestamp NULL,
        start_date timestamp NULL,
        book_id int4 NULL,
        borrower_id int4 NULL,
        CONSTRAINT loan_pkey PRIMARY KEY (id),
        CONSTRAINT fkckf2g131el3qunjs9afsf6265 FOREIGN KEY (borrower_id) REFERENCES member(id),
        CONSTRAINT fkllwvq8yhcx4uqka3jay8a53o2 FOREIGN KEY (book_id) REFERENCES book(id)
);