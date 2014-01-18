# Azige.JSON

What's JSON? See [http://json.org](http://json.org)

## Overview

This is a simple JSON Library in Java that implements the features below.

* The JSON-Type class structure.
* Convert JSON-Type object to JSON-Text.
* Wrap any object of fit type into JSON-Type object.
* Wrap a JavaBean object into JSON-Object object.
* Parse JSON-Text into JSON-Type object. (incoming)
* Put the data from JSON-Object into JavaBean. (incoming)

## Examples

Generate some JSON-Text in this way

	JSONObject person = new JSONObject();
	person.put("name", "bob");
	person.put("age", 12);
	person.put("asset", new String[]{"PC", "phone", "TV"});
	System.out.println(person);

	// The order of the pairs may not be same.
	// output: {"name":"bob","age":20,"asset":["PC","phone","TV"]}

or in this way

	// Person is a JavaBean
	class Person{
	    String name;
	    int age;
	    String[] asset;

	    // Constructors, setters and getters.
	}

	Person person = new Person("bob", 20, new String[]{"PC", "phone", "TV"});
	System.out.println(JSONType.valueOf(person));

	// The order of the pairs may not be same.
	// output: {"name":"bob","age":20,"asset":["PC","phone","TV"]}
