package cbt

/**
 * This represents a logger with namespaces that can  be enabled or disabled as needed. The
 * namespaces are defined using {{enabledLoggers}}. Possible values are defined in the subobject
 * "names".
 *
 * We can replace this with something more sophisticated eventually.
 */
case class Logger(enabledLoggers: Set[String], start: Long) {
  def this(enabledLoggers: Option[String], start: Long) = {
    this(
      enabledLoggers.toVector.flatMap( _.split(",") ).toSet,
      start
    )
  }

  val disabledLoggers: Set[String] = enabledLoggers.filter(_.startsWith("-")).map(_.drop(1))

  def log(name: String, msg: => String) = {
    if(
      (
        (enabledLoggers contains name)
        || (enabledLoggers contains "all")
      ) && !(disabledLoggers contains name)
    ){
      logUnguarded(name, msg)
    }
  }

  def showInvocation(method: String, args: Any) = method ++ "( " ++ args.toString ++ " )"

  final def stage1(msg: => String) = log(names.stage1, msg)
  final def stage2(msg: => String) = log(names.stage2, msg)
  final def loop(msg: => String) = log(names.loop, msg)
  final def task(msg: => String) = log(names.task, msg)
  final def composition(msg: => String) = log(names.composition, msg)
  final def resolver(msg: => String) = log(names.resolver, msg)
  final def lib(msg: => String) = log(names.lib, msg)
  final def test(msg: => String) = log(names.test, msg)
  final def git(msg: => String) = log(names.git, msg)
  final def pom(msg: => String) = log(names.pom, msg)
  final def dynamic(msg: => String) = log(names.dynamic, msg)
  final def run(msg: => String) = log(names.run, msg)
  final def transientCache(msg: => String) = log(names.transientCache, msg)

  private object names{
    val stage1 = "stage1"
    val stage2 = "stage2"
    val loop = "loop"
    val task = "task"
    val resolver = "resolver"
    val composition = "composition"
    val lib = "lib"
    val test = "test"
    val pom = "pom"
    val run = "run"
    val git = "git"
    val dynamic = "dynamic"
    val transientCache = "transientCache"
  }

  private def logUnguarded(name: String, msg: => String) = {
    val timeTaken = ((System.currentTimeMillis.toDouble - start) / 1000).toString
    System.err.println( s"[$timeTaken][$name] $msg" )
  }
}
