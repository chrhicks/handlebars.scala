package com.gilt.handlebars.experimental.visitors

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{FunSpec, FlatSpec}
import com.gilt.handlebars.{HandlebarsGrammar, Program}

/**
 * User: chicks
 * Date: 3/1/13
 */
class ScalaReflectVisitorSpec extends FunSpec with ShouldMatchers {
  describe("A ScalaReflectVisitor") {
    it("should render a constant") {
      val raw = "Hello"
      val program: Program = HandlebarsGrammar(("{{", "}}")).scan(raw)
      val visitor = ScalaReflectVisitor(new {})

      visitor.visit(program) should equal("Hello")
    }

    it("should not render a comment") {
      val raw = "{{! a comment}}"
      val program: Program = HandlebarsGrammar(("{{", "}}")).scan(raw)
      val visitor = ScalaReflectVisitor(new {})

      visitor.visit(program) should equal("")
    }

    it("should render a mustache") {
      case class Location(city: String)

      val raw = "{{city}}"
      val program: Program = HandlebarsGrammar(("{{", "}}")).scan(raw)
      val model = new Location("New York")
      val visitor = ScalaReflectVisitor(model)

      visitor.visit(program) should equal("New York")
    }
  }
}
