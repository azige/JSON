# Azige.JSON

What's JSON? See [http://json.org](http://json.org)

## Overview

This is a JSON processing library that implements the APIs in JSR-353.

Besides implementing JSR-353, this project is also to implement JSON binding in the future.

## Examples

Add the jar file of this project to classpath and use the APIs from JSR-353 to process JSON.

Generate some JSON text in this way

	JsonObject obj = Json.createObjectBuilder()
	    .add("name", "bob")
	    .add("age", 20)
	    .add("asset",
	        Json.createArrayBuilder()
	        .add("PC")
	        .add("phone")
	        .add("TV")
	    )
	    .build();
	System.out.println(obj);

	// The order of the pairs may not be same.
	// output: {"name":"bob","age":20,"asset":["PC","phone","TV"]}
