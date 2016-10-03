package org.yenbo.commonDemo;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LambdaExpDemo {

	public static void main(String[] args) {
		
		listByFilter();
		filterAndCollect();
	}
	
	private static void listByFilter() {
		
		System.out.println("-------- listByFilter() --------");
		
		String path = Paths.get("").toAbsolutePath().toString() +
				"/src/main/java/org/yenbo/commonDemo";
		
		File dir = new File(path);
		
		// FilenameFilter version
		System.out.println("** FilenameFilter version **");
		
		String[] filelistByFilenameFilter = dir.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".java");
			}
		});
		
		for (String s: filelistByFilenameFilter) {
			System.out.println(s);
		}
		
		// Lambda expression version
		System.out.println("** Lambda expression version **");
		
		String[] filelistByLambda = dir.list(
				(f, s) -> {
					return s.endsWith(".java");
				}
			);
		
		for (String s: filelistByLambda) {
			System.out.println(s);
		}
	}
	
	private static void filterAndCollect() {
		
		System.out.println("-------- filterAndCollect() --------");
		
		String search = "tiger";
		String[] input = {"tiger", "cat", "TIGER", "Tiger", "leopard"};
		
		List<String> cats = Arrays.asList(input);
		
		String tigers = cats.stream()
				.filter(s -> s.equalsIgnoreCase(search))
				.collect(Collectors.joining(", "));
		
		System.out.println(tigers);
		
		// or
		Predicate<String> predicate = s -> s.equalsIgnoreCase(search); // must be explicitly created
		Predicate<String> combined = predicate.or(s -> s.equals("leopard"));
		
		System.out.println(cats.stream().filter(combined).collect(Collectors.joining(", ")));
	}
}
