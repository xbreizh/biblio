package org.troparo.business.impl;

import org.apache.log4j.Logger;
import org.troparo.business.contract.MailManager;
import org.troparo.business.contract.LoanManager;
import org.troparo.model.Loan;
import org.troparo.model.Mail;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

/*@Component
@Configuration
@EnableScheduling
@PropertySource("classpath:mail.properties")*/
@Named
public class MailManagerImpl implements MailManager {
    private Logger logger = Logger.getLogger(MailManagerImpl.class);

    @Inject
    LoanManager loanManager;


   /* @Value("${sender}")
    private String mailFrom;
    @Value("${fileLocation}")
    private String fileLocation;
    @Value("${subject}")
    private String subject;
    @Value("${body}")
    private String body;
    @Value("${mailTemplateLocation}")
    private String templateLocation;
    @Value("${mailServer}")
    private String mailServer;
    @Value("${mailServerPort}")
    private String port;*/


    /*  public static final String AES = "AES";*/

    //  @Scheduled(cron = "*/10 * * * * *")
  /*  public void sendMail() {
        final String username = "xavier.lamourec@gmail.com";


        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", mailServer);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        try {
                            return new PasswordAuthentication(username, getPassword());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailFrom));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("dontkillewok@gmail.com"));
            message.setSubject(subject);
            message.setText(body);
            String test = "markolo";

            List<Loan> overdueList = getOverdueList();
            logger.info("overdue list size: "+overdueList.size());
            for (Loan loan: overdueList
                 ) {
                logger.info("loan id: "+loan.getId());
                String text = createMailContent(loan);

                //HTML mail content
                String htmlText = readEmailFromHtml(templateLocation,loan);
                logger.info("html to be sent: "+htmlText);

                message.setContent(htmlText, "text/html");
                logger.info("sending email to "+loan.getBorrower().getEmail());
                try {
                    logger.info("mail content: "+message.getContent().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                 Transport.send(message);

            }

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }*/




    //Method to replace the values for keys
   /* protected String readEmailFromHtml(String filePath, Loan loan)
    {
        Map<String, String> input = getTemplateItems(loan);


        String msg = readContentFromFile(filePath);
        try
        {
            Set<Map.Entry<String, String>> entries = input.entrySet();
            for(Map.Entry<String, String> entry : entries) {
                msg = msg.replace(entry.getKey().trim(), entry.getValue().trim());
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return msg;
    }*/

  /*  private Map<String, String> getTemplateItems(Loan loan) {
        //Set key values
        Map<String, String> input = new HashMap<String, String>();
        input.put("FIRSTNAME", loan.getBorrower().getFirstName());
        input.put("LASTNAME", loan.getBorrower().getLastName());
        SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
        String dueDate = dt1.format(loan.getPlannedEndDate());
        input.put("DUEDATE", dueDate);
        Date today = new Date();
        int overDays = calculateDaysBetweenDates(new Date(), loan.getPlannedEndDate());
        input.put("Isbn", loan.getBook().getIsbn());
        input.put("DIFFDAYS", Integer.toString(overDays));
        input.put("TITLE", loan.getBook().getTitle());
        input.put("AUTHOR", loan.getBook().getAuthor());
        input.put("EDITION", loan.getBook().getEdition());
        return input;
    }*/

    //Method to read HTML file as a String
  /*  private String readContentFromFile(String fileName)
    {
        StringBuffer contents = new StringBuffer();

        try {
            //use buffering, reading one line at a time
            BufferedReader reader =  new BufferedReader(new FileReader(fileName));
            try {
                String line = null;
                while (( line = reader.readLine()) != null){
                    contents.append(line);
                    contents.append(System.getProperty("line.separator"));
                }
            }
            finally {
                reader.close();
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        return contents.toString();
    }
*/

   /* private String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }*/

   /* private byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }*/

