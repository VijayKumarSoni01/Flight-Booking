package com.project.usermanagment.service.UserService.Verification;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.project.usermanagment.config.bravoprop.MailProperty;
import com.project.usermanagment.entity.User;
import com.project.usermanagment.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailVerificationService {
    public void sendResetPasswordEmail(String email, String token) {
        System.out.println("RESET LINK GENERATED:");
        System.out.println("http://localhost:3000/reset-password?token=" + token);
    }

    @Value("${spring.mail.username}")
    private String smtpUser;

    @Value("${spring.mail.host}")
    private String smtpHost;

    @PostConstruct
    public void testMailConfig() {
        System.out.println("SMTP HOST = " + smtpHost);
        System.out.println("SMTP USER = " + smtpUser);
    }

    private final JavaMailSender mailSender;
    private final MailProperty mailProperty;
    private final UserRepository userRepository;

    public void sendVerificationEmail(User user) {

        String token = UUID.randomUUID().toString();
        String fullUrl = mailProperty.buildVerificationUrl(token);

        user.setEmailVerificationToken(token);
        user.setEmailVerificationTokenExpiry(
                LocalDateTime.now().plusMinutes(mailProperty.getVerificationExpiryMinutes()));

        userRepository.save(user);

        sentHtmlEmail(
                user.getEmail(),
                "Verify your Email - " + mailProperty.getFromName(),
                buildHtml(user.getFirstName(), fullUrl));

        log.info("Verification email sent to {} expires in {} minutes", user.getEmail(),
                mailProperty.getVerificationExpiryMinutes());
    }

    public void verifyEmail(String token) {

        User user = userRepository.findByEmailVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification token"));

        if (user.isEmailVerified()) {
            throw new IllegalStateException("Email already verified");
        }

        if (user.getEmailVerificationTokenExpiry() == null ||
                user.getEmailVerificationTokenExpiry().isBefore(LocalDateTime.now())) {

            throw new IllegalStateException(
                    "Link expired. Please request a new one.");
        }

        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);
        user.setEmailVerificationTokenExpiry(null);

        userRepository.save(user);

        log.info("Email verified for {}", user.getEmail());
    }

    public void resendVerificationEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.isEmailVerified())
            throw new IllegalStateException("Email is already verified");

        sendVerificationEmail(user);
    }

    private void sentHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(mailProperty.getFromEmail(), mailProperty.getFromName());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace(); // IMPORTANT
            throw new RuntimeException(e);
        }
    }

    private String buildHtml(String firstName, String verifyUrl) {
        return """
                <div style="font-family:Arial,sans-serif;max-width:540px;margin:auto;
                            padding:32px;border:1px solid #e5e7eb;border-radius:10px;">

                    <h2 style="color:#1e40af;margin-bottom:4px;">Hi %s!</h2>

                    <p style="color:#374151;font-size:15px;line-height:1.7;margin-top:4px;">
                        Thanks for signing up with <strong>%s</strong>.
                        Please verify your email address to activate your account.
                        This link expires in <strong>%d minutes</strong>.
                    </p>

                    <div style="text-align:center;margin:36px 0;">
                        <a href="%s"
                        style="background:#2563eb;color:#ffffff;padding:14px 36px;
                                border-radius:6px;text-decoration:none;font-size:15px;
                                font-weight:600;display:inline-block;">
                            Verify Email Address
                        </a>
                    </div>

                    <p style="color:#6b7280;font-size:13px;line-height:1.6;">
                        If the button does not work, paste this link into your browser:<br/>
                        <a href="%s" style="color:#2563eb;word-break:break-all;">%s</a>
                    </p>

                    <hr style="border:none;border-top:1px solid #e5e7eb;margin:28px 0;">

                    <p style="color:#9ca3af;font-size:12px;text-align:center;">
                        If you did not create this account, ignore this email.<br/>
                        &copy; %s
                    </p>
                </div>
                """.formatted(
                firstName,
                mailProperty.getFromName(),
                mailProperty.getVerificationExpiryMinutes(),
                verifyUrl,
                verifyUrl,
                verifyUrl,
                mailProperty.getFromName());
    }


//-------------------------------------Below is used for password reset otp--------------------------
public void sendOtpEmail(String email, String otp) {

    String html = """
            <div style="font-family:Arial,sans-serif;
                        max-width:500px;
                        margin:auto;
                        padding:20px;">

                <h2>Flight Booking OTP</h2>

                <p>Your One Time Password is:</p>

                <h1 style="
                        color:#2563eb;
                        letter-spacing:5px;
                        text-align:center;">
                    %s
                </h1>

                <p>
                    This OTP is valid for
                    <strong>10 minutes</strong>.
                </p>

                <p>
                    Do not share this OTP with anyone.
                </p>

            </div>
            """.formatted(otp);

    sentHtmlEmail(
            email,
            "Flight Booking Password Reset OTP",
            html);
}
}
