package com.example.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.*;
import java.util.List;

@SpringBootApplication
public class TodoApplication {
    public static void main(String[] args) {
        SpringApplication.run(TodoApplication.class, args);
    }
}

// --- DATABASE ENTITY ---
@Entity
@Table(name = "todos")
class TodoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private boolean completed;

    // Constructors
    public TodoItem() {}
    public TodoItem(String title, boolean completed) {
        this.title = title;
        this.completed = completed;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}

// --- REPOSITORY LAYER ---
interface TodoRepository extends JpaRepository<TodoItem, Long> {}

// --- REST API CONTROLLER ---
@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*")
class TodoController {

    private final TodoRepository repository;

    public TodoController(TodoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<TodoItem> getAllTodos() {
        return repository.findAll();
    }

    @PostMapping
    public TodoItem createTodo(@RequestBody TodoItem todo) {
        return repository.save(todo);
    }

    @PutMapping("/{id}")
    public TodoItem updateTodo(@PathVariable Long id, @RequestBody TodoItem updatedTodo) {
        return repository.findById(id)
                .map(todo -> {
                    todo.setTitle(updatedTodo.getTitle());
                    todo.setCompleted(updatedTodo.isCompleted());
                    return repository.save(todo);
                })
                .orElseGet(() -> {
                    updatedTodo.setId(id);
                    return repository.save(updatedTodo);
                });
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable Long id) {
        repository.deleteById(id);
    }
}