  /*  private List<Loan> getOverdueList(){
        HashMap<String, String> criterias = new HashMap<>();
        criterias.put("status", "OVERDUE");
        logger.info("getting overdue list");
        return loanManager.getLoansByCriterias(criterias);
    }*/
    @Override
    public int calculateDaysBetweenDates(Date d1, Date d2){/*
        String format = "MM/dd/yyyy hh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(format);*/
        long diff = d2.getTime() - d1.getTime();
        //int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
        return (int) diff / (24 * 60 * 60 * 1000);
    }


   /* private String createMailContent(Loan loan){
        Member member = loan.getBorrower();
        Book book = loan.getBook();
        SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");

      *//*  String body="Dear "+member.getFirstName()+" "+member.getLastName()+"<br><br>" +
                "This is to inform you that the following loan is overdue by "+overDays+" days as you were supposed to return the following item by " +
                dt1.format(loan.getPlannedEndDate())+".<br>   " +
                "ISBN: "+book.getIsbn()+"<br>"+
                "Title: "+book.getTitle()+"<br>"+
                "Author: "+book.getAuthor()+"<br>"+
                "Edition: "+book.getEdition()+"<br>"+
                "As a reminder, according to our policy, a fee of 1 euro is applied per day per item.<br>" +
                "Please return that item as soon as possible <br>" +
                "Best Regards<br>" +
                "Library Loan Manager";*//*

        return null;
    }*/


  /*  private String getPassword() throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String tempkey = "";
        String password = "";
        Properties prop = new Properties();
        InputStream input = null;
        input = new FileInputStream(fileLocation);
        // load a properties file
        prop.load(input);
        tempkey = prop.getProperty("Key");
        password = prop.getProperty("Encrypted_Password");

        byte[] bytekey = hexStringToByteArray(tempkey);
        SecretKeySpec sks = new SecretKeySpec(bytekey, AES);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, sks);
        byte[] decrypted = cipher.doFinal(hexStringToByteArray(password));
        String OriginalPassword = new String(decrypted);

        return OriginalPassword;
    }*/
    @Override
    public List<Mail> getOverdueEmailList() {
        HashMap<String, String> criterias = new HashMap<>();
        criterias.put("status", "OVERDUE");
        logger.info("getting overdue list");
        List<Loan> loans = loanManager.getLoansByCriterias(criterias);
        /*List<Mail> mailList = new ArrayList<>();*/
        System.out.println("loans: "+loans.size());
        List<Mail> mailList = new ArrayList<>();
        for (Loan loan: loans
        ) {
            Mail mail = new Mail();
            mail.setEmail(loan.getBorrower().getEmail());
            mail.setFirstname(loan.getBorrower().getFirstName());
            mail.setLastname(loan.getBorrower().getLastName());
            mail.setIsbn(loan.getBook().getIsbn());
            mail.setTitle(loan.getBook().getTitle());
            mail.setAuthor(loan.getBook().getAuthor());
            mail.setEdition(loan.getBook().getEdition());
            mail.setDueDate(loan.getPlannedEndDate());
            int overDays = calculateDaysBetweenDates(getTodaySDate(), loan.getPlannedEndDate());
            mail.setDiffdays(overDays);
            mailList.add(mail);
        }
        return mailList;
    }

    Date getTodaySDate(){
        return new Date();
    }

    /*@Override
    public List<Mail> createMailListfromLoans(List<Loan> loans) {
        List<Mail> mailList = new ArrayList<>();
        for (Loan loan: loans
             ) {
            Mail mail = new Mail();
            mail.setEmail(loan.getBorrower().getEmail());
            mail.setFirstname(loan.getBorrower().getFirstName());
            mail.setLastname(loan.getBorrower().getLastName());
            mail.setIsbn(loan.getBook().getIsbn());
            mail.setTitle(loan.getBook().getTitle());
            mail.setAuthor(loan.getBook().getAuthor());
            mail.setEdition(loan.getBook().getEdition());
            mail.setDueDate(loan.getPlannedEndDate());
            int overDays = calculateDaysBetweenDates(new Date(), loan.getPlannedEndDate());
            mail.setDiffdays(overDays);
            mailList.add(mail);
        }
        return mailList;
    }*/

    public void setLoanManager(LoanManager loanManager) {
        this.loanManager = loanManager;
    }
}
