package org.yenbo.commonDemo;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Sample codes from "Java in a Nutshell".
 *
 */
public class LambdaExpDemo {

	public static void main(String[] args) {
		
		listByFilter();
		filterAndCollect();
		mapToStringLength();
		printByForEach();
		sumByReduce();
//		flatternStreams();
		filterByRegex();
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
	
	private static void mapToStringLength() {
		
		System.out.println("-------- mapToStringLength() --------");
		
		String[] input = {"tiger", "cat", "TIGER", "Tiger", "leopard"};
		List<String> cats = Arrays.asList(input);
		
		List<Integer> namesLength = cats.stream()
				.map(String::length)
				.collect(Collectors.toList());
		
		System.out.println(namesLength);
	}
	
	private static void printByForEach() {
		
		System.out.println("-------- printByForEach() --------");
		
		String[] input = {"tiger", "cat", "TIGER", "Tiger", "leopard"};
		List<String> cats = Arrays.asList(input);
		
		System.out.println("** Print by bound method reference: **");
		cats.stream().forEach(System.out::println);
		
		System.out.println("** Print by anonymous method: **");
		cats.stream().forEach(s -> System.out.println(s));
	}
	
	private static void sumByReduce() {
		
		System.out.println("-------- sumByReduce() --------");
		
		double sumPrimes = (double) Stream.of(2, 3, 5, 7, 11, 13, 17, 19, 23)
				.reduce(0,
						(x, y) -> {
							return x + y;
						}
					);
		System.out.println("Sum of some primes: " + sumPrimes);
	}
	
//	private static void flatternStreams() {
//		
//		System.out.println("-------- flatternStreams() --------");
//		
//		String[] billyQuotes = {
//				"For Brutus is an honourable man",
//				"Give me your hands if we be friends and Robin shall restore amends",
//				"Misery acquaints a man with strange bedfellows"
//		};
//		List<String> quotes = Arrays.asList(billyQuotes);
//		
//		// FIXME: Type mismatch
//		List<String> words = quotes.stream()
//				.flatMap(line -> Stream.of(line.split(" +")))
//				.collect(Collectors.toList());
//		
//		long wordCount = words.size();
//		
//		double avg = (double) words.stream()
//				.map(String::length)
//				.reduce(0, (x, y) -> {
//					return x + y;
//					}
//				) / wordCount;
//		
//		System.out.println("Avg. word length: " + avg);
//	}
	
	// TODO Collection::parallelStream
	// TODO Collection::spliterator
	// TODO Collection::removeIf
	// TODO Map::forEach
	// TODO Map:: computeIfAbsent
	// TODO Map::computeIfPresent
	// TODO Map::compute
	// TODO Map::merge
	// TODO Map::remove
	// TODO Map::replace
	// TODO Map::putIfAbsent
	// TODO Map::getOrDefault
	// TODO List::sort
	
	private static void filterByRegex() {
		
		System.out.println("-------- filterByRegex() --------");
		
		Pattern pattern = Pattern.compile("\\d");
		
		String[] inputs = {"cat", "dog", "ice-9", "99 ranch"};
		List<String> list = Arrays.asList(inputs);
		
		List<String> containDigits = list.stream()
				.filter(pattern.asPredicate())
				.collect(Collectors.toList());
		
		System.out.println(containDigits);
	}
}
