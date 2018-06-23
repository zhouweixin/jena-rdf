package com.zhou;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.apache.jena.rdf.model.Bag;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.VCARD;
import org.apache.log4j.BasicConfigurator;

public class Rdf {
	public static void main(String[] args) throws IOException {
		BasicConfigurator.configure();

		Rdf rdf1 = new Rdf();
		rdf1.fun6();
	}

	/**
	 * 创建简单的三元组
	 */
	public void fun1() {
		// 创建空的模型: 相当于关系数据库里的table
		Model model = ModelFactory.createDefaultModel();

		// 创建:主语
		Resource johnSmith = model.createResource("http://somewhere/JohnSmith");

		// 添加谓语和宾语
		johnSmith.addProperty(VCARD.FN, "John Smith");

		// 查询所有的语句: 三元组, 相当于关系数据库里的每一行的数据
		StmtIterator iter = model.listStatements();

		if (iter.hasNext()) {
			Statement stmt = iter.nextStatement();

			// 主语
			Resource subject = stmt.getSubject();
			// 谓语
			Property predicate = stmt.getPredicate();
			// 宾语
			RDFNode object = stmt.getObject();

			System.out.println("\n=====================三元组=========================");
			System.out.println("subject = " + subject.toString());
			System.out.println("predicate = " + predicate.toString());
			System.out.println("object = " + object.toString());

			System.out.println("\n=====================主语的属性=========================");
			System.out.println("subject.URI = " + subject.getURI());
			System.out.println("subject.nameSpace = " + subject.getNameSpace());
			System.out.println("subject.localName = " + subject.getLocalName());

			System.out.println("\n=====================谓语的属性=========================");
			System.out.println("predicate.URI = " + predicate.getURI());
			System.out.println("predicate.nameSpace = " + predicate.getNameSpace());
			System.out.println("predicate.localName = " + predicate.getLocalName());
		}
	}

	/**
	 * 三元组里的一对多
	 */
	public void fun2() {
		String personURI = "http://somewhere/JohnSmith";
		String givenName = "John";
		String familyName = "Smith";
		String fullName = givenName + " " + familyName;

		// 创建空的模型
		Model model = ModelFactory.createDefaultModel();

		// 用链式创建三元组
		model.createResource(personURI).addProperty(VCARD.FN, fullName).addProperty(VCARD.N,
				model.createResource().addProperty(VCARD.Given, givenName).addProperty(VCARD.Family, familyName));

		StmtIterator iter = model.listStatements();

		System.out.println("\n=====================三元组=========================");
		while (iter.hasNext()) {
			Statement stmt = iter.nextStatement();

			// 主语
			Resource subject = stmt.getSubject();
			// 谓语
			Property predicate = stmt.getPredicate();
			// 宾语
			RDFNode object = stmt.getObject();

			String result = String.format("%s %s %s .", subject.toString(), predicate.toString(), object.toString());
			System.out.println(result);
		}
	}

	/**
	 * 输出xml文件
	 * 
	 * @throws IOException
	 */
	public void fun3() throws IOException {
		String personURI = "http://somewhere/JohnSmith";
		String givenName = "John";
		String familyName = "Smith";
		String fullName = givenName + " " + familyName;

		// 创建空的模型
		Model model = ModelFactory.createDefaultModel();

		// 链式创建三元组
		model.createResource(personURI).addProperty(VCARD.FN, fullName).addProperty(VCARD.N,
				model.createResource().addProperty(VCARD.Given, givenName).addProperty(VCARD.Family, familyName));

		// 以xml的格式输出
		model.write(System.out);

		// 写入data1.rdf文件
		FileOutputStream fos = new FileOutputStream("data/data1.rdf");
		model.write(fos);
		fos.close();
	}

	/**
	 * 从文件里读入RDF
	 */
	public void fun4() {
		String inputFileName = "data/data1.rdf";

		// 创建空的模型
		Model model = ModelFactory.createDefaultModel();

		// 以输入流的方式打开文件
		InputStream in = FileManager.get().open(inputFileName);
		if (in == null) {
			throw new IllegalArgumentException("File: " + inputFileName + " not found");
		}

		// 从文件流里读入模型
		model.read(in, "");

		// 以xml的格式输出
		model.write(System.out);
	}

