package secret;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import secret.repositories.MessageRepository;
import secret.repositories.SecretRepository;
import secret.struct.EmailBox;
import secret.struct.Message;
import secret.struct.CheckInfo;
import secret.struct.Secret;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.net.URI;
import java.util.Arrays;
import java.util.Date;

@RestController
@RequestMapping(path = "/message", produces = "application/json") //Обрабатывает запросы на /design
@CrossOrigin(origins = "*") //Позволяет перекрестные запросы
public class MessageController {

    private SecretRepository secretRepository;
    private MessageRepository messageRepository;
    private JavaMailSender emailSender;
    private TemplateEngine templateEngine;
    private final int logRounds = 12;

    @Autowired
    public MessageController(SecretRepository secretRepository, MessageRepository messageRepository, @Qualifier("getJavaMailSender") JavaMailSender emailSender, TemplateEngine templateEngine) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
        this.secretRepository = secretRepository;
        this.messageRepository = messageRepository;
    }

    boolean normalDifferenceBtwTwoDates(Date create, String timeLong) {
        Date now = new Date();
        long milliseconds = now.getTime()-create.getTime();
        long minutes = milliseconds / (1000 * 60);
        switch (timeLong) {
            case "15 минут":
                if (minutes > 15) return false;
                break;
            case "30 минут":
                if (minutes > 30) return false;
                break;
            case "1 час":
                if (minutes > 60) return false;
                break;
            case "3 часа":
                if (minutes > 180) return false;
                break;
            case "12 часов":
                if (minutes > 720) return false;
                break;
            case "1 день":
                if (minutes > 1440) return false;
                break;
            case "7 дней":
                if (minutes > 10080) return false;
                break;
        }
        return true;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Secret> getSecret(@PathVariable("id") String id) {
        Secret secret = secretRepository.getSecretByHash(id);
        if (secret == null) {
            secret = new Secret();
            Message mes = new Message("Sorry but this secret not exist");
            secret.setMessage(mes);
            return new ResponseEntity<>(secret, HttpStatus.ALREADY_REPORTED);
        }
        if (!secret.isShow()) {
            if (!normalDifferenceBtwTwoDates(secret.getCreatedAt(), secret.getLifetime())) {
                secret.getMessage().setMessage("Lifetime of this secret end");
                return new ResponseEntity<>(secret, HttpStatus.IM_USED);
            }
            if (!secret.isForPrivateComp()) {
                secret.setForPrivateComp(true);
                secretRepository.save(secret);
                secret = secretRepository.getSecretById(secret.getId());
            } else {
                if (secret.getPassword().length() != 0) {
                    secret.getMessage().setMessage("Need a pass for this secret");
                } else {
                    secret.setShow(true);
                    secretRepository.save(secret);
                    secret = secretRepository.getSecretById(secret.getId());
                }
            }

            return new ResponseEntity<>(secret, HttpStatus.OK);
        } else {
            secret.getMessage().setMessage("This secret already read");
            return new ResponseEntity<>(secret, HttpStatus.IM_USED);
        }
    }

    @Transactional
    @DeleteMapping(path = "/{id}")
    public void deleteSecret(@PathVariable("id") String id) {
        secretRepository.deleteSecretByHash(id);//добавить удаление из базы данных сообщений
    }

    @PostMapping
    public URI addMessage(@RequestBody Secret secret) {
        System.out.println(secret);
        Message mes = new Message(secret.getMessage().getMessage());//проверить добавляется ли message в базу без создания объекта, просто при сохранении secret в базу, так как если пустое, то nullpointersxeption
        secret.setMessage(messageRepository.save(mes));
        long id = secretRepository.save(secret).getId();
        String idHash = BCrypt.hashpw(String.valueOf(id), BCrypt.gensalt(logRounds)).replaceAll("/", "");

        if (secret.getPassword().length() != 0) {
            String passHash = BCrypt.hashpw(String.valueOf(secret.getPassword()), BCrypt.gensalt(logRounds));
            secret.setPassword(passHash);
        }

        secret.setHash(idHash);
        secretRepository.save(secret);
        ControllerLinkBuilder link = ControllerLinkBuilder.linkTo(MessageController.class).slash((idHash));
        System.out.println(link.toString());
        return link.toUri();
    }

    @PostMapping(path = "/checkPassword/{id}")
    public CheckInfo checkPassword(@PathVariable("id") String id, @RequestBody CheckInfo pass) {
        System.out.println(pass);
        Secret secret = secretRepository.getSecretByHash(id);

        if (BCrypt.checkpw(pass.getPassword(), secret.getPassword())) {
            pass.setCheck(true);
            secret.setShow(true);
            secretRepository.save(secret);
            pass.setMessage(secret.getMessage().getMessage());
            System.out.println("yes");
            return pass;
        } else {
            pass.setCheck(false);
            System.out.println("no");
            return pass;
        }
    }


    @PostMapping(path = "/sendEmail")
    public void sendEmail(@RequestBody EmailBox emailBox) throws MessagingException {
       /* SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailBox.getEmail());
        message.setSubject("SecretRoom");
        message.setText(emailBox.getUrl());
        emailSender.send(message);*/
        final Context ctx = new Context();
        ctx.setVariable("link", emailBox.getUrl());

        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.emailSender.createMimeMessage();
        final MimeMessageHelper message =
                new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
        message.setSubject("SecretRoom");
        message.setTo(emailBox.getEmail());

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.templateEngine.process("emailTemplate.html", ctx);
        message.setText(htmlContent, true); // true = isHtml

        this.emailSender.send(mimeMessage);
    }
}
