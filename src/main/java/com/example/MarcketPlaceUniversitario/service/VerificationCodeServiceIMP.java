// ===============================
// SERVICE IMP
// ===============================

package com.example.MarcketPlaceUniversitario.service;

import com.example.MarcketPlaceUniversitario.DTO.DtoExtra.SendCodeRequestDTO;
import com.example.MarcketPlaceUniversitario.DTO.DtoExtra.VerifyCodeRequestDTO;
import com.example.MarcketPlaceUniversitario.model.VerificationCode;
import com.example.MarcketPlaceUniversitario.repository.UsuarioRepository;
import com.example.MarcketPlaceUniversitario.repository.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VerificationCodeServiceIMP
        implements VerificationCodeService {

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public String sendCode(SendCodeRequestDTO dto) {

        String correo = dto.getCorreo().toLowerCase();

        // validar correo repetido
        if (usuarioRepository.existsByCorreo(correo)) {
            throw new RuntimeException(
                    "El correo ya está registrado"
            );
        }

        // generar código de 6 dígitos
        String codigo = String.valueOf(
                (int)(Math.random() * 900000) + 100000
        );

        VerificationCode verificationCode =
                new VerificationCode();

        verificationCode.setCorreo(correo);

        verificationCode.setCodigo(codigo);

        verificationCode.setFechaExpiracion(
                LocalDateTime.now().plusMinutes(5)
        );

        verificationCode.setUsado(false);

        verificationCodeRepository.save(verificationCode);

        // enviar email
        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(correo);

        message.setSubject(
                "Código de verificación"
        );

        message.setText(
                "Tu código de verificación es: "
                        + codigo
        );

        mailSender.send(message);

        return "Código enviado correctamente";
    }

    @Override
    public String verifyCode(VerifyCodeRequestDTO dto) {

        Optional<VerificationCode> optionalCode =
                verificationCodeRepository
                        .findByCorreoAndCodigo(
                                dto.getCorreo().toLowerCase(),
                                dto.getCodigo()
                        );

        if (optionalCode.isEmpty()) {
            throw new RuntimeException(
                    "Código inválido"
            );
        }

        VerificationCode verificationCode =
                optionalCode.get();

        // verificar expiración
        if (verificationCode.getFechaExpiracion()
                .isBefore(LocalDateTime.now())) {

            throw new RuntimeException(
                    "El código expiró"
            );
        }

        // verificar si ya fue usado
        if (verificationCode.getUsado()) {

            throw new RuntimeException(
                    "El código ya fue utilizado"
            );
        }

        return "Código verificado correctamente";
    }
}