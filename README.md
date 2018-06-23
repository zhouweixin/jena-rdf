# jena-RDF
---

## 不同格式的表示形式

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