	/**
	 * 从文件里读入修改
	 * 
	 * @throws FileNotFoundException
	 */
	public void fun5() throws FileNotFoundException {
		String inputFileName = "data/data1.rdf";

		// 创建空模型
		Model model = ModelFactory.createDefaultModel();

		// 以输入流的方式打开文件
		InputStream in = FileManager.get().open(inputFileName);
		if (in == null) {
			throw new IllegalArgumentException("File: " + inputFileName + " not found");
		}

		// 从文件里读入RDF数据
		model.read(new InputStreamReader(in), "");

		// 通过URI获取资源
		Resource johnSmith = model.getResource("http://somewhere/JohnSmith");

		// 获取johnSmith的FN属性
		String fullName = johnSmith.getRequiredProperty(VCARD.FN).getString();

		// 获取johnSmith的N属性
		RDFNode name = johnSmith.getRequiredProperty(VCARD.N).getObject();
		if (name.isResource()) {
			Resource res = (Resource) name;
			RDFNode givenName = res.getRequiredProperty(VCARD.Given).getObject();
			RDFNode familyName = res.getRequiredProperty(VCARD.Family).getObject();

			System.out.println(
					String.format("fullName = %s, givenName = %s, familyName = %s", fullName, givenName, familyName));
		}

		// 为johnSmith添加两个属性
		johnSmith.addProperty(VCARD.NICKNAME, "Smithy").addProperty(VCARD.NICKNAME, "Adman");

		StmtIterator iter = johnSmith.listProperties(VCARD.NICKNAME);
		System.out.print("nickName = ");
		while (iter.hasNext()) {
			System.out.print(iter.nextStatement().getObject().toString() + ", ");
		}
		System.out.println();

		System.out.println("\n==================添加nickName后的结构=====================");
		System.out.println("\n【RDF/XML】:");
		model.write(System.out, "RDF/XML");
		model.write(new FileOutputStream("data/data2(RDF-XML).rdf"), "RDF/XML");
		
		System.out.println("\n【RDF/XML-ABBREV】:");
		model.write(System.out, "RDF/XML-ABBREV");
		model.write(new FileOutputStream("data/data2(RDF-XML-ABBREV).rdf"), "RDF/XML-ABBREV");
		
		System.out.println("\n【N-TRIPLE】:");
		model.write(System.out, "N-TRIPLE");
		model.write(new FileOutputStream("data/data2(N-TRIPLE).rdf"), "N-TRIPLE");
		
		System.out.println("\n【TURTLE】:");
		model.write(System.out, "TURTLE");
		model.write(new FileOutputStream("data/data2(TURTLE).rdf"), "TURTLE");
		
		System.out.println("\n【TTL】:");
		model.write(System.out, "TTL");
		model.write(new FileOutputStream("data/data2(TTL).rdf"), "TTL");
		
		System.out.println("\n【N3】:");
		model.write(System.out, "N3");
		model.write(new FileOutputStream("data/data2(N3).rdf"), "N3");
	}

	/**
	 * 查询与过滤
	 */
	public void fun6() {
		String inputFileName = "data/data1.rdf";

		// 创建空的模型
		Model model = ModelFactory.createDefaultModel();

		// 打开rdf文件
		InputStream in = FileManager.get().open(inputFileName);
		if (in == null) {
			throw new IllegalArgumentException("File: " + inputFileName + " not found");
		}

		// 读入内容
		model.read(in, "");

		// 1.取出有FN属性的三元组; 2.过滤以Smith结尾的三元组
		StmtIterator iter = model.listStatements(new SimpleSelector(null, VCARD.FN, (RDFNode) null) {
			@Override
			public boolean selects(Statement s) {
				return s.getString().endsWith("Smith");
			}
		});

		if (iter.hasNext()) {
			System.out.println("The database contains vcards for:");
			while (iter.hasNext()) {
				System.out.println("  " + iter.nextStatement().getString());
			}
		} else {
			System.out.println("No Smith's were found in the database");
		}
	}

	/**
	 * 合并模型
	 */
	public void fun7() {
		String inputFileName1 = "data/data1.rdf";
		String inputFileName2 = "data/data2.rdf";

		// create an empty model
		Model model1 = ModelFactory.createDefaultModel();
		Model model2 = ModelFactory.createDefaultModel();

		// use the class loader to find the input file
		InputStream in1 = FileManager.get().open(inputFileName1);
		if (in1 == null) {
			throw new IllegalArgumentException("File: " + inputFileName1 + " not found");
		}
		InputStream in2 = FileManager.get().open(inputFileName2);
		if (in2 == null) {
			throw new IllegalArgumentException("File: " + inputFileName2 + " not found");
		}

		// read the RDF/XML files
		model1.read(in1, "");
		model2.read(in2, "");

		// merge the graphs
		Model model = model1.union(model2);

		// print the graph as RDF/XML
		model.write(System.out, "RDF/XML-ABBREV");
		System.out.println();
	}

	/**
	 * 创建包
	 */
	public void fun8() {
		String inputFileName = "data/data1.rdf";

		// create an empty model
		Model model = ModelFactory.createDefaultModel();

		// use the class loader to find the input file
		InputStream in = FileManager.get().open(inputFileName);
		if (in == null) {
			throw new IllegalArgumentException("File: " + inputFileName + " not found");
		}

		// read the RDF/XML file
		model.read(new InputStreamReader(in), "");

		// create a bag
		Bag smiths = model.createBag();

		// select all the resources with a VCARD.FN property
		// whose value ends with "Smith"
		StmtIterator iter = model.listStatements(new SimpleSelector(null, VCARD.FN, (RDFNode) null) {
			@Override
			public boolean selects(Statement s) {
				return s.getString().endsWith("Smith");
			}
		});
		// add the Smith's to the bag
		while (iter.hasNext()) {
			smiths.add(iter.nextStatement().getSubject());
		}

		// print the graph as RDF/XML
		model.write(new PrintWriter(System.out));
		System.out.println();

		// print out the members of the bag
		NodeIterator iter2 = smiths.iterator();
		if (iter2.hasNext()) {
			System.out.println("The bag contains:");
			while (iter2.hasNext()) {
				System.out.println("  " + ((Resource) iter2.next()).getRequiredProperty(VCARD.FN).getString());
			}
		} else {
			System.out.println("The bag is empty");
		}
	}

	public void fun9() {
		// create an empty graph
		Model model = ModelFactory.createDefaultModel();

		// create the resource
		Resource r = model.createResource();

		// add the property
		r.addProperty(RDFS.label, model.createLiteral("chat", "en"))
				.addProperty(RDFS.label, model.createLiteral("chat", "fr"))
				.addProperty(RDFS.label, model.createLiteral("<em>chat</em>", true));

		// write out the graph
		model.write(new PrintWriter(System.out));
		System.out.println();

		// create an empty graph
		model = ModelFactory.createDefaultModel();

		// create the resource
		r = model.createResource();

		// add the property
		r.addProperty(RDFS.label, "11").addLiteral(RDFS.label, 11);

		// write out the graph
		model.write(System.out, "N-TRIPLE");
	}
}
