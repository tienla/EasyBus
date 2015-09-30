package com.hieund.gui.Activity;

/**
 * Created by Minh vuong on 30/06/2015.
 */
    import android.util.Log;

    import java.util.Date;
import java.util.Properties;
import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.google.android.gms.internal.em;


    public class Mail extends javax.mail.Authenticator {

        private static final String EMAIL = "bus.VietNam.report@gmail.com";
        private String email_to = "bus.VietNam.report@gmail.com";
        private static final String PASSWORD = "12365478a";
        private static final String SUBJECT = "Bao loi thong tin VietNamBus";

        private String _user;
        private String _pass;

        private String[] _to = new String[1];
        private String _from;

        private String _port;
        private String _sport;

        private String _host;

        private String _subject;
        private String _body;

        private boolean _auth;

        private boolean _debuggable;

        private Multipart _multipart;


        public Mail() {
            _host = "smtp.gmail.com"; // default smtp server
            _port = "465"; // default smtp port
            _sport = "465"; // default socketfactory port

            _user = EMAIL; // username
            _pass = PASSWORD; // password
            _from = EMAIL; // email sent from
            _to[0] = email_to;
            _subject = SUBJECT; // email subject
            _body = "jhklhjk;gjhkf;kkh;l"; // email body

            _debuggable = false; // debug mode on or off - default off
            _auth = true; // smtp authentication - default on

            _multipart = new MimeMultipart();

            // There is something wrong with MailCap, javamail can not find a handler for the multipart/mixed part, so this bit needs to be added.
            MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
            CommandMap.setDefaultCommandMap(mc);
        }

        public Mail(String user, String pass) {
            this();

            _user = user;
            _pass = pass;
        }
        
        public Mail(String sender) {
            this();

        	this.email_to = sender;
            _to[0] = email_to;

        }

        public boolean send() {
            Properties props = _setProperties();

            if(!_user.equals("") && !_pass.equals("") && _to.length > 0 && !_from.equals("") && !_subject.equals("") && !_body.equals("")) {
                Session session = Session.getInstance(props, this);

                MimeMessage msg = new MimeMessage(session);

                try {
                    msg.setFrom(new InternetAddress(_from));
                } catch (MessagingException e) {
                    e.printStackTrace();
                    Log.i("aaaa", "1");
                }

                InternetAddress[] addressTo = new InternetAddress[_to.length];
                for (int i = 0; i < _to.length; i++) {
                    try {
                        addressTo[i] = new InternetAddress(_to[i]);
                    } catch (AddressException e) {
                        e.printStackTrace();
                        Log.i("aaaa", "2");
                    }
                }
                try {
                    msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);
                } catch (MessagingException e) {
                    e.printStackTrace();
                    Log.i("aaaa", "3");
                }

                try {
                    msg.setSubject(_subject);
                } catch (MessagingException e) {
                    e.printStackTrace();
                    Log.i("aaaa", "4");
                }
                try {
                    msg.setSentDate(new Date());
                } catch (MessagingException e) {
                    e.printStackTrace();
                    Log.i("aaaa", "5");
                }

                // setup message body
                BodyPart messageBodyPart = new MimeBodyPart();
                try {
                    messageBodyPart.setText(_body);
                } catch (MessagingException e) {
                    e.printStackTrace();
                    Log.i("aaaa", "6");
                }
                try {
                    _multipart.addBodyPart(messageBodyPart);
                } catch (MessagingException e) {
                    e.printStackTrace();
                    Log.i("aaaa", "7");
                }

                // Put parts in message
                try {
                    msg.setContent(_multipart);
                } catch (MessagingException e) {
                    e.printStackTrace();
                    Log.i("aaaa", "8");
                }

                // send email
                try {
                    Transport.send(msg);
                } catch (MessagingException e) {
                    e.printStackTrace();
                    Log.i("aaaa", "9");
                }

                return true;
            } else {
                return false;
            }
        }

        public void addAttachment(String filename) throws Exception {
            BodyPart messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);

            _multipart.addBodyPart(messageBodyPart);
        }

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(_user, _pass);
        }

        private Properties _setProperties() {
            Properties props = new Properties();

            props.put("mail.smtp.host", _host);

            if(_debuggable) {
                props.put("mail.debug", "true");
            }

            if(_auth) {
                props.put("mail.smtp.auth", "true");
            }

            props.put("mail.smtp.port", _port);
            props.put("mail.smtp.socketFactory.port", _sport);
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", "false");

            return props;
        }

        // the getters and setters
        public String getBody() {
            return _body;
        }

        public void setBody(String _body) {
            this._body = _body;
        }

		public String getEmailTo() {
			return email_to;
		}

		public void setEmailTo(String email_to) {
			this.email_to = email_to;
		}


    }
