package com.taskchecker.cli.service;
import com.taskchecker.cli.io.TaskStore;
import com.taskchecker.cli.model.Task;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
public class TaskService {
    private  final  TaskStore store;

    public TaskService(TaskStore store) {
        this.store = store;
    }
    private String nowIso(){
        return Instant.now().toString();
    }

    public Task add(String description){
        if(description == null || description.trim().isEmpty()){
            throw new IllegalArgumentException("Description cannot be empty");
        }
        List<Task> tasks = store.loadAll();
        int nextId =1;
        for (Task t : tasks){
            if(t.getId() >= nextId){
                nextId = t.getId()+1;
            }
        }
        String now = nowIso();
        Task newTask = new Task(nextId,description.trim(),"todo",now,now);
        tasks.add(newTask);
        store.saveAll(tasks);
        return newTask;
    }
    public List<Task> listAll(){
        return  store.loadAll();
    }
    public List<Task> listByStatus(String status){
        if(status == null) return listAll();

        status = status.toLowerCase();
        if(!status.equals("todo") && !status.equals("in-progress") && !status.equals("done")){
            throw new IllegalArgumentException("Invalid status" + status);
        }
        List<Task> all = store.loadAll();
        List<Task> filtered = new ArrayList<>();

        for (Task t : all){
            if(t.getStatus().equalsIgnoreCase(status)){
                filtered.add(t);
            }
        }
        return  filtered;
    }

    public Task update(int id, String description){
        if (id <= 0) {
            throw new IllegalArgumentException("Error : 'udapte' needs <id> and \"new description\"");
        } else if (description== null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        List<Task> tasks = store.loadAll();
        Task found = null;
        for (Task t: tasks ){
            if (t.getId()== id){
                t.setDescription(description.trim());
                t.setUpdatedAt(java.time.Instant.now().toString());
                found = t;
                break;
            }
        }
        if (found == null){
            throw new IllegalArgumentException("Task not found (ID: "+ id+ ")");
        }
        store.saveAll(tasks);
        return found;
    }
    private Task changeStatus(int id, String newStatus){
        if(id<=0) throw new IllegalArgumentException("Invaild ID:"+ id);
        List<Task> tasks = store.loadAll();
        Task found = null;
        for (Task t: tasks){
            if (t.getId() == id){
                t.setStatus(newStatus);
                t.setUpdatedAt(nowIso());
                found = t;
                break;
            }
        }
        if (found == null) throw new IllegalArgumentException("Task not found (ID:"+id+")");
        store.saveAll(tasks);
        return found;
    }
    public Task markDone(int id) {
        return changeStatus(id,"done");
    }
    public Task markInProgress(int id ){
        return  changeStatus(id,"in progress");
    }

    public void delete(int id){
        if (id<=0){
            throw new IllegalArgumentException("Invalid Id:"+ id);
        }
        List<Task> tasks = store.loadAll();
        Task toRemove = null;
        for (Task t: tasks){
            if (t.getId() == id){
                toRemove  = t;
                break;
            }
        }
        if (toRemove == null) throw new IllegalArgumentException("Task not foudn at ID: "+ id );
        tasks.remove(toRemove);
        store.saveAll(tasks);
    }
}
