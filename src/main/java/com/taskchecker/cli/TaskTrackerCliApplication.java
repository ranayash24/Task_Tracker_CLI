package com.taskchecker.cli;

import com.taskchecker.cli.io.TaskStore;
import com.taskchecker.cli.model.Task;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class TaskTrackerCliApplication implements org.springframework.boot.CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(TaskTrackerCliApplication.class, args);
	}
	private final com.taskchecker.cli.io.TaskStore store = new com.taskchecker.cli.io.TaskStore();
	private final com.taskchecker.cli.service.TaskService service = new com.taskchecker.cli.service.TaskService(store);
	@Override
	public void run(String... args) throws Exception {
		if (args == null || args.length == 0) {
			printHelp();
			return;
		}
		String command = args[0];
		switch (command) {
			case "help" -> printHelp();

			case "add" -> {
				if (args.length < 2) {
					System.out.println("Error: 'add' needs a description.");
					return;
				}
				StringBuilder sb = new StringBuilder();
				for (int i = 1 ; i<args.length;i++){
					if (i>1) sb.append(' ');
					sb.append(args[i]);
				}
				String desc = sb.toString().trim();
				try {
					var t = service.add(desc);
					System.out.println("Task added successfully (ID: "+t.getId()+")");
				}
				catch (IllegalArgumentException e){
					System.out.println(e.getMessage());
				}
			}

			case "list" -> {
				try {
					if (args.length ==1){
						var tasks = service.listAll();
						printTasks(tasks);
					}
					else {
						var tasks = service.listByStatus(args[1]);
						printTasks(tasks);
					}

				}
				catch (IllegalArgumentException e){
					System.out.println(e.getMessage());
				}
			}

			case "delete" -> {
				if (args.length < 2) {
					System.out.println("Error: 'delete' needs <id>");
					return;
				}

				int id;
				try {
					id = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					System.out.println("Invalid ID: " + args[1]);
					return;
				}

				try {
					service.delete(id);
					System.out.println("Task deleted successfully (ID: " + id + ")");
				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
				}
			}

			case "mark-done" -> {
				if (args.length < 2) {
					System.out.println("Error: 'mark-done' needs an ID.");
					return;
				}
				int id;
				try {
					id = Integer.parseInt(args[1]);
				}
				catch (NumberFormatException e){
					System.out.println("Invalid Id:"+ args[1]);
					return;
				}
				try {
					var t = service.markDone(id);
					System.out.println("Task marked done (ID:"+ t.getId()+")");
				}catch (IllegalArgumentException e){
					System.out.println(e.getMessage());
				}
			}

			case "mark-in-progress" -> {
				if (args.length < 2) {
					System.out.println("Error: 'mark-in-progress' needs an ID.");
					return;
				}
				int id;
				try{
					id = Integer.parseInt(args[1]);
				}catch (NumberFormatException e){
					System.out.println("Invalid id : "+ args[1]);
					return;
				}
				try{
					var t = service.markInProgress(id);
					System.out.println("Task mark in progress (ID:"+ id+")");
				}catch (IllegalArgumentException e){
					System.out.println(e.getMessage());
				}
			}
			case "store-self-test" -> {
				var store = new TaskStore();
				var tasks = store.loadAll();
				System.out.println("Loaded " + tasks.size() + " tasks");
				store.saveAll(tasks);
				System.out.println("Store OK");
			}

			case "update" -> {
				if (args.length <3){
					System.out.println("Error: 'update' needs <id> and new description");
					return;
				}
				int id;
				try{
					id = Integer.parseInt(args[1]);
				}
				catch (NumberFormatException e){
					System.out.println("Invalid ID:"+ args[1]);
					return;
				}
				StringBuilder sb = new StringBuilder();
				for (int i = 2; i < args.length; i++) {
					if (i > 2) sb.append(" ");
					sb.append(args[i]);
				}
				String desc = sb.toString().trim();

				// call the service
				try {
					var t = service.update(id, desc);
					System.out.println("Task updated successfully (ID: " + t.getId() + ")");
				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
				}
			}
			default -> {
				System.out.println("Unknown command: " + command);
				printHelp();
			}
		}
	}

	private void printTasks(List<Task> tasks) {
		if (tasks.isEmpty()){
			System.out.println("No tasks found");
			return;
		}
		for (Task t: tasks){
			System.out.println("["+ t.getId()+"] ("+t.getStatus()+")"+ t.getDescription());
			System.out.println("  createdAt: " + t.getCreatedAt());
			System.out.println("  updatedAt: " + t.getUpdatedAt());
			System.out.println();
		}

	}

	private void printHelp() {
		System.out.println("Usage: task-cli <command> [args]");
		System.out.println("Commands:");
		System.out.println("  add \"description\"");
		System.out.println("  update <id> \"new description\"");
		System.out.println("  delete <id>");
		System.out.println("  mark-in-progress <id>");
		System.out.println("  mark-done <id>");
		System.out.println("  list [todo|in-progress|done]");
		System.out.println("  help");
	}

}
