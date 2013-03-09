package com.gilt.handlebars.experimental.visitors

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalacheck.{Arbitrary, Gen}
import java.util.Date
import com.gilt.util.ProfilingUtils
import com.gilt.handlebars.{ChildContext, HandlebarsVisitor, Handlebars}
import java.io.File

import scala.language.dynamics
import com.sun.org.apache.xalan.internal.xsltc.compiler.sym
import reflect.api.Symbols
import com.chrhicks.Props


/**
 * User: chicks
 * Date: 3/2/13
 */
class ReflectionPerfTest extends FunSpec with ShouldMatchers with ProfilingUtils with GenHelper with Loggable {
  describe("A ProductBuilder") {
    it("should dynamically invoke") {
      val attrVal = new SkuAttributeValue(123123123l, "attrValue")

      info("dyncall: %s".format(attrVal.get("label")))
    }

//    it("should test invoking a method") {
//      val skus: List[Sku] = Gen.listOfN(10000, Sku.gen).sample.get
//
//      println("Testing HandlebarsVisitor.Context invoke")
//      time("Total: ") {
//        skus.foreach { sku => {
//          val ctx = new ChildContext(sku, None)
//          ctx.invoke("skuId")
//          ctx.invoke("salePrice")
//          ctx.invoke("msrpPrice")
//          ctx.invoke("availability")
//        }}
//      }
//
//      println("Testing ReflectUtils invoke")
//      time("Total: ") {
//        skus.foreach { sku => {
//          ReflectUtils.invoke(sku, "skuId")
//          ReflectUtils.invoke(sku, "salePrice")
//          ReflectUtils.invoke(sku, "msrpPrice")
//          ReflectUtils.invoke(sku, "availability")
//        }}
//      }
//
//      println("Testing Macro invoke")
//      time("Total: ") {
//        skus.foreach { sku => {
//          sku.get("skuId")
//          sku.get("salePrice")
//          sku.get("msrpPrice")
//          sku.get("availability")
//        }}
//      }
//    }
//
//
//    it("should create a product") {
//      val handlebars = Handlebars.fromFile(new File("src/test/resources/saleListing.handlebars"))
//      val testSales: List[Sale] = time("Created Sales in: ") {
//        Gen.listOfN(rndGen.nextInt(15), Sale.gen).sample.get
//      }
//
//      println("Created %s sales.".format(testSales.length))
//      println("Testing HandlebarsVisitor (Original)")
//      time("Testing HandlebarsVisitor (Original) TOTAL: ") {
//        testSales.foreach { sale =>
//          val model = new SalesContainer(List(sale))
//          time("--> Rendered In: ") { HandlebarsVisitor(model).visit(handlebars.program) }
//        }
//      }
//
//      "Hello" should equal("Hello")
//    }
  }
}

trait GenHelper {
  val rndGen = new java.util.Random(63642)
}

case class SalesContainer(sales: List[Sale])

object Sale extends GenHelper {
  def gen: Gen[Sale] = {
    for {
      saleId <- Gen.choose[Long](0, Long.MaxValue)
      name <- Gen.alphaStr
      urlKey <- Gen.alphaStr
      startDate <- Arbitrary.arbitrary[Date]
      endDate <- Arbitrary.arbitrary[Date]
      products <- Gen.listOfN(rndGen.nextInt(100), Product.gen)
    } yield new Sale(saleId, name, urlKey, startDate, endDate, products)
  }
}
case class Sale(saleId: Long, name: String, urlKey: String, startDate: Date, endDate: Date, products: List[Product])

object Product extends GenHelper {
  def gen: Gen[Product] = {
    for {
      productId <- Gen.choose[Long](0, Long.MaxValue)
      name <- Gen.alphaStr
      urlKey <- Gen.alphaStr
      brand <- Brand.gen
      skus <- Gen.listOfN(rndGen.nextInt(50) + 1, Sku.gen)
      descriptions <- Gen.listOfN(rndGen.nextInt(3) + 1, DescriptionElement.gen)
    } yield new Product(productId, name, urlKey, brand, skus.toSet, descriptions)
  }
}
case class Product(productId: Long, name: String, urlKey:String, brand: Brand, skus: Set[Sku], descriptions: List[DescriptionElement])

