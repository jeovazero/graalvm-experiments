package object Util {
  def pretty(product: Product): String = {
    val className   = product.productPrefix
    val fieldNames  = product.productElementNames.toList
    val fieldValues = product.productIterator.toList
    val fields =
      fieldNames.zip(fieldValues).map { case (name, value) =>
        s"$name = $value"
      }

    fields.mkString(s"$className(", ", ", ")")
  }
}
