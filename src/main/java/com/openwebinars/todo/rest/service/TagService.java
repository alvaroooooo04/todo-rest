package com.openwebinars.todo.rest.service;

import com.openwebinars.todo.rest.dto.EditTagDto;
import com.openwebinars.todo.rest.error.TagNotFoundException;
import com.openwebinars.todo.rest.error.ValidationException;
import com.openwebinars.todo.rest.model.Tag;
import com.openwebinars.todo.rest.repos.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    public Tag findById(Long id) {
        return tagRepository.findById(id).orElseThrow(() -> new TagNotFoundException(id));
    }

    public Tag create(EditTagDto dto) {
        if (tagRepository.existsByNameIgnoreCase(dto.name()))
            throw new ValidationException("Ya existe un tag con ese nombre");
        return tagRepository.save(Tag.builder().name(dto.name()).build());
    }

    public Tag edit(Long id, EditTagDto dto) {
        Tag t = findById(id);
        t.setName(dto.name());
        return tagRepository.save(t);
    }

    public void delete(Long id) {
        if (!tagRepository.existsById(id))
            throw new TagNotFoundException(id);
        tagRepository.deleteById(id);
    }
}
