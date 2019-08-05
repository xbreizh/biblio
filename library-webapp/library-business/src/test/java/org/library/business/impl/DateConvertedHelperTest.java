package org.library.business.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DateConvertedHelperTest {

    private DateConvertedHelper dateConvertedHelper;

    @BeforeEach
    void init() {
        dateConvertedHelper = new DateConvertedHelper();
    }


    @Test
    void convertDateIntoXmlDate() throws ParseException {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date date = simpleDateFormat.parse("2018-09-09");
        assertEquals("2018-09-09T00:00:00.000Z", dateConvertedHelper.convertDateIntoXmlDate(date).toString());

    }

    @Test
    void convertDateIntoXmlDate1() {

        assertNull(dateConvertedHelper.convertDateIntoXmlDate(null));

    }


    @Test
    void convertXmlDateIntoDate() throws ParseException, DatatypeConfigurationException {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date date = simpleDateFormat.parse("2018-09-09");
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        assertEquals("Sun Sep 09 00:00:00 UTC 2018", dateConvertedHelper.convertXmlDateIntoDate(xmlCalendar).toString());
    }

    @Test
    void convertXmlDateIntoDate1() {

        assertNull(dateConvertedHelper.convertXmlDateIntoDate(null));
    }
}