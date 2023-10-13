package br.com.olgari.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.olgari.todolist.errors.RequestErrorException;
import br.com.olgari.todolist.utils.Utils;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

  private ITaskRepository taskRepository;

  @PostMapping
  public ResponseEntity<TaskModel> create(@RequestBody TaskModel task, @RequestAttribute("userId") UUID userId)
      throws RequestErrorException {
    Optional<TaskModel> findByTitle = taskRepository.findByTitleAndIdUser(task.getTitle(), userId);
    if (findByTitle.isPresent()) {
      throw new RequestErrorException("Já existe essa tarefa para esse usuário.");
    }
    task.setIdUser(userId);
    LocalDateTime currentDate = LocalDateTime.now();

    if (currentDate.isAfter(task.getStartAt()) || currentDate.isAfter(task.getEndAt())) {

      throw new RequestErrorException("A data de inicio/final deve ser maior que a data atual");
    }
    return ResponseEntity.ok(taskRepository.save(task));
  }

  @GetMapping
  public List<TaskModel> list(@RequestAttribute("userId") UUID userId) {
    return this.taskRepository.findByIdUser(userId);
  }

  @PutMapping("/{id}")
  public ResponseEntity<TaskModel> update(@RequestBody TaskModel taskUpdate, @PathVariable UUID id,
      @RequestAttribute UUID userId) throws RequestErrorException {

    TaskModel findTask = this.taskRepository.findByIdAndIdUser(id, userId)
        .orElseThrow(() -> new RequestErrorException("Tarefa não localizada"));

    Utils.copyNonNullProperties(taskUpdate, findTask);

    return ResponseEntity.ok(this.taskRepository.save(findTask));
  }
}
