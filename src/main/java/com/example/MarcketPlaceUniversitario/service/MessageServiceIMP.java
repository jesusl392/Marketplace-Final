package com.example.MarcketPlaceUniversitario.service;

import com.example.MarcketPlaceUniversitario.DTO.MessageRequestDTO;
import com.example.MarcketPlaceUniversitario.DTO.MessageResponseDTO;
import com.example.MarcketPlaceUniversitario.model.Message;
import com.example.MarcketPlaceUniversitario.model.Usuario;
import com.example.MarcketPlaceUniversitario.repository.MessageRepository;
import com.example.MarcketPlaceUniversitario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceIMP implements MessageService {

    private final MessageRepository messageRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public MessageServiceIMP(MessageRepository messageRepository, UsuarioRepository usuarioRepository) {
        this.messageRepository = messageRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public MessageResponseDTO guardar(MessageRequestDTO dto) {
        Usuario sender = usuarioRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new RuntimeException("Remitente no encontrado"));
        Usuario receiver = usuarioRepository.findById(dto.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Destinatario no encontrado"));

        Message message = new Message();
        message.setMessage(dto.getMessage());
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setLeido(false);
        message.setDateTime(LocalDateTime.now());

        MessageResponseDTO saved = toDTO(messageRepository.save(message));

        // Broadcast en tiempo real al destinatario vía WebSocket STOMP
        messagingTemplate.convertAndSend("/topic/user/" + dto.getReceiverId(), saved);

        return saved;
    }

    @Override
    public List<MessageResponseDTO> obtenerTodos() {
        return messageRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public MessageResponseDTO buscarPorId(long id) {
        return toDTO(messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado")));
    }

    @Override
    public MessageResponseDTO actualizar(MessageRequestDTO dto, long id) {
        Message existente = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado"));
        existente.setMessage(dto.getMessage());
        return toDTO(messageRepository.save(existente));
    }

    @Override
    public void eliminar(long id) {
        messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado"));
        messageRepository.deleteById(id);
    }

    @Override
    public void marcarLeidos(Long senderId, Long receiverId) {
        messageRepository.marcarLeidos(senderId, receiverId);
    }

    private MessageResponseDTO toDTO(Message m) {
        return new MessageResponseDTO(
                m.getId(),
                m.getMessage(),
                m.getLeido(),
                m.getDateTime(),
                m.getSender() != null ? m.getSender().getId() : null,
                m.getSender() != null ? m.getSender().getNombre() : null,
                m.getReceiver() != null ? m.getReceiver().getId() : null,
                m.getReceiver() != null ? m.getReceiver().getNombre() : null
        );
    }
}