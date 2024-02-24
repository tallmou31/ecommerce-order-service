package sn.esmt.mp2isi.ecommerce.orderservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import sn.esmt.mp2isi.ecommerce.orderservice.domain.Order;
import sn.esmt.mp2isi.ecommerce.orderservice.domain.OrderItem;
import sn.esmt.mp2isi.ecommerce.orderservice.service.dto.OrderItemResponseDTO;
import sn.esmt.mp2isi.ecommerce.orderservice.service.dto.OrderResponseDTO;
import sn.esmt.mp2isi.ecommerce.orderservice.service.dto.UserDTO;
import tech.jhipster.config.JHipsterProperties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;

@Service
public class MailService {
    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";

    private final JHipsterProperties jHipsterProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    public MailService(
        JHipsterProperties jHipsterProperties,
        JavaMailSender javaMailSender,
        MessageSource messageSource,
        SpringTemplateEngine templateEngine
    ) {
        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(String[] to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug(
            "Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart,
            isHtml,
            to,
            subject,
            content
        );

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (MailException | MessagingException e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }

    @Async
    public void sendCommandeEmailToAdmin(String [] to, OrderResponseDTO order) {
        Locale locale = Locale.forLanguageTag("fr");
        Context context = new Context(locale);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        context.setVariable("order", order);
        context.setVariable("total", order.getTotal());

        String content = templateEngine.process("mail/orderNotifEmail", context);
        sendEmail(to,"[SENCOMMERCE] - NOTIFICATION NOUVELLE COMMANDE", content, false, true);
    }

    @Async
    public void sendCommandeEmailToCustomer(UserDTO user, OrderResponseDTO order, LocalDate expectedDeliveryDate, String deliveryAddress) {
        Locale locale = Locale.forLanguageTag("fr");
        Context context = new Context(locale);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        context.setVariable("order", order);
        context.setVariable("nom", String.format("%s %s", user.getFirstName(), user.getLastName()));
        context.setVariable("total", order.getTotal());
        context.setVariable("deliveryAddress", deliveryAddress);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        context.setVariable("deliveryDate", formatter.format(expectedDeliveryDate));


        String content = templateEngine.process("mail/newOrderCustomerEmail", context);
        sendEmail(new String[]{user.getEmail()},"[SENCOMMERCE] - NOTIFICATION NOUVELLE COMMANDE", content, false, true);
    }

    @Async
    public void sendDeliveredCommandeEmailToCustomer(UserDTO user, Set<OrderItemResponseDTO> orderItems, String deliveryAddress, Double total) {
        Locale locale = Locale.forLanguageTag("fr");
        Context context = new Context(locale);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        context.setVariable("items", orderItems);
        context.setVariable("nom", String.format("%s %s", user.getFirstName(), user.getLastName()));
        context.setVariable("total", total);
        context.setVariable("deliveryAddress", deliveryAddress);


        String content = templateEngine.process("mail/deliveredOrderCustomerEmail", context);
        sendEmail(new String[]{user.getEmail()},"[SENCOMMERCE] - NOTIFICATION LIVRAISON COMMANDE", content, false, true);
    }

    @Async
    public void sendCancelledCommandeEmailToCustomer(UserDTO user, Set<OrderItemResponseDTO> orderItems, Double total) {
        Locale locale = Locale.forLanguageTag("fr");
        Context context = new Context(locale);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        context.setVariable("items", orderItems);
        context.setVariable("nom", String.format("%s %s", user.getFirstName(), user.getLastName()));
        context.setVariable("total", total);


        String content = templateEngine.process("mail/cancelledOrderCustomerEmail", context);
        sendEmail(new String[]{user.getEmail()},"[SENCOMMERCE] - NOTIFICATION ANNULATION COMMANDE", content, false, true);
    }





}
