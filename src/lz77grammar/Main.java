package lz77grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

	static Converter converter;
	static TreePrinter treeprinter;
	static Signature signature;

	public static void main(String[] args) throws Exception {

		converter = new Converter();

		treeprinter = new TreePrinter();

		switch (args[1]) {
		case "c":
			compressAndBuild(args[0]);
			break;
		case "p":
			parseAndBuild(args[0]);
			break;
		case "s":
			createSignature(args[0]);
			break;
		default:
			System.out.println("Please enter h for details");
		}

	}

	public static void createSignature(String file) throws Exception {
		Signature signature = new Signature(file);
		//List<Pair<Character, Integer>> list = signature.group();
		//signature.printPairs(list);
		List<Integer> list = signature.g();
		for(Integer i : list) {
			System.out.println(i);
		}
	}
	public static void compressAndBuild(String file) throws Exception {
		Node node = null;

		LZ77 lz77 = new LZ77(32800, 250);
		ArrayList<Reference> list = lz77.compress(file);
		System.out.println("String to factorise: ");
		lz77.decompress(list);
		String[] result = lz77.getTuples(list);

		System.out.println("Computing grammar...");
		node = converter.create(result[0]);
		for (int i = 1; i < result.length; i++) {
			node = converter.concatenate(node, converter.create(result[i]));
		}
		node.setName("S");
		
		System.out.println("Parse tree: ");
		treeprinter.print(node);

		System.out.println("Balance = " + node.getBalance());
		System.out.println("Height = " + node.getHeight());

		System.out.println("Production Rules: ");
		Set<String> set = node.addChildren(new HashSet<String>());
		for (String s : set) {
			converter.getNodes().get(s).printProduction();
		}
		System.out.println("LZ77 factors: " + result.length);
		for (String s : result) {
			System.out.println("- " + s);
		}
	}

	public static void parseAndBuild(String file) {
		CFG cfg = new CFG(file);
		System.out.println("Is in CNF: " + cfg.isCNF());
		System.out.println("Evaluation: " + cfg.evaluate());
		System.out.println("Is cyclic: " + cfg.isCyclic());
		Node node = cfg.toTree();
		treeprinter.print(node);
		converter.gFactors(node, new HashMap<String, Integer>());
	}

	public static Node generateTree() {
		Node nodex2 = new Terminal("X2", 'a');
		Node nodex1 = new Terminal("X1", 'b');

		Node nodex3 = new Branch("X3", nodex2, nodex1);
		Node nodex4 = new Branch("X4", nodex3, nodex2);
		Node nodex5 = new Branch("X5", nodex4, nodex3);
		Node nodex6 = new Branch("X6", nodex5, nodex4);
		Node nodex7 = new Branch("X7", nodex6, nodex5);
		return nodex7;
	}
}
/*
 * String word = "testing"; converter.create("testing"); converter.create("a");
 * for(int i = 0; i < 20; i++) {
 * converter.concatenate(converter.getNodes().get(Character.toString((char) i)),
 * converter.getNodes().get(word)); word = Character.toString((char) i) + word;
 * } System.out.println(converter.getNodes().get(word).getBalance());
 * System.out.println(word);
 * System.out.println(converter.getNodes().get(word).evaluate());
 * System.out.println(word.equals(converter.getNodes().get(word))); TreePrinter
 * treeprinter = new TreePrinter();
 * treeprinter.print(converter.getNodes().get(word));
 * 
 * converter = new Converter(); String word2 = "testing";
 * converter.create("testing"); converter.create("a"); for(int i = 0; i < 20;
 * i++) { converter.concatenate(converter.getNodes().get(word2),
 * converter.getNodes().get(Character.toString((char) i))); word2 = word2 +
 * Character.toString((char) i); }
 * System.out.println(converter.getNodes().get(word2).getBalance());
 * System.out.println(converter.getNodes().get(word2).evaluate());
 */
/*
 * Node newNode = converter.concatenate(converter.getNodes().get("TEST"),
 * converter.getNodes().get("TEST"));
 * converter.getNodes().put(newNode.evaluate(), newNode);
 * System.out.println(converter.getNodes().get("TESTTEST").getHeight()); Node
 * bigNode = converter.concatenate(converter.getNodes().get("TESTTEST"),
 * converter.getNodes().get("B")); converter.getNodes().put(bigNode.evaluate(),
 * bigNode);
 * System.out.println(converter.concatenate(converter.getNodes().get("TESTTESTB"
 * ), converter.getNodes().get("S")).evaluate());
 * System.out.println(converter.concatenate(converter.getNodes().get("S"),
 * converter.getNodes().get("TESTTESTB")).evaluate());
 * System.out.println(converter.getNodes().get("TESTTEST").evaluate());
 * System.out.println(converter.getNodes().get("TESTTESTB").getBalance());
 * 
 * 
 * for(List<String> list : converter.recursive("hello")) { for(String s : list)
 * { System.out.print(s); System.out.print(" "); } System.out.println(""); } }
 */