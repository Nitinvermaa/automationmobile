package com.gl.testngfw.email;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Mail sender.
 */
public class MailSender {
    private static final Logger LOGGER = Logger.getLogger(MailSender.class.getName());
    /**
     * Application name.
     */
    private static final String APPLICATION_NAME =
            "Gmail GlobalLogic Automation";
    /**
     * Directory to store user credentials for this application.
     */
    private static final File DATA_STORE_DIR = new File(
            "email/.credentials/GlobalLogic-automation-gmail");
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();
    /**
     * Global instance of the scopes required by this quickstart.
     */
    private static final List<String> SCOPES =
            Collections.singletonList(GmailScopes.MAIL_GOOGLE_COM);
    /**
     * Properties relative path for email address
     */
    private static final String EMAIL_PROPERTIES_RELATIVE_PATH = "email/email_recipients.txt";
    private static EmailConfig emailConfig = new EmailConfig();
    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private static FileDataStoreFactory DATA_STORE_FACTORY;
    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Exception t) {
            LOGGER.log(Level.WARNING, "", t);
            System.exit(1);
        }
    }

    private MailSender() {
    }

    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException the io exception
     */
    private static Credential authorize() throws IOException {
        // Load client secrets.
        String fileName = "email/client_secret.json";

        InputStream in = new FileInputStream(fileName);
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        return new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
    }

    /**
     * Build and return an authorized Gmail client service.
     *
     * @return an authorized Gmail client service
     * @throws IOException the io exception
     */
    private static Gmail getGmailService() throws IOException {
        Credential credential = authorize();
        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Create email mime message.
     *
     * @return the mime message
     * @throws MessagingException the messaging exception
     */
    private static MimeMessage createEmail(String file) throws MessagingException {
        Properties props = new Properties();
        javax.mail.Session session = javax.mail.Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        try {
            if (!emailConfig.getMailToRecipients().isEmpty()) {
                String[] recipientsToList = emailConfig.getMailToRecipients().trim().split(";");
                for (String eachRecipient : recipientsToList) {
                    email.addRecipient(javax.mail.Message.RecipientType.TO,
                            new InternetAddress(eachRecipient));
                }
            }
            if (!emailConfig.getMailCCRecipients().isEmpty()) {
                String[] recipientsCcList = emailConfig.getMailCCRecipients().trim().split(";");
                for (String eachRecipient : recipientsCcList) {
                    email.addRecipient(javax.mail.Message.RecipientType.CC,
                            new InternetAddress(eachRecipient));
                }
            }
            if (!emailConfig.getMailBCCRecipients().isEmpty()) {
                String[] recipientsBccList = emailConfig.getMailBCCRecipients().trim().split(";");
                for (String eachRecipient : recipientsBccList) {
                    email.addRecipient(javax.mail.Message.RecipientType.CC,
                            new InternetAddress(eachRecipient));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Failed to read file. Sending email to default recipients");
            LOGGER.log(Level.WARNING, "", e);
            email.addRecipient(javax.mail.Message.RecipientType.TO,
                    new InternetAddress("akshay.chimote@globallogic.com"));
            email.addRecipient(javax.mail.Message.RecipientType.TO,
                    new InternetAddress("rashmi.z@globallogic.com"));
        }
        email.setSubject(emailConfig.getMailSubject());

        String str = SendEmail.createMailContents();
        MimeBodyPart mimeBodyPart = new MimeBodyPart();

        mimeBodyPart.setContent(str, "text/html; charset=utf-8");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);
        mimeBodyPart = new MimeBodyPart();

        String logoFileName = emailConfig.getMailLogoImage();
        DataSource source = new FileDataSource(new File(logoFileName));
        mimeBodyPart.setDataHandler(new DataHandler(source));
        mimeBodyPart.setFileName(logoFileName);

        //Updated
        DataSource source1 = new FileDataSource(file);
        if (((FileDataSource) source1).getFile().exists()) {
            MimeBodyPart attachPart = new MimeBodyPart();
            attachPart.setDataHandler(new DataHandler(source1));
            attachPart.setFileName(new File(file).getName());
            multipart.addBodyPart(attachPart);
        }
        mimeBodyPart.setHeader("Content-ID", "<image>");
        multipart.addBodyPart(mimeBodyPart);
        email.setContent(multipart);
        return email;
    }

    /**
     * Create message with email message.
     *
     * @param email the Mime Message email
     * @return the message
     * @throws IOException        the io exception
     * @throws MessagingException the messaging exception
     */
    private static Message createMessageWithEmail(MimeMessage email)
            throws IOException, MessagingException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        email.writeTo(bytes);
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes.toByteArray());
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }


    /**
     * Send message.
     *
     * @param service the gmail service
     * @param userId  the user id of the email
     * @param email   the Mime Message email
     * @throws IOException        the io exception
     * @throws MessagingException the messaging exception
     */
    private static void sendMessage(Gmail service, String userId, MimeMessage email)
            throws IOException, MessagingException {
        Message message = createMessageWithEmail(email);
        try {
            service.users().messages().send(userId, message).execute();
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Internet Error. Failed to send Email", e);
        }
    }

    /**
     * Send mail
     *
     * @throws IOException the io exception
     */
    public static void sendMail(String fileName) throws IOException {
        Gmail service = getGmailService();
        try {
            MimeMessage m = createEmail(fileName);
            sendMessage(service, "me", m);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "", e);
        }
    }

    public static Set<String> loadEmailAddress() throws IOException {
        Properties p = new Properties();
        InputStream input = MailSender.class.getResourceAsStream(EMAIL_PROPERTIES_RELATIVE_PATH);
        p.load(input);
        return p.stringPropertyNames();
    }

    public static void main(String[] args) throws IOException {
        sendMail("");
    }
}