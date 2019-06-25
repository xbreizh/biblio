-- Cleanup

DROP TABLE IF EXISTS public.loan;
DROP TABLE IF EXISTS public.member;
DROP TABLE IF EXISTS public.book;

DROP SEQUENCE IF EXISTS hibernate_sequence;
DROP SEQUENCE IF EXISTS loan_id_seq cascade;
DROP SEQUENCE IF EXISTS member_id_seq cascade;
DROP SEQUENCE IF EXISTS book_id_seq cascade;

DROP SCHEMA IF EXISTS TROPARO;


-- Start Creation

CREATE SCHEMA TROPARO;


-- Specify timezone
SET TIME ZONE 'CET';
