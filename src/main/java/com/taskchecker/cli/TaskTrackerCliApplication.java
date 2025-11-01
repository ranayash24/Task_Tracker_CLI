package com.taskchecker.cli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskTrackerCliApplication implements org.springframework.boot.CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(TaskTrackerCliApplication.class, args);
	}

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
				System.out.println("Adding new task: " + args[1]);
			}

			case "list" -> System.out.println("Listing all tasks...");

			case "delete" -> {
				if (args.length < 2) {
					System.out.println("Error: 'delete' needs an ID.");
					return;
				}
				System.out.println("Deleting task with ID: " + args[1]);
			}

			case "mark-done" -> {
				if (args.length < 2) {
					System.out.println("Error: 'mark-done' needs an ID.");
					return;
				}
				System.out.println("Marking task " + args[1] + " as done");
			}

			case "mark-in-progress" -> {
				if (args.length < 2) {
					System.out.println("Error: 'mark-in-progress' needs an ID.");
					return;
				}
				System.out.println("Marking task " + args[1] + " as in progress");
			}

			default -> {
				System.out.println("Unknown command: " + command);
				printHelp();
			}
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
