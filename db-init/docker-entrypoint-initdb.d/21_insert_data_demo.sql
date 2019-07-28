INSERT INTO public."member" (dateconnect, datejoin, email, firstName, lastName, login, "password", "role", "token",
                             "tokenexpiration")
VALUES (current_timestamp, '2019-01-17 17:22:58.013', 'POLI@KOL.FR', 'JOHN', 'POLI', 'JPOLINO',
        '$2a$10$h0swcQCaOyuJ2CffLkVRn.vj.L2VaAqXCH2rRFGhGArN0YjVGktFK', 'USER', '62751f44-b7db-49f5-a19c-5b98edef50db',
        '2019-01-17 17:22:58.013')
     , ('2019-05-27 13:24:46.270', '2019-01-16 18:58:19.604', 'LOKI@LOKI.LOKII', 'JEAN', 'MOKOTI', 'LOKII',
        '$2a$10$shu9chhwSesp/3ZIukarpu49OAnooYwCMXrlolphPbgI7Nd.doS/e', 'ADMIN', '1b0e0a5b-ae4f-41cf-a600-b8b26e2ec013',
        '2019-01-17 17:22:58.013')
     , ('2019-07-06 10:43:55', '2019-07-06 10:42:06', 'mom.o56@gmail.rok', 'MAURICE', 'LOMBARD', 'MOMO56',
        '$2a$10$BtyyTuY/Gt9fxZwSW5wINuapTewFUqxwomwXrsI/GQoUpI93YQvoy', 'REGULAR',
        '24ef3fb5-e487-4d37-872b-822470a14556', '2019-01-17 17:22:58.013')
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
;

INSERT INTO public.loan (reservation_date, available_date, start_date, planned_end_date, end_date, isbn, book_id, borrower_id)
VALUES (NULL, NULL, '2019-01-18 15:26:17.468', '2019-01-25 00:00:00.000','2019-01-25 16:25:26.422',  '12345678OK', 3, 1)
     , (NULL, NULL, '2019-02-12 16:21:22.005', '2019-02-19 00:00:00.000', '2019-02-25 17:11:43.224', '12345678OK',  3, 1)
     , (NULL, NULL, '2018-12-22 17:15:46.294', '2018-12-29 00:00:00.000', '2018-12-24 17:17:24.356', '12345678OK',  3, 1)
     , (NULL, NULL, '2019-03-15 17:08:20.767', '2019-03-23 00:00:00.000', NULL,  '8574596258', 6, 2)
     , (NULL, NULL, '2019-08-12 00:00:00.000', '2019-08-19 00:00:00.000', NULL, '1234567824',  2, 1)
;