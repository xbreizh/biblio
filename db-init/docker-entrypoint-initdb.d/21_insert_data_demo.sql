INSERT INTO public."member" (dateconnect, datejoin, email, firstName, lastName, login, "password", "role", "token", "tokenexpiration" )
VALUES (current_timestamp, '2019-01-17 17:22:58.013', 'POLI@KOL.FR', 'JOHN', 'POLI', 'JPOLINO',
        '$2a$10$h0swcQCaOyuJ2CffLkVRn.vj.L2VaAqXCH2rRFGhGArN0YjVGktFK', 'USER', '62751f44-b7db-49f5-a19c-5b98edef50db', '2019-01-17 17:22:58.013')
     , ('2019-05-27 13:24:46.270', '2019-01-16 18:58:19.604', 'LOKI@LOKI.LOKII', 'JEAN', 'MOKOTI', 'LOKII',
        '$2a$10$iLOQnw2UohWVA4JwX2shx.RPQdX6Y3CO./.Xpy689.pAIR9/I8uri', 'ADMIN', '1b0e0a5b-ae4f-41cf-a600-b8b26e2ec013', '2019-01-17 17:22:58.013')
     , ('2019-07-06 10:43:55', '2019-07-06 10:42:06', 'mom.o56@gmail.rok', 'MAURICE', 'LOMBARD', 'MOMO56',
        '$2a$10$BtyyTuY/Gt9fxZwSW5wINuapTewFUqxwomwXrsI/GQoUpI93YQvoy', 'REGULAR', '24ef3fb5-e487-4d37-872b-822470a14556', '2019-01-17 17:22:58.013')
;

INSERT INTO public.book (author,edition,insert_date,isbn,keywords,nb_pages,publicationyear,title) VALUES 
('MAURICE MOSS','GALLIMARD','2019-01-18 15:25:33.546','12345678OK','IT  MOON  ADVENTURE',34,2007,'LA GRANDE AVENTURE')
,('TEST','TEST','2019-01-18 21:43:16.506','1234567824','TEST',125,1985,'TEST')
,('MAURICE MOSS','GALLIMARD','2019-01-19 15:38:44.323','12345678OK','IT  MOON  ADVENTURE',34,2007,'LA GRANDE AVENTURE')
,('MAURICE MOSS','GALLIMARD','2019-01-19 15:38:44.411','12345678OK','IT  MOON  ADVENTURE',34,2007,'LA GRANDE AVENTURE')
,('MAURICE MOSS','GALLIMARD','2019-01-19 15:38:44.412','12345678OK','IT  MOON  ADVENTURE',34,2007,'LA GRANDE AVENTURE')
;

INSERT INTO public.loan (end_date, planned_end_date, start_date, book_id, borrower_id, checked)
VALUES ('2019-01-25 16:25:26.422', '2019-01-25 00:00:00.000', '2019-01-18 15:26:17.468', 3, 1, true)
     , ('2019-02-25 17:11:43.224', '2019-02-19 00:00:00.000', '2019-02-12 16:21:22.005', 3, 1, true)
     , ('2018-12-24 17:17:24.356', '2018-12-29 00:00:00.000', '2018-12-22 17:15:46.294', 3, 1, true)
     , (NULL, '2019-03-23 00:00:00.000', '2019-03-15 17:08:20.767', 6, 2, false)
     , (NULL, '2019-08-19 00:00:00.000', '2019-08-12 00:00:00.000', 2, 1, false)
;