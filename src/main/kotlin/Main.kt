fun main() {

    val start = System.currentTimeMillis()

    // thread n 개 생성.
    val t = MainThread()
    t.run()
    t.run()

    val end = System.currentTimeMillis()
    println("elapsed time : ${end - start}ms")
}