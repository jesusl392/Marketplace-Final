package com.example.MarcketPlaceUniversitario.controller;

import com.example.MarcketPlaceUniversitario.DTO.MessageRequestDTO;
import com.example.MarcketPlaceUniversitario.DTO.MessageResponseDTO;
import com.example.MarcketPlaceUniversitario.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Mensaje")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public MessageResponseDTO guardarMessage(@Valid @RequestBody MessageRequestDTO dto) {
        return messageService.guardar(dto);
    }

    @GetMapping("/{id}")
    public MessageResponseDTO buscarPorId(@PathVariable long id) {
        return messageService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public MessageResponseDTO actualizar(@Valid @RequestBody MessageRequestDTO dto, @PathVariable long id) {
        return messageService.actualizar(dto, id);
    }

    @GetMapping
    public List<MessageResponseDTO> buscarTodos() {
        return messageService.obtenerTodos();
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable long id) {
        messageService.eliminar(id);
    }

    @PatchMapping("/marcar-leidos")
    public void marcarLeidos(@RequestParam Long senderId, @RequestParam Long receiverId) {
        messageService.marcarLeidos(senderId, receiverId);
    }
}