
import com.beard.BeardDownload
import groovy.lang.Closure

object ScalaCanCallGroovy extends App {

  private val fn = new Closure[Integer]() { override def call() = 11 }

  private val updateFn = new Closure[String]() {
    override def call(name: Object) =
      s"${} from closure"
  }

  val download = new BeardDownload
  val data = download.doSomething(fn)

  assert(data == 11)
}
