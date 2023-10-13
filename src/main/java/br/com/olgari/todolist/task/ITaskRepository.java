package br.com.olgari.todolist.task;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {

  Optional<TaskModel> findByTitleAndIdUser(String title, UUID idUser);

  List<TaskModel> findByIdUser(UUID idUser);

  Optional<TaskModel> findByIdAndIdUser(UUID id, UUID idUser);

}
