package com.levelup.backend.services;

import com.levelup.backend.exceptions.ResourceNotFoundException;
import com.levelup.backend.models.Blog;
import com.levelup.backend.repositories.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {
    
    private final BlogRepository blogRepository;
    
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }
    
    public List<Blog> getPublishedBlogs() {
        return blogRepository.findByPublicadoTrue();
    }
    
    public Blog getBlogById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog", "id", id));
    }
    
    @Transactional
    public Blog createBlog(Blog blog) {
        return blogRepository.save(blog);
    }
    
    @Transactional
    public Blog updateBlog(Long id, Blog blogDetails) {
        Blog blog = getBlogById(id);
        
        blog.setTitulo(blogDetails.getTitulo());
        blog.setContenido(blogDetails.getContenido());
        blog.setResumen(blogDetails.getResumen());
        blog.setImagen(blogDetails.getImagen());
        blog.setAutor(blogDetails.getAutor());
        blog.setPublicado(blogDetails.getPublicado());
        
        return blogRepository.save(blog);
    }
    
    @Transactional
    public void deleteBlog(Long id) {
        Blog blog = getBlogById(id);
        blogRepository.delete(blog);
    }
}
