package com.amazon.notification.service.impl;

import com.amazon.notification.config.ProjectConfig;
import com.amazon.notification.service.NotificationManager;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Service(value = "notificationManager")
@Transactional(propagation = Propagation.REQUIRED)
public class NotificationManagerImpl implements NotificationManager {

    protected static final Logger LOGGER = LoggerFactory.getLogger(NotificationManagerImpl.class);

    @Inject
    private JavaMailSender javaMailSender;

    private VelocityEngine velocityEngine;

    @Autowired
    protected Environment env;

    public NotificationManagerImpl() {
        try {
            velocityEngine = new VelocityEngine();
            // This sets Velocity to use our own logging implementation so we can use SLF4J
            velocityEngine.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS, LOGGER);
            velocityEngine.setProperty("resource.loader", "class");
            velocityEngine.setProperty("class.resource.loader.description", "Classpath Loader");
            velocityEngine.setProperty("class.resource.loader.class",
                    "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            // Init
            velocityEngine.init();
        } catch (Exception e) {
            LOGGER.error("Could not initialise velocity engine!", e);
        }
    }

    @Override
    public void sendEnquiryEmail(String name, String email, String enquiry) {
        final String emailTitle = "You have received a new enquiry from Compact Interview";

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("email", email);
        map.put("enquiry", enquiry);

        String emailBody = renderTemplate(map, "contact.vm");

        sendEmail(email, env.getRequiredProperty(ProjectConfig.CONFIG_ADMIN_EMAIL_ADDRESSES_PROPERTY, String[].class),
                new String[]{}, emailTitle, emailBody);

        System.out.println("Enquiry sent from: " + email);
    }

    // Automatically add the header and footer to the email
    public void sendEmail(String from, String[] to, String[] bcc, String subject, String body) {
        Map<String, Object> context = new HashMap<String, Object>();

        StringBuilder completeBody = new StringBuilder();
        completeBody.append(renderTemplate(context, "include/header.vm"));
        completeBody.append(body);
        completeBody.append(renderTemplate(context, "include/footer.vm"));

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = null;

        try {
            messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setTo(to);
            Address[] bccAddresses = new Address[bcc.length];
            for (int i = 0; i < bcc.length; i++) {
                bccAddresses[i] = new InternetAddress(bcc[i]);
            }
            message.addRecipients(Message.RecipientType.BCC, bccAddresses);
            messageHelper.setFrom(from);
            messageHelper.setSubject(subject);
            messageHelper.setText(completeBody.toString(), true);

            javaMailSender.send(messageHelper.getMimeMessage());
        } catch (MessagingException e) {
            LOGGER.error("Could send email", e);
        }
    }

    public String renderTemplate(Map<String, Object> map, String template) {
        // build our context map
        VelocityContext velocityContext = new VelocityContext();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            velocityContext.put(entry.getKey(), entry.getValue());
        }

        // Try the renderTemplate, log any problems
        final Writer writer = new StringWriter();
        try {
            velocityEngine.mergeTemplate(template, velocityContext, writer);
        } catch (Exception e) {
            LOGGER.error("Could not renderTemplate template {}", "email", e);
        }

        return writer.toString();
    }
}
