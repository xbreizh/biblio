package org.library.business.impl;

import org.apache.log4j.Logger;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateConvertedHelper {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public XMLGregorianCalendar convertDateIntoXmlDate(Date date) {
        // converting Date into XML date
        if (date == null) return null;
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        XMLGregorianCalendar xmlCalendar = null;
        try {
            xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (DatatypeConfigurationException e) {
            logger.error(e.getMessage());
        }
        return xmlCalendar;
    }


    Date convertXmlDateIntoDate(XMLGregorianCalendar startDate) {
        if (startDate == null) return null;
        return startDate.toGregorianCalendar().getTime();
    }
}
