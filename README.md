# jena-RDF
---

## 1 创建三元组

```java
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
```

结果

```
=====================三元组=========================
subject = http://somewhere/JohnSmith
predicate = http://www.w3.org/2001/vcard-rdf/3.0#FN
object = John Smith

=====================主语的属性=========================
subject.URI = http://somewhere/JohnSmith
subject.nameSpace = http://somewhere/
subject.localName = JohnSmith

=====================谓语的属性=========================
predicate.URI = http://www.w3.org/2001/vcard-rdf/3.0#FN
predicate.nameSpace = http://www.w3.org/2001/vcard-rdf/3.0#
predicate.localName = FN
```

## 2 创建图并保存成文件

```java
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
```

结果

data1.rdf

```xml
<rdf:RDF
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:vcard="http://www.w3.org/2001/vcard-rdf/3.0#">
  <rdf:Description rdf:about="http://somewhere/JohnSmith">
    <vcard:N rdf:parseType="Resource">
      <vcard:Family>Smith</vcard:Family>
      <vcard:Given>John</vcard:Given>
    </vcard:N>
    <vcard:FN>John Smith</vcard:FN>
  </rdf:Description>
</rdf:RDF>
```

## 3 图的不格式的存储

```java
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
```

**1.【RDF/XML】:**

```xml
<rdf:RDF
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:vcard="http://www.w3.org/2001/vcard-rdf/3.0#">
  <rdf:Description rdf:about="http://somewhere/JohnSmith">
    <vcard:NICKNAME>Adman</vcard:NICKNAME>
    <vcard:NICKNAME>Smithy</vcard:NICKNAME>
    <vcard:FN>John Smith</vcard:FN>
    <vcard:N rdf:parseType="Resource">
      <vcard:Given>John</vcard:Given>
      <vcard:Family>Smith</vcard:Family>
    </vcard:N>
  </rdf:Description>
</rdf:RDF>
```

**2.【RDF/XML-ABBREV】:**

```xml
<rdf:RDF
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:vcard="http://www.w3.org/2001/vcard-rdf/3.0#">
  <rdf:Description rdf:about="http://somewhere/JohnSmith">
    <vcard:NICKNAME>Adman</vcard:NICKNAME>
    <vcard:NICKNAME>Smithy</vcard:NICKNAME>
    <vcard:FN>John Smith</vcard:FN>
    <vcard:N rdf:parseType="Resource">
      <vcard:Given>John</vcard:Given>
      <vcard:Family>Smith</vcard:Family>
    </vcard:N>
  </rdf:Description>
</rdf:RDF>
```

**3.【N-TRIPLE】:**

```xml
<http://somewhere/JohnSmith> <http://www.w3.org/2001/vcard-rdf/3.0#NICKNAME> "Adman" .
<http://somewhere/JohnSmith> <http://www.w3.org/2001/vcard-rdf/3.0#NICKNAME> "Smithy" .
<http://somewhere/JohnSmith> <http://www.w3.org/2001/vcard-rdf/3.0#FN> "John Smith" .
<http://somewhere/JohnSmith> <http://www.w3.org/2001/vcard-rdf/3.0#N> _:Babcac2b9X2D13beX2D4b1eX2D8286X2D15701483cbf7 .
_:Babcac2b9X2D13beX2D4b1eX2D8286X2D15701483cbf7 <http://www.w3.org/2001/vcard-rdf/3.0#Given> "John" .
_:Babcac2b9X2D13beX2D4b1eX2D8286X2D15701483cbf7 <http://www.w3.org/2001/vcard-rdf/3.0#Family> "Smith" .
```

**4.【TURTLE】:**

```xml
@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix vcard: <http://www.w3.org/2001/vcard-rdf/3.0#> .

<http://somewhere/JohnSmith>
        vcard:FN        "John Smith" ;
        vcard:N         [ vcard:Family  "Smith" ;
                          vcard:Given   "John"
                        ] ;
        vcard:NICKNAME  "Adman" , "Smithy" .
```

**5.【TTL】:**

```xml
@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix vcard: <http://www.w3.org/2001/vcard-rdf/3.0#> .

<http://somewhere/JohnSmith>
        vcard:FN        "John Smith" ;
        vcard:N         [ vcard:Family  "Smith" ;
                          vcard:Given   "John"
                        ] ;
        vcard:NICKNAME  "Adman" , "Smithy" .
```

**6.【N3】:**

```xml
@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix vcard: <http://www.w3.org/2001/vcard-rdf/3.0#> .

<http://somewhere/JohnSmith>
        vcard:FN        "John Smith" ;
        vcard:N         [ vcard:Family  "Smith" ;
                          vcard:Given   "John"
                        ] ;
        vcard:NICKNAME  "Adman" , "Smithy" .
```

## 4 查询与过滤

```java
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
```

结果

```
The database contains vcards for:
  John Smith
```