object Brand extends GenHelper {
  def gen: Gen[Brand] = {
    for {
      brandId <- Gen.choose[Long](0, Long.MaxValue)
      urlKey <- Gen.alphaStr
      name <- Gen.alphaStr
    } yield new Brand(brandId, urlKey, name)
  }
}
case class Brand(brandId: Long, urlKey: String, name: String)

object Image extends GenHelper {
  def gen: Gen[Image] = {
    for {
      imageId <- Gen.choose[Long](0, Long.MaxValue)
      url <-  Gen.oneOf(Seq("/images/share/uploads/0000/0001/1233/%s/orig.jpg".format(imageId)))
    } yield new Image(imageId, url)
  }
}
case class Image(imageId: Long, url: String)

object DescriptionElement extends GenHelper {
  def gen: Gen[DescriptionElement] = {
    for {
      label <- Gen.alphaStr
      content <- Gen.alphaStr
    } yield new DescriptionElement(label, content)
  }
}
case class DescriptionElement(label: String, content: String)

object Availability extends Enumeration {
  type Availability = Value
  val FOR_SALE, RESERVED, SOLD_OUT, UNAVAILABLE = Value
}

object Sku extends GenHelper {
  def gen: Gen[Sku] = {
    for {
      skuId <- Gen.choose[Long](0, Long.MaxValue)
      attributes <- Gen.listOfN[SkuAttribute](rndGen.nextInt(3) + 1, SkuAttribute.gen)
      msrpPrice <- Gen.choose[Double](0d, 1000d)
      salePrice <- Gen.choose[Double](0d, 1000d)
      availability <- Gen.oneOf(Availability.values.toList)
    } yield new Sku(skuId, attributes, msrpPrice, salePrice, availability)
  }
}
case class Sku(skuId: Long, attributes: List[SkuAttribute], msrpPrice: BigDecimal, salePrice: BigDecimal, availability: Availability.Value) {
  def get(propName: String): Any = {
//    propName match {
//      case "skuId" => Props.getValue[this.type](this, "skuId")
//      case "attributes" => Props.getValue[this.type](this, "attributes")
//      case "msrpPrice" => Props.getValue[this.type](this, "msrpPrice")
//      case "salePrice" => Props.getValue[this.type](this, "salePrice")
//      case "availability" => Props.getValue[this.type](this, "availability")
//      case _ => ""
//    }
  }
}

object SkuAttribute extends GenHelper {
  def gen: Gen[SkuAttribute] = {
    for {
      id <- Gen.choose[Long](0, Long.MaxValue)
      label <- Gen.alphaStr
      values <- Gen.listOfN[SkuAttributeValue](rndGen.nextInt(7) + 1, SkuAttributeValue.gen)
    } yield new SkuAttribute(id, label, values)
  }
}
case class SkuAttribute(attributeId: Long, label: String, values: List[SkuAttributeValue])


object SkuAttributeValue extends GenHelper {
  def gen: Gen[SkuAttributeValue] = {
    for {
      id <- Gen.choose[Long](0, Long.MaxValue)
      name <- Gen.alphaStr
    } yield new SkuAttributeValue(id, name)
  }
}
case class SkuAttributeValue(attributeValueId: Long, label: String) {
  import reflect.runtime.universe._
  def get(propName: String): Any = {
      Props.getValueTerm[this.type](this, Literal(Constant(propName)))
//    propName match {
//      case "attributeValueId" => Props.getValue[this.type](this, "attributeValueId")
//      case "label" => Props.getValue[this.type](this, "label")
//      case _ => ""
//    }
  }
}