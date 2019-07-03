package org.troparo.web.service.helper;

import org.apache.log4j.Logger;

import javax.inject.Named;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Named
public class DateConvertedHelper {

    private Logger logger = Logger.getLogger(DateConvertedHelper.class);

    public XMLGregorianCalendar convertDateIntoXmlDate(Date date) throws DatatypeConfigurationException {
        // converting Date into XML date
        if (date == null) return null;
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        XMLGregorianCalendar xmlCalendar;

        xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
       logger.info("date to return: "+xmlCalendar);
        return xmlCalendar;
    }


}
