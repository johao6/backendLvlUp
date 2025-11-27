package com.levelup.backend.services;

import com.levelup.backend.exceptions.ResourceNotFoundException;
import com.levelup.backend.models.Contacto;
import com.levelup.backend.repositories.ContactoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactoService {
    
    private final ContactoRepository contactoRepository;
    
    public List<Contacto> getAllContactos() {
        return contactoRepository.findAll();
    }
    
    public List<Contacto> getContactosNoLeidos() {
        return contactoRepository.findByLeidoFalse();
    }
    
    public Contacto getContactoById(Long id) {
        return contactoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contacto", "id", id));
    }
    
    @Transactional
    public Contacto createContacto(Contacto contacto) {
        contacto.setLeido(false);
        return contactoRepository.save(contacto);
    }
    
    @Transactional
    public Contacto marcarComoLeido(Long id) {
        Contacto contacto = getContactoById(id);
        contacto.setLeido(true);
        return contactoRepository.save(contacto);
    }
}